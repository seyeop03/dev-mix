package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.common.Position;
import msa.devmix.domain.common.TechStack;
import msa.devmix.domain.user.User;
import msa.devmix.domain.user.UserPosition;
import msa.devmix.domain.user.UserTechStack;
import msa.devmix.dto.TechStackDto;
import msa.devmix.dto.UserBoardsDto;
import msa.devmix.dto.UserWithPositionTechStackDto;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.*;
import msa.devmix.service.FileService;
import msa.devmix.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserPositionRepository userPositionRepository;
    private final UserTechStackRepository userTechStackRepository;
    private final BoardRepository boardRepository;
    private final FileService fileService;
    private final PositionRepository positionRepository;
    private final TechStackRepository techStackRepository;

    //유저 ID로 유저 엔티티 조회
    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    //username 으로 user 테이블에서 유저를 찾고, 없으면 예외 발생
    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    //사용자 정보 및 관련된 기술 스택 & 포지션 정보 조회 (페치 조인)
    @Override
    public UserWithPositionTechStackDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<String> positionNames = userPositionRepository.findWithPositionByUser(user);
        List<TechStackDto> techStackNames = userTechStackRepository.findTechStackDtoByUser(user);

        return UserWithPositionTechStackDto.of(user, positionNames, techStackNames);
    }

    //본인 게시글 리스트 조회
    @Override
    public List<UserBoardsDto> getUserBoards(Long userId, Pageable pageable) {

        List<Board> boards = boardRepository.findByUserId(userId, pageable)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return boards.stream()
                .map(UserBoardsDto::from)
                .toList();
    }

    //닉네임 중복 확인
    @Override
    public void checkNickname(String nickname) {
        validateUserNickname(nickname);
    }

    private void validateUserNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    //유저 프로필 설정
    @Transactional
    @Override
    public void saveUserProfile(UserWithPositionTechStackDto dto, MultipartFile profileImage) throws IOException {

        /**
         * 0. Validation Check -> Nickname, Position, TechStack
         */
        //nickname 검증
        validateUserNickname(dto.getNickname());

        //positionList 검증
        List<String> positionNameList = dto.getPositions();
        List<Position> newPositions = positionRepository.findByPositionNameIn(positionNameList);
        if (positionNameList != null && newPositions.size() != positionNameList.size()) {
            throw new CustomException(ErrorCode.POSITION_NOT_FOUND);
        }

        //techStackList 검증
        List<String> techStackNameList = dto.getTechStacks()
                .stream()
                .map(TechStackDto::getTechStackName)
                .toList();
        List<TechStack> newTechStacks = techStackRepository.findByTechStackNameIn(techStackNameList);
        if (newTechStacks.size() != techStackNameList.size()) { //이미 Request 에서 Dto 로 변환시 ArrayList 생성해주므로 null 체크 필요 x
            throw new CustomException(ErrorCode.TECH_STACK_NOT_FOUND);
        }

        /**
         * 1. 유저 저장
         */
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String profileImageUrl = fileService.uploadFile(profileImage);
        user.setAdditionalUserInfo(dto, profileImageUrl);

        /**
         * 2. 유저의 포지션들 저장
         */
        //1. 기존 자식 엔티티 조회 후 Map 으로 변환 (positionName 을 키로 사용)
        List<UserPosition> existingUserPositions = userPositionRepository.findUserPositionWithPositionByUserId(dto.getUserId());
        Map<String, UserPosition> existingUserPositionMap = existingUserPositions.stream()
                .collect(Collectors.toMap(
                        userPosition -> userPosition.getPosition().getPositionName(),
                        userposition -> userposition)
                );

        //2. 새로운 엔티티 리스트를 Map 으로 변환 (positionName 을 키로 사용)
        Map<String, Position> newPositionMap = newPositions.stream()
                .collect(Collectors.toMap(
                        Position::getPositionName,
                        newPosition -> newPosition)
                );

        //3. 삭제할 엔티티 필터링 후 삭제 (기존 엔티티에 있지만 새로운 DTO 에는 없는 경우)
        List<UserPosition> removeUserPositions = existingUserPositions.stream()
                .filter(existingUserPosition -> !newPositionMap.containsKey(existingUserPosition.getPosition().getPositionName()))
                .toList();

        //4. 업데이트할 엔티티 처리
        List<UserPosition> updateUserPositions = newPositions.stream()
                .filter(newPosition -> !existingUserPositionMap.containsKey(newPosition.getPositionName()))
                .map(newPosition -> UserPosition.of(user, newPosition))
                .toList();

        userPositionRepository.deleteAllInBatch(removeUserPositions);
        userPositionRepository.saveAll(updateUserPositions);

        /**
         * 3. 유저의 기술 스택들 저장
         */
        //1. 기존 자식 엔티티 조회 후 Map 으로 변환 (techStackName 을 키로 사용)
        List<UserTechStack> existingUserTechStacks = userTechStackRepository.findWithTechStackByUserId(dto.getUserId());
        Map<String, UserTechStack> existingUserTechStackMap = existingUserTechStacks.stream()
                .collect(Collectors.toMap(
                        userTechStack -> userTechStack.getTechStack().getTechStackName(),
                        userTechStack -> userTechStack
                ));

        //2. 새로운 엔티티 리스트를 Map 으로 변환 (techStackName 을 키로 사용)
        Map<String, TechStack> newTechStackMap = newTechStacks.stream()
                .collect(Collectors.toMap(
                        TechStack::getTechStackName,
                        newTechStack -> newTechStack
                ));

        //3. 삭제할 엔티티 필터링 후 삭제 (기존 엔티티에 있지만 새로운 DTO 에는 없는 경우)
        List<UserTechStack> removeUserTechStacks = existingUserTechStacks.stream()
                .filter(existingUserTechStack -> !newTechStackMap.containsKey(existingUserTechStack.getTechStack().getTechStackName()))
                .toList();

        //4. 업데이트할 엔티티 처리
        List<UserTechStack> updateUserTechStacks = newTechStacks.stream()
                .filter(newTechStack -> !existingUserTechStackMap.containsKey(newTechStack.getTechStackName()))
                .map(newTechStack -> UserTechStack.of(user, newTechStack))
                .toList();

        userTechStackRepository.deleteAllInBatch(removeUserTechStacks);
        userTechStackRepository.saveAll(updateUserTechStacks);
    }
}

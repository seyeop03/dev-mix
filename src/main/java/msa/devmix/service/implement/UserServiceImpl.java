package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.user.User;
import msa.devmix.domain.user.UserPosition;
import msa.devmix.domain.user.UserTechStack;
import msa.devmix.dto.TechStackDto;
import msa.devmix.dto.UserBoardsDto;
import msa.devmix.dto.UserDto;
import msa.devmix.dto.UserWithPositionTechStackDto;
import msa.devmix.dto.request.CheckNicknameRequest;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.*;
import msa.devmix.service.FileService;
import msa.devmix.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


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

    //사용자 정보 및 관련된 기술 스택 & 포지션 정보 조회 => 페치 조인 필요
    @Override
    public UserWithPositionTechStackDto getUserInfo(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<String> positionNames = userPositionRepository.findWithPositionByUser(user);
        List<TechStackDto> techStackNames = userTechStackRepository.findWithTechStackByUser(user);

        return UserWithPositionTechStackDto.of(user, positionNames, techStackNames);
    }

    //본인 게시글 리스트 조회
    @Override
    public List<UserBoardsDto> findUserBoards(Long userId, Pageable pageable) {

        List<Board> boards = boardRepository.findByUserId(userId, pageable)
                .orElseThrow(() -> new CustomException(ErrorCode.BOARD_NOT_FOUND));

        return boards.stream()
                .map(UserBoardsDto::from)
                .toList();
    }

    //닉네임 중복 확인
    @Override
    public void checkNickname(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    //유저 프로필 설정
    @Transactional
    @Override
    public void saveUserProfile(UserWithPositionTechStackDto dto, MultipartFile profileImage) throws IOException {
        //1. 유저 저장
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        String profileImageUrl = fileService.uploadFile(profileImage);
        user.setAdditionalUserInfo(dto, profileImageUrl);

        //2. 유저의 포지션들 저장
        List<UserPosition> userPositions = positionRepository.findByPositionNameIn(dto.getPositionNames())
                .stream()
                .map(position -> UserPosition.of(user, position))
                .toList();
        userPositionRepository.saveAll(userPositions);

        //3. 유저의 기술 스택들 저장
        List<String> techStackList = dto.getTechStackDtos()
                .stream()
                .map(TechStackDto::getTechStackName)
                .toList();
        List<UserTechStack> userTechStacks = techStackRepository.findByTechStackNameIn(techStackList)
                .stream()
                .map(techStack -> UserTechStack.of(user, techStack))
                .toList();
        userTechStackRepository.saveAll(userTechStacks);
    }
}

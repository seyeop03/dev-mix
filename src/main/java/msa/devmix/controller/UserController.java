package msa.devmix.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.config.oauth.userinfo.UserPrincipal;
import msa.devmix.domain.common.Position;
import msa.devmix.domain.common.TechStack;
import msa.devmix.dto.UserDto;
import msa.devmix.dto.request.CheckNicknameRequest;
import msa.devmix.dto.request.UserProfileRequest;
import msa.devmix.dto.response.ResponseDto;
import msa.devmix.dto.response.UserBoardsResponse;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.PositionRepository;
import msa.devmix.repository.TechStackRepository;
import msa.devmix.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final PositionRepository positionRepository;
    private final TechStackRepository techStackRepository;

    /**
     * 로그인된 사용자 본인 정보 조회
     */
    @GetMapping
    public ResponseEntity<?> getSignInUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("UserDto: {}", UserDto.from(userPrincipal.getUser()).getUsername());
        return ResponseEntity.ok()
                .body(ResponseDto.success(UserDto.from(userPrincipal.getUser())));
    }

    /**
     * 사용자 상세 프로필 조회
     */
    @GetMapping("/{user-id}")
    public ResponseEntity<?> getUser(@PathVariable("user-id") Long userId) {
        return ResponseEntity.ok()
                .body(ResponseDto.success(userService.getUserInfo(userId)));
    }

    /**
     * 사용자 정보 입력: 소셜 로그인 이후에야 사용자 정보 입력이 가능하므로 인증이 되어야 함.
     */
    @PutMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> userProfile(@Valid @RequestPart("userProfile") UserProfileRequest userProfileRequest,
                                         @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
                                         @AuthenticationPrincipal UserPrincipal userPrincipal) throws IOException {
        //positionList 검증
        List<String> positionList = userProfileRequest.getPositionList();
        List<Position> positions = positionRepository.findByPositionNameIn(userProfileRequest.getPositionList());
        if (positionList != null && positions.size() != positionList.size()) {
            throw new CustomException(ErrorCode.POSITION_NOT_FOUND);
        }

        //techStackList 검증
        List<String> techStackList = userProfileRequest.getTechStackList();
        List<TechStack> techStacks = techStackRepository.findByTechStackNameIn(userProfileRequest.getTechStackList());
        if (techStackList != null && techStacks.size() != techStackList.size()) {
            throw new CustomException(ErrorCode.TECH_STACK_NOT_FOUND);
        }


        //User 프로필 설정 + (User 기술 스택 설정 + User 포지션 설정)
        userService.saveUserProfile(userProfileRequest.toDto(userPrincipal.getUser()), profileImage);
        return ResponseEntity.ok()
                .body(ResponseDto.success());
    }

    /**
     * 닉네임 중복 확인
     */
    @PostMapping("/nickname-check")
    public ResponseEntity<?> checkNickname(@Valid @RequestBody CheckNicknameRequest checkNicknameRequest) {
        userService.checkNickname(checkNicknameRequest.getNickname());
        return ResponseEntity.ok()
                .body(ResponseDto.success());
    }

    /**
     * 본인 게시글 목록 조회
     */
    @GetMapping("/{user-id}/boards")
    public ResponseEntity<?> getUserBoards(@PathVariable("user-id") @Min(1) Long userId,
                                           @PageableDefault Pageable pageable,
                                           @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (!Objects.equals(userPrincipal.getUser().getId(), userId)) {
            throw new CustomException(ErrorCode.AUTHORIZATION_FAILED);
        }

        return ResponseEntity.ok()
                .body(ResponseDto.success(
                        userService.findUserBoards(userId, pageable)
                                .stream()
                                .map(UserBoardsResponse::from)
                                .toList()
                        )
                );
    }
}

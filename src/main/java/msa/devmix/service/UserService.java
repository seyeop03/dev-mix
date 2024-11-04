package msa.devmix.service;

import jakarta.validation.constraints.Min;
import msa.devmix.domain.board.Board;
import msa.devmix.domain.user.User;
import msa.devmix.dto.UserBoardsDto;
import msa.devmix.dto.UserDto;
import msa.devmix.dto.UserWithPositionTechStackDto;
import msa.devmix.dto.request.CheckNicknameRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User findById(Long userId);
    User findByUsername(String username);
    UserWithPositionTechStackDto getUserInfo(Long userId);
    void checkNickname(String nickname);
    void saveUserProfile(UserWithPositionTechStackDto userWithPositionTechStackDto, MultipartFile profileImage) throws IOException;

    List<UserBoardsDto> findUserBoards(Long userId, Pageable pageable);
}

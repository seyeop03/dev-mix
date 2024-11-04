package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import msa.devmix.config.jwt.JwtProvider;
import msa.devmix.domain.user.User;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.service.RefreshTokenService;
import msa.devmix.service.TokenService;
import msa.devmix.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenServiceImpl implements TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;


    //리프레시 토큰으로 새로운 액세스 토큰 생성
    @Transactional
    public String createNewAccessToken(String refreshToken) {

        // 토큰 유효성 검사에 실패하면 예외 발생
        if (!jwtProvider.validToken(refreshToken)) {
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        User user = userService.findById(userId);

        return jwtProvider.generateToken(user, Duration.ofHours(2));
    }
}

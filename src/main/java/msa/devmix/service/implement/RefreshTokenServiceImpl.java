package msa.devmix.service.implement;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.config.jwt.JwtProvider;
import msa.devmix.domain.token.RefreshToken;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.RefreshTokenRepository;
import msa.devmix.service.RefreshTokenService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    //리프레시 토큰 조회
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new CustomException(ErrorCode.REFRESH_TOKEN_NOT_FOUND));
    }

    //리프레시 토큰 삭제
    @Transactional
    public void delete() {
        String token = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        Long userId = jwtProvider.getUserId(token);
        log.info("userId: {}", userId);

        refreshTokenRepository.deleteByUserId(userId);
    }
}

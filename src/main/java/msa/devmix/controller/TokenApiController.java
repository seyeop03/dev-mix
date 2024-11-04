package msa.devmix.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.dto.request.CreateAccessTokenRequest;
import msa.devmix.dto.response.CreateAccessTokenResponse;
import msa.devmix.dto.response.ResponseDto;
import msa.devmix.service.RefreshTokenService;
import msa.devmix.service.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
@Slf4j
public class TokenApiController {

    private final TokenService tokenService;
    private final RefreshTokenService refreshTokenService;

    /**
     * 리프레시 토큰을 기반으로 새로운 액세스 토큰을 생성
     */
    @PostMapping
    public ResponseEntity<?> createNewAccessToken(@Valid @RequestBody CreateAccessTokenRequest request) {
        log.info("Create new access token from refresh token.");
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ResponseDto.success(new CreateAccessTokenResponse(newAccessToken)));
    }

    /**
     * 리프레시 토큰 삭제
     */
    @DeleteMapping("/refresh-token")
    public ResponseEntity<?> deleteRefreshToken() {
        log.info("Delete refresh token.");
        refreshTokenService.delete();

        return ResponseEntity.ok().body(ResponseDto.success());
    }
}

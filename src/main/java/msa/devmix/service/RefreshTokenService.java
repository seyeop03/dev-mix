package msa.devmix.service;

import msa.devmix.domain.token.RefreshToken;

public interface RefreshTokenService {
    void delete();
    RefreshToken findByRefreshToken(String refreshToken);
}

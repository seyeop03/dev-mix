package msa.devmix.service;

public interface TokenService {
    String createNewAccessToken(String refreshToken);
}

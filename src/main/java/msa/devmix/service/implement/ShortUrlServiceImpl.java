package msa.devmix.service.implement;

import lombok.RequiredArgsConstructor;
import msa.devmix.domain.url.ShortUrl;
import msa.devmix.dto.ShortUrlInformationDto;
import msa.devmix.dto.request.CreateShortUrlRequest;
import msa.devmix.dto.response.ShortUrlResponse;
import msa.devmix.exception.CustomException;
import msa.devmix.exception.ErrorCode;
import msa.devmix.repository.ShortUrlRepository;
import msa.devmix.service.ShortUrlService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ShortUrlServiceImpl implements ShortUrlService {

    private static final int MAX_RETRY_COUNT = 5;

    private final ShortUrlRepository shortUrlRepository;

    @Override
    @Transactional
    public ShortUrlResponse generateShortUrl(CreateShortUrlRequest createShortUrlRequest) {
        String originalUrl = createShortUrlRequest.getOriginalUrl();
        String ShortUrlKey = getUniqueShortUrlKey();

        ShortUrl shortUrl = ShortUrl.of(originalUrl, ShortUrlKey);
        shortUrlRepository.save(shortUrl);

        return ShortUrlResponse.of(shortUrl);
    }

    @Override
    public String getOriginalUrlByShortUrlKey(String shortUrlKey) {
        ShortUrl shortUrl = shortUrlRepository.findByShortUrlKey(shortUrlKey)
                .orElseThrow(() -> new CustomException(ErrorCode.SHORT_URL_NOT_FOUND, shortUrlKey));

        shortUrl.increaseRedirectCount();

        return shortUrl.getOriginalUrl();
    }

    @Override
    public ShortUrlInformationDto getShortUrlInformationByShortUrlKey(String ShortUrlKey) {
        ShortUrl shortUrl = shortUrlRepository.findByShortUrlKey(ShortUrlKey)
                .orElseThrow(() -> new CustomException(ErrorCode.SHORT_URL_NOT_FOUND, ShortUrlKey));

        return ShortUrlInformationDto.from(shortUrl);
    }

    private String getUniqueShortUrlKey() {
        return Stream.generate(ShortUrl::generateShortUrlKey)
                .limit(MAX_RETRY_COUNT)
                .filter(key -> !shortUrlRepository.existsByShortUrlKey(key))
                .findFirst()
                .orElseThrow(() -> new CustomException(ErrorCode.SHORTEN_URL_KEY_EXHAUSTED));
    }

}


package msa.devmix.service;

import msa.devmix.dto.ShortUrlInformationDto;
import msa.devmix.dto.request.CreateShortUrlRequest;
import msa.devmix.dto.response.ShortUrlResponse;

public interface ShortUrlService {
    ShortUrlResponse generateShortUrl(CreateShortUrlRequest createShortUrlRequest);
    String getOriginalUrlByShortUrlKey(String shortUrlKey);
    ShortUrlInformationDto getShortUrlInformationByShortUrlKey(String shortUrlKey);
}

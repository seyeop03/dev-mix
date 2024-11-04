package msa.devmix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import msa.devmix.domain.url.ShortUrl;


@Getter
@AllArgsConstructor
public class ShortUrlInformationDto {

    private String originalUrl;
    private String shortUrlKey;
    private Long redirectCount;

    public static ShortUrlInformationDto from(ShortUrl shortUrl) {
        return new ShortUrlInformationDto(shortUrl.getOriginalUrl(), shortUrl.getShortUrlKey(), shortUrl.getRedirectCount());
    }
}

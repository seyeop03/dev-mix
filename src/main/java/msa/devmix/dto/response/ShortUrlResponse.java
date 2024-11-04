package msa.devmix.dto.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import msa.devmix.domain.url.ShortUrl;


@Getter
@AllArgsConstructor
public class ShortUrlResponse {

    private String originalUrl;
    private String shortUrlKey;

    public static ShortUrlResponse of(ShortUrl shortenUrl) {
        return new ShortUrlResponse(shortenUrl.getOriginalUrl(), shortenUrl.getShortUrlKey());
    }
}

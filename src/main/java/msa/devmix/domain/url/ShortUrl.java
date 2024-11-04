package msa.devmix.domain.url;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Random;


@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class ShortUrl {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "short_url_id")
    private Long id;

    private String originalUrl;
    private String shortUrlKey;
    private Long redirectCount;

    private ShortUrl(String originalUrl, String shortUrlKey, Long redirectCount) {
        this.originalUrl = originalUrl;
        this.shortUrlKey = shortUrlKey;
        this.redirectCount = redirectCount;
    }

    public static ShortUrl of(String originalUrl, String shortUrlKey) {
        return new ShortUrl(originalUrl, shortUrlKey, 0L);
    }

    public void increaseRedirectCount() {
        redirectCount += 1;
    }

    public static String generateShortUrlKey() {
        String base56Characters = "23456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnpqrstuvwxyz";
        Random random = new Random();
        StringBuilder shortenUrlKey = new StringBuilder();

        for (int count = 0; count < 8; count++) {
            int base56CharactersIndex = random.nextInt(0, base56Characters.length());
            char base56Character = base56Characters.charAt(base56CharactersIndex);

            shortenUrlKey.append(base56Character);
        }

        return shortenUrlKey.toString();
    }

}

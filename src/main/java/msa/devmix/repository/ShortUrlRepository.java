package msa.devmix.repository;

import msa.devmix.domain.url.ShortUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShortUrlRepository extends JpaRepository<ShortUrl, Long> {

    Optional<ShortUrl> findByShortUrlKey(String shortUrl);
    boolean existsByShortUrlKey(String shortUrlKey);
}

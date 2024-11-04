package msa.devmix.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/short-url")
public class ShortUrlController {

    //shortUrl 생성
    @PostMapping
    public ResponseEntity<?> createShortUrl() {
        return null;
    }

    //shortUrl 리다이렉트
    @GetMapping("/{short-url-key}")
    public ResponseEntity<?> redirectShortUrl(@PathVariable("short-url-key") String shortUrlKey) {
        return null;
    }

    //shortUrl 정보 조회
    @GetMapping("/{short-url-key}/info")
    public ResponseEntity<?> getShortUrlInfo(@PathVariable("short-url-key") String shortUrlKey) {
        return null;
    }
}

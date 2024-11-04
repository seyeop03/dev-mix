package msa.devmix.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import msa.devmix.domain.user.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class JwtProvider {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    public String generateToken(User user, Duration expireAt) {
        Date now = new Date();
        return makeToken(new Date(now.getTime() + expireAt.toMillis()), user);
    }

    //JWT 토큰 생성
    private String makeToken(Date expiry, User user) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setIssuer(issuer)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .setSubject(user.getUsername())
                .claim("id", user.getId()) //private claim
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //JWT 토큰 검증 메서드
    public boolean validToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    //토큰으로 Authentication 가져와서 시큐리티 컨텍스트에 설정
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token); //클레임 정보 가져옴
        Set<SimpleGrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        //우리가 만든 User 가 아니라, 스프링 시큐리티에서 제공하는 User 객체 사용
        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(claims.getSubject(),"", authorities),
                token,
                authorities
        );
    }

    //3-2. 토큰 기반으로 유저 ID를 가져오는 메서드
    public Long getUserId(String token) {
        Claims claims = getClaims(token); //클레임 정보 가져옴
        return claims.get("id", Long.class); //클레임에서 id 키로 저장된 값을 반환
    }

    //토큰으로부터 클레임 정보 조회
    private Claims getClaims(String token) {
        return Jwts.parser() // 클레임 조회
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }
}
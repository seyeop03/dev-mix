package msa.devmix.config.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.exception.CustomException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailService userDetailService;
    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        log.info("Filter request URI -> {}",request.getRequestURL().toString());
        String authorizationHeader = request.getHeader("Authorization");
        String token = getAccessToken(authorizationHeader);

        try {
            if (jwtProvider.validToken(token)) {
                log.info("Filter validation success!");
                UserDetails userDetails = userDetailService.loadUserByUserId(jwtProvider.getUserId(token));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//            Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (CustomException e) {
            //인증이 필요하지 않은 엔드포인트에 대해서..
            log.info("Filter validation failed! {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String getAccessToken(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}

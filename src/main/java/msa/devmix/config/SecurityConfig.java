package msa.devmix.config;

import lombok.RequiredArgsConstructor;
import msa.devmix.config.jwt.JwtAuthenticationFilter;
import msa.devmix.config.jwt.JwtProvider;
import msa.devmix.config.jwt.UserDetailService;
import msa.devmix.config.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import msa.devmix.config.oauth.OAuth2SuccessHandler;
import msa.devmix.config.oauth.OAuth2UserCustomService;
import msa.devmix.repository.RefreshTokenRepository;
import msa.devmix.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@RequiredArgsConstructor
@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {

    private final OAuth2UserCustomService oAuth2UserCustomService;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserDetailService userDetailService;

    @Bean
    public WebSecurityCustomizer configure() {  // 스프링 시큐리티 기능 비활성화
        return (web) -> web.ignoring()
                .requestMatchers(
                        new AntPathRequestMatcher("/images/**"),
                        new AntPathRequestMatcher("/css/**"),
                        new AntPathRequestMatcher("/js/**")
                );
    }

    //OAuth 2.0 프로토콜에 대한 인증은 OAuth2LoginAuthenticationFilter 를 거친다.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //1. 토큰 방식으로 인증하므로 기존에 사용하던 폼 로그인, 세션 비활성화
        return http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        management -> management.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))
                //2. 헤더 확인할 커스텀 필터 추가 (우리가 작성했던 JwtAuthenticationFilter 써먹음)
                .addFilterBefore(jwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class)
                //3. 토큰 재발급 URL 은 인증 없이 접근하도록 설정. 나머지 API URL 은 인증 필요
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                new AntPathRequestMatcher("/api/v1/tokens"),
                                new AntPathRequestMatcher("/api/v1/boards/**", "GET"),
                                new AntPathRequestMatcher("/api/v1/users/**", "GET")
                        ).permitAll()
                        .requestMatchers(new AntPathRequestMatcher("/api/**")).authenticated()
                        .anyRequest().permitAll())
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login")
                        //4. Authorization 요청과 관련된 상태 저장
                        //OAuth2에 필요한 정보를 세션이 아닌 쿠키에 저장해서 쓸 수 있도록 인증 요청과 관련된 상태를 저장할 저장소를 설정
                        .authorizationEndpoint(authorizationEndpoint ->
                                authorizationEndpoint.authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository()))
                        //리소스 서버에서 OAuth 사용자 정보 받아오고, DB에 저장
                        .userInfoEndpoint(userInfoEndpoint
                                -> userInfoEndpoint.userService(oAuth2UserCustomService))
                        //5. 인증 성공 시 실행할 핸들러 설정
                        .successHandler(oAuth2SuccessHandler())
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint()) //인증 실패시
                        .accessDeniedHandler(new CustomAccessDeniedHandler())
                )
                .build();
    }

    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(
                jwtProvider,
                refreshTokenRepository,
                oAuth2AuthorizationRequestBasedOnCookieRepository()
        );
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(userDetailService, jwtProvider);
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //Make the below setting as * to allow connection from any host

        //내 서버가 응답을 할 때 json 을 자바스크립트에서 처리할 수 있게 할지 설정
        corsConfiguration.addAllowedOrigin("http://localhost:5173"); //모든 ip에 응답을 허용
        corsConfiguration.addAllowedMethod("*"); //모든 post,get,put,delete 에 응답을 허용
        corsConfiguration.addAllowedHeader("*"); //모든 header 에 응답을 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        //source.registerCorsConfiguration("/api/v1/**", corsConfiguration);
        return source;
    }

}

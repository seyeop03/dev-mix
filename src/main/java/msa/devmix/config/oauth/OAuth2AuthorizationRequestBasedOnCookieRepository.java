package msa.devmix.config.oauth;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import msa.devmix.util.CookieUtil;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.web.util.WebUtils;


public class OAuth2AuthorizationRequestBasedOnCookieRepository implements
        AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    // 인증 관련 설정값, 쿠키 제거
    public void removeAuthorizationRequestCookies(
            HttpServletRequest request, HttpServletResponse response)
    {
        CookieUtil.deleteCookie(request, response, "oauth2_auth_request");
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(
            HttpServletRequest request, HttpServletResponse response) {
        return this.loadAuthorizationRequest(request);
    }

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "oauth2_auth_request");
        return CookieUtil.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    @Override
    public void saveAuthorizationRequest(
            OAuth2AuthorizationRequest authorizationRequest,
            HttpServletRequest request,
            HttpServletResponse response)
    {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        CookieUtil.addCookie(response, "oauth2_auth_request", CookieUtil.serialize(authorizationRequest), 18000);
    }
}
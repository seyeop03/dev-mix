package msa.devmix.config.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import msa.devmix.config.oauth.userinfo.*;
import msa.devmix.domain.constant.Role;
import msa.devmix.domain.user.User;
import msa.devmix.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;


@Slf4j
@Service
@RequiredArgsConstructor
public class OAuth2UserCustomService extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;

    /* 리소스 서버에서 보내주는 사용자 정보를 불러와 DB 에 저장하는 메서드 => 성공 시 커스터마이징한 OAuth2SuccessHandler 로 이동 */
    // OAuth2UserRequest 는 code 를 받아서 accessToken 을 응답 받은 객체
    @Override
    public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
        //부모에게 요청을 던지면 유저 정보를 담은 객체를 받을 수 있음
        OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

        //Attribute 를 파싱해서 공통 객체로 묶어 관리가 편하도록 함.
        OAuth2UserInfo oAuth2UserInfo = null;
        if (oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("google")) {
            oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
            oAuth2UserInfo = new KakaoUserInfo(oAuth2User.getAttributes());
        } else if (oAuth2UserRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            //네이버는 getAttributes() 에 [resultcode, message, response] 가 날라오고, response 안에 회원 정보가 존재
            oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
        } else {
            log.info("Not supported social platform.");
        }


//        String nickName = oAuth2UserInfo.getProviderName();
        String password = bCryptPasswordEncoder.encode("dummy");
        String email = oAuth2UserInfo.getProviderEmail();
        String provider = oAuth2UserInfo.getProvider();
        String providerId = oAuth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        Role role = Role.ROLE_GUEST;

        User user = userRepository.findByUsername(username).orElse(null);

        //최초 SNS 로그인 시 회원가입
        if (user == null) {
            user = User.of(username, password, null, email, null, null, provider, providerId, role);
            userRepository.save(user);
            log.info("Process sign-up");
        } else { //이미 SNS 로그인 한 적 있으면 회원가입 X => 바로 로그인 진행
            log.info("Your account is already created.");
        }

        return new UserPrincipal(user, oAuth2User.getAttributes());
    }
}
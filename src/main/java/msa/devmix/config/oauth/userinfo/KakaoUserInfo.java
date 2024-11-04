package msa.devmix.config.oauth.userinfo;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes; // getAttributes()

    public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderEmail() {
        return null;
    }

    @Override
    public String getProviderName() {
        return (String) attributes.get("name");
    }

}

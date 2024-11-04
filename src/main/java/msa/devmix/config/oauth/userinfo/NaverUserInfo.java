package msa.devmix.config.oauth.userinfo;

import java.util.Map;

public class NaverUserInfo implements OAuth2UserInfo {

    private Map<String, Object> attributes; // getAttributes()

    public NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getProviderEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getProviderName() {
        return (String) attributes.get("name");
    }
}
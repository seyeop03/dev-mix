package msa.devmix.config.oauth.userinfo;


public interface OAuth2UserInfo {
    String getProviderId();
    String getProvider();
    String getProviderEmail();
    String getProviderName();
}

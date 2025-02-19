package com.simpleject.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client")
public class OAuthProperties {
    private Map<String, Client> registration;
    private Map<String, Provider> provider;
    private String redirectUri;

    @Getter
    @Setter
    public static class Client {
        private String clientId;
        private String clientSecret;
        private String scope;
    }

    @Getter
    @Setter
    public static class Provider {
        private String authorizationUri;
        private String tokenUri;
        private String userInfoUri;
    }
}

package com.simpleject.service;

import com.simpleject.config.OAuthProperties;
import com.simpleject.dto.response.LoginResponse;
import com.simpleject.entity.User;
import com.simpleject.repository.jpa.UserRepository;
import com.simpleject.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuthService {

    private final OAuthProperties oAuthProperties;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 소셜 로그인 URL 생성 (Google, Kakao, GitHub 등)
    public String getOAuthLoginUrl(String provider) {
        OAuthProperties.Client client = oAuthProperties.getRegistration().get(provider);
        OAuthProperties.Provider providerInfo = oAuthProperties.getProvider().get(provider);

        if (client == null || providerInfo == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth 제공자입니다: " + provider);
        }

        return providerInfo.getAuthorizationUri() +
                "?client_id=" + client.getClientId() +
                "&redirect_uri=" + oAuthProperties.getRedirectUri() +
                "&response_type=code" +
                "&scope=" + client.getScope();
    }

    // OAuth2 Callback 처리 (Google, Kakao, GitHub 등)
    public LoginResponse handleOAuthCallback(String provider, String code) {
        OAuthProperties.Client client = oAuthProperties.getRegistration().get(provider);
        OAuthProperties.Provider providerInfo = oAuthProperties.getProvider().get(provider);

        if (client == null || providerInfo == null) {
            throw new IllegalArgumentException("지원하지 않는 OAuth 제공자입니다: " + provider);
        }

        RestTemplate restTemplate = new RestTemplate();

        // 액세스 토큰 요청
        ResponseEntity<Map> response = restTemplate.exchange(
                providerInfo.getTokenUri() +
                        "?client_id=" + client.getClientId() +
                        "&client_secret=" + client.getClientSecret() +
                        "&code=" + code +
                        "&grant_type=authorization_code" +
                        "&redirect_uri=" + oAuthProperties.getRedirectUri(),
                HttpMethod.POST,
                null,
                Map.class
        );

        String accessToken = (String) response.getBody().get("access_token");

        // 사용자 정보 요청
        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                providerInfo.getUserInfoUri(),
                HttpMethod.GET,
                new HttpEntity<>(new HttpHeaders() {{
                    set("Authorization", "Bearer " + accessToken);
                }}),
                Map.class
        );

        String email = (String) userInfoResponse.getBody().get("email");

        // 사용자 정보 저장 (신규 회원가입)
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> userRepository.save(User.builder()
                        .email(email)
                        .password("SOCIAL_LOGIN")
                        .build()));

        // JWT 발급
        String token = jwtTokenProvider.generateToken(email);
        return new LoginResponse(provider + " 로그인 성공", token);
    }

    // OAuth 로그인 성공 처리
    public LoginResponse handleOAuthSuccess(Authentication authentication) {
        OAuth2User oauth2User = (OAuth2User) authentication.getPrincipal();
        String email = oauth2User.getAttribute("email");

        String token = jwtTokenProvider.generateToken(email);
        return new LoginResponse("소셜 로그인 성공", token);
    }
}

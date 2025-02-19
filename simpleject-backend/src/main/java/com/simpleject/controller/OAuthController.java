package com.simpleject.controller;

import com.simpleject.dto.response.LoginResponse;
import com.simpleject.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/oauth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oAuthService;

    // ✅ 소셜 로그인 URL 반환 (Google, Kakao, GitHub 등 확장 가능)
    @GetMapping("/{provider}")
    public ResponseEntity<String> socialLogin(@PathVariable String provider) {
        String loginUrl = oAuthService.getOAuthLoginUrl(provider);
        return ResponseEntity.ok(loginUrl);
    }

    // ✅ OAuth2 Callback (Google, Kakao, GitHub 등)
    @GetMapping("/{provider}/callback")
    public ResponseEntity<LoginResponse> socialCallback(
            @PathVariable String provider,
            @RequestParam("code") String code) {
        LoginResponse response = oAuthService.handleOAuthCallback(provider, code);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/success")
    public ResponseEntity<LoginResponse> oauthSuccess(Authentication authentication) {
        return ResponseEntity.ok(oAuthService.handleOAuthSuccess(authentication));
    }

    @GetMapping("/failure")
    public ResponseEntity<String> oauthFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("소셜 로그인 실패");
    }
}

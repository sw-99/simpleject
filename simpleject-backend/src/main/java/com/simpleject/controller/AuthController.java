package com.simpleject.controller;

import com.simpleject.dto.request.LoginRequest;
import com.simpleject.dto.request.SignupRequest;
import com.simpleject.dto.response.LoginResponse;
import com.simpleject.dto.response.SignupResponse;
import com.simpleject.security.JwtTokenProvider;
import com.simpleject.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response) {
        try {
            // 사용자 인증
            LoginResponse loginResponse = authService.authenticateUser(request, response);

            // 성공적으로 로그인 처리된 경우
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            return ResponseEntity.status(400).body(new LoginResponse(400, "로그인 실패", ""));
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        jwtTokenProvider.deleteTokenFromCookie(response); // 쿠키에서 JWT 삭제
        return ResponseEntity.ok().build(); // 로그아웃 성공
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(HttpServletRequest request) {
        // 쿠키에서 토큰을 가져옴
        String token = jwtTokenProvider.resolveToken(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.ok("Valid token");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }

}

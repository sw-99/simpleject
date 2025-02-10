package com.simpleject.controller;

import com.simpleject.dto.request.SignupRequest;
import com.simpleject.dto.response.SignupResponse;
import com.simpleject.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@Valid @RequestBody SignupRequest request) {
        SignupResponse response = authService.registerUser(request);
        return ResponseEntity.ok(response);
    }
}

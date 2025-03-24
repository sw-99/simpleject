package com.simpleject.service;

import com.simpleject.security.JwtTokenProvider;
import com.simpleject.dto.request.LoginRequest;
import com.simpleject.dto.request.SignupRequest;
import com.simpleject.dto.response.LoginResponse;
import com.simpleject.dto.response.SignupResponse;
import com.simpleject.entity.User;
import com.simpleject.repository.jpa.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.simpleject.util.UuidGenerator.generate;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public SignupResponse registerUser(SignupRequest request) {
        // 중복 체크
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("이미 사용 중인 사용자 이름입니다.");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("이미 사용 중인 이메일입니다.");
        }

        // 비밀번호 해싱
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // User 생성 후 저장
        User user = User.builder()
                .publicId(generate())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(encodedPassword)
                .build();

        userRepository.save(user);

        return new SignupResponse("회원가입이 완료되었습니다.");
    }

    public LoginResponse authenticateUser(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("잘못된 사용자 이름 또는 비밀번호입니다."));

        // 비밀번호 검증
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("잘못된 사용자 이름 또는 비밀번호입니다.");
        }

        // JWT 토큰 발급
        String token = jwtTokenProvider.generateToken(user.getUsername());

        return new LoginResponse("로그인 성공", token);
    }

}

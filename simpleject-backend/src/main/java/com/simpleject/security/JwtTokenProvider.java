package com.simpleject.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long expirationTime;

    // @Value를 생성자에서 사용하여 초기화
    public JwtTokenProvider(@Value("${jwt.secret}") String secret,
                            @Value("${jwt.expiration}") long expirationTime) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret)); // ✅ Base64 디코딩 후 Key 생성
        this.expirationTime = expirationTime;
    }

    /** JWT 토큰 생성 */
    public String generateToken(String username) {
        Instant now = Instant.now();
        Instant expirationTimeInstant = now.plusMillis(expirationTime);

        return Jwts.builder()
                .subject(username)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTimeInstant))
                .signWith(secretKey, Jwts.SIG.HS256)
                .compact();
    }

    /** 토큰에서 사용자 이름(Username) 추출 */
    public String getUsernameFromToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /** 토큰 유효성 검증 */
    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            System.out.println("JWT 검증 오류: " + e.getMessage());
            return false;
        }
    }

    // 로그인 후 토큰을 쿠키에 저장하는 메서드
    public void setTokenInCookie(HttpServletResponse response, String token) throws IOException {
        // JWT 토큰을 HTTPOnly 쿠키로 설정
        Cookie cookie = new Cookie("access_token", token);
        cookie.setHttpOnly(true); // 클라이언트에서 접근 불가
        cookie.setSecure(false); // HTTPS 환경에서만 사용
        cookie.setPath("/"); // 모든 경로에서 사용 가능
        cookie.setMaxAge(60 * 60); // 쿠키 만료 시간 (60분)
        response.addCookie(cookie);
    }

    // 로그아웃 시 쿠키 삭제
    public void deleteTokenFromCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("access_token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // HTTPS 환경에서만 사용
        cookie.setPath("/"); // 모든 경로에서 사용 가능
        cookie.setMaxAge(0); // 만료 시간을 0으로 설정하여 쿠키 삭제
        response.addCookie(cookie);
    }

    /**
     * 클라이언트 요청에서 JWT 토큰을 추출하는 메서드
     * @param request HTTP 요청
     * @return JWT 토큰
     */
    public String resolveToken(HttpServletRequest request) {
        // Authorization 헤더에서 "Bearer <token>" 형식으로 토큰을 추출
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            // "Bearer "를 제거하고 토큰만 반환
            return bearerToken.substring(7);
        }
        // 만약 Authorization 헤더에 토큰이 없으면 쿠키에서 확인할 수 있습니다.
        // 예를 들어, 쿠키에서 "access_token" 이름으로 JWT 토큰을 찾는 방법
        String cookieToken = getCookieValue(request);
        if (cookieToken != null) {
            return cookieToken;
        }

        return null;
    }

    /**
     * 쿠키에서 값 추출 (예시)
     *
     * @param request HTTP 요청
     * @return 쿠키 값
     */
    private String getCookieValue(HttpServletRequest request) {
        // 요청에서 모든 쿠키를 가져옴
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("access_token")) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

}

package com.simpleject.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final UserDetails principal;
    private final String token;

    // ✅ 인증되지 않은 상태의 JwtAuthenticationToken (토큰만 존재)
    public JwtAuthenticationToken(String token) {
        super(null);
        this.token = token;
        this.principal = null;
        setAuthenticated(false);
    }

    // ✅ 인증된 JwtAuthenticationToken (사용자 정보 포함)
    public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.token = token;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token; // JWT 토큰이 credentials 역할을 함
    }

    @Override
    public Object getPrincipal() {
        return principal; // 사용자 정보 (UserDetails)
    }
}

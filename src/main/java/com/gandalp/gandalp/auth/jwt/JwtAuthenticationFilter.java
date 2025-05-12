package com.gandalp.gandalp.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. Authorization 헤더에서 Bearer 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        Optional<String> optionalToken = jwtTokenProvider.resolveToken(bearerToken);

        // 2. 없으면 → Cookie에서 accessToken 추출
        if (optionalToken.isEmpty()) {
            optionalToken = resolveTokenFromCookie(request);
        }

        // 3. 유효한 토큰이면 인증 객체 생성
        if (optionalToken.isPresent()) {
            String token = optionalToken.get();
            if (isUsableAccessToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 4. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    private Optional<String> resolveTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        for (Cookie cookie : request.getCookies()) {
            if ("accessToken".equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }

        return Optional.empty();
    }


    private boolean isUsableAccessToken(String token) {
        return token != null
                && jwtTokenProvider.validateToken(token)
                && !jwtTokenProvider.isBlacklisted(token)
                && jwtTokenProvider.getType(token) != null; // "type" 클레임 존재 여부
    }
}

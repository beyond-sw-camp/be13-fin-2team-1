package com.gandalp.gandalp.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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

        // 1. HttpServletRequest에서 Bearer 토큰 추출
        String bearerToken = request.getHeader("Authorization");
        Optional<String> optionalToken = jwtTokenProvider.resolveToken(bearerToken);

        // 2. 토큰이 존재하고 && 유효하고 && 블랙리스트에 없으면
        if (optionalToken.isPresent()) {
            String token = optionalToken.get();

            if (isUsableAccessToken(token)) {
                // 3. Authentication 객체를 생성 후 SecurityContext에 저장
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // 4. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    private boolean isUsableAccessToken(String token) {
        return token != null
                && jwtTokenProvider.validateToken(token)
                && !jwtTokenProvider.isBlacklisted(token)
                && jwtTokenProvider.getType(token) != null; // "type" 클레임 존재 여부
    }
}

package com.gandalp.gandalp.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final UserDetailsService userDetailsService;
    private final RedisTemplate<String, String> redisTemplate;

    private static final long ACCESS_TOKEN_EXP = 1000L * 60L * 15L; // 15분
    private static final long REFRESH_TOKEN_EXP = 1000L * 60L * 60L * 24L; // 1일


    public JwtTokenProvider(
            @Value("${springboot.jwt.secret}") String secret,
            UserDetailsService userDetailsService,
            RedisTemplate<String, String> redisTemplate) {

        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
        this.userDetailsService = userDetailsService;
        this.redisTemplate = redisTemplate;
    }

    // 🔹 AccessToken 생성
    public String createAccessToken(String accountId, String type) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", accountId);
        claims.put("type", type); // ADMIN, HEAD_NURSE, PARAMEDIC

        return createToken(claims, ACCESS_TOKEN_EXP);
    }

    // 🔹 RefreshToken 생성
    public String createRefreshToken(String accountId) {
        Map<String, String> claims = Map.of("username", accountId);

        String refreshToken = createToken(claims, REFRESH_TOKEN_EXP);

        redisTemplate.opsForValue().set("refresh:" + accountId, refreshToken, REFRESH_TOKEN_EXP, TimeUnit.MILLISECONDS);

        return refreshToken;
    }

    // 🔹 내부 공통 Token 생성 메서드
    private String createToken(Map<String, String> claims, long tokenExp) {
        return Jwts.builder()
                .header().add("typ", "JWT").and()
                .claims(claims)
                .id(Long.toHexString(System.nanoTime()))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + tokenExp))
                .signWith(secretKey)
                .compact();
    }

    // 🔹 AccessToken 유효성 검증
    public boolean validateToken(String token) {

        return !getClaims(token).getExpiration().before(new Date());
    }

    // 🔹 토큰에서 username 꺼내기
    public String getUserName(String token) {
        return getClaims(token).get("username", String.class);
    }

    // 🔹 토큰에서 role 꺼내기
    public String getType(String token) {
        return getClaims(token).get("type", String.class);
    }

    // 🔹 Authentication 객체 생성 (ROLE 붙여줌)
    public Authentication getAuthentication(String token) {
        String username = getUserName(token);
        String type = getType(token); // ADMIN, HEAD_NURSE, PARAMEDIC

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + type) // Spring Security 권한 이름 규칙
        );

        return new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
    }

    // 🔹 토큰 Bearer 파싱
    public Optional<String> resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return Optional.of(bearerToken.substring(7));
        }
        return Optional.empty();
    }

    // 🔹 블랙리스트 추가 (로그아웃)
    public void addBlacklist(String accessToken) {
        redisTemplate.opsForValue().set("blacklist:" + getJti(accessToken), "true", ACCESS_TOKEN_EXP, TimeUnit.MILLISECONDS);
    }

    public boolean isBlacklisted(String token) {
        return redisTemplate.hasKey("blacklist:" + getJti(token));
    }

    // 🔹 RefreshToken 삭제
    public void deleteRefreshToken(String accessToken) {
        redisTemplate.delete("refresh:" + getUserName(accessToken));
    }

    // 🔹 RefreshToken 검증
    public boolean isValidRefreshToken(String refreshToken) {
        String storedRefreshToken = redisTemplate.opsForValue().get("refresh:" + getUserName(refreshToken));
        return storedRefreshToken != null && storedRefreshToken.equals(refreshToken);
    }

    // 🔹 Claims 꺼내기 (예외 처리 포함)
    private Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    // 🔹 JTI 꺼내기
    private String getJti(String token) {
        return getClaims(token).getId();
    }
}

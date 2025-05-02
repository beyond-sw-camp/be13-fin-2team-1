package com.gandalp.gandalp.auth;

import com.gandalp.gandalp.auth.model.dto.JoinRequestDto;
import com.gandalp.gandalp.auth.model.dto.LoginRequestDto;
import com.gandalp.gandalp.auth.model.dto.TokenResponseDto;
import com.gandalp.gandalp.auth.model.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "01 인증 API", description = "인증 API")
@Slf4j
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;


    @Operation(summary = "회원가입")
    @PostMapping("/join")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> join(@Valid @RequestBody JoinRequestDto dto) {

        authService.join(dto);
        Map<String, String> response = new HashMap<>();
        response.put("message", "회원가입 완료");
        return ResponseEntity.ok("회원가입");
    }

    /**
     * 로그인 API
     * - accountId, password 입력받아 JWT 토큰 발급
     */
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequestDto dto) {

        try {
            TokenResponseDto tokenResponse = authService.login(dto);
            return ResponseEntity.ok(createSuccessResponse("로그인 성공", tokenResponse));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse(e.getMessage(), dto.getAccountId()));
        }
    }

    /**
     * 로그아웃 API
     * - Access Token 을 블랙리스트 등록
     */
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String bearerToken) {
        authService.logout(bearerToken);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    /**
     * Refresh 토큰으로 Access Token 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<TokenResponseDto> refresh(@RequestHeader("Authorization") String bearerToken) {
        TokenResponseDto tokenResponse = authService.refresh(bearerToken);
        return ResponseEntity.ok(tokenResponse);
    }

    // ✅ 내부 응답 포맷 정리
    private Map<String, Object> createSuccessResponse(String message, TokenResponseDto tokenResponse) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("accessToken", tokenResponse.getAccessToken());
        response.put("refreshToken", tokenResponse.getRefreshToken());
        return response;
    }

    private Map<String, Object> createErrorResponse(String message, String accountId) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("accountId", accountId);
        return response;
    }
}

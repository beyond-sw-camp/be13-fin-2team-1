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
    public ResponseEntity<?> join(@Valid @RequestBody JoinRequestDto dto) {
        Map<String, String> response = new HashMap<>();
        try{
            authService.join(dto);
            response.put("message", "회원가입 완료");

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("회원가입");
    }

    /**
     * 로그인 API
     * - accountId, password 입력받아 JWT 토큰 발급
     */
    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {

        TokenResponseDto tokenResponse = null;

        try {
            tokenResponse = authService.login(dto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok(createSuccessResponse("로그인 성공", tokenResponse));
    }

    /**
     * 로그아웃 API
     * - Access Token 을 블랙리스트 등록
     */
    @Operation(summary = "로그아웃")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken) {

        try{
            authService.logout(bearerToken);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        return ResponseEntity.ok("로그아웃 성공");
    }

    /**
     * Refresh 토큰으로 Access Token 재발급
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String bearerToken) {
        TokenResponseDto tokenResponse = null;
        try{
            tokenResponse = authService.refresh(bearerToken);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

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

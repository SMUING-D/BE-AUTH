package dev.umc.auth.global.auth.controller;

import dev.umc.auth.global.auth.service.AuthService;
import dev.umc.auth.global.auth.dto.AuthRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    /**
     * 로그인 기능 메서드
     * @param loginDto
     * @return AuthResponse.TokenDto
     */
    @PostMapping("/api/v0/auth/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest.LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    /**
     * 토큰 재발급 메서드
     * @param reissueDto
     * @return AuthResponse.TokenDto
     */
    @PostMapping("/api/v0/auth/reissue")
    public ResponseEntity<?> reissue(@RequestBody AuthRequest.ReissueDto reissueDto) {
        return ResponseEntity.ok(authService.reissue(reissueDto));
    }

    /**
     * 로그아웃 기능 메서드
     * @param requestAccessTokenDto
     * @return null
     */
    @PostMapping("/api/v0/auth/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") AuthRequest.AccessTokenDto requestAccessTokenDto) {
        authService.logout(requestAccessTokenDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    /**
     * 회원 탈퇴 기능 메서드
     * @param requestAccessTokenDto
     * @return null
     */
    @PostMapping("/api/v0/auth/withdraw")
    public ResponseEntity<?> withdraw(@RequestHeader("Authorization") AuthRequest.AccessTokenDto requestAccessTokenDto) {
        authService.withdraw(requestAccessTokenDto);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}

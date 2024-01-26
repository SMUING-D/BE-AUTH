package dev.umc.auth.global.auth;

import dev.umc.auth.domain.user.dto.UserRequest;
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

    @PostMapping("/api/v1/join")  // 회원가입
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDto joinDto) {
        return ResponseEntity.ok(authService.join(joinDto));
    }

    @PostMapping("/api/v1/login")  // 로그인
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @PostMapping("/api/v1/validate")  // Access Token 검증
    public ResponseEntity<?> validate(@RequestHeader("Authorization") String requestAccessToken) {
        if (!authService.validate(requestAccessToken)) {
            return ResponseEntity.status(HttpStatus.OK).build();
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping("/api/v1/reissue")  //
    public ResponseEntity<?> reissue(@RequestHeader("Authorization") String requestAccessToken, String requestRefreshToken) {
        return ResponseEntity.ok(authService.reissue(requestAccessToken, requestRefreshToken));
    }
}

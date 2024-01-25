package dev.umc.auth.global.auth;

import dev.umc.auth.domain.user.dto.UserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/api/v1/join")  // Signup Controller
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDto joinDto) {
        return ResponseEntity.ok(authService.join(joinDto));
    }

    @PostMapping("/api/v1/login")  // Login Controller
    public ResponseEntity<?> login(@RequestBody UserRequest.LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }
}

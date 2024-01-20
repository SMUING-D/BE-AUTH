package dev.umc.auth.global.auth;

import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final PrincipalDetailsService principalDetailsService;

    @PostMapping("/api/v1/register")
    public ResponseEntity<?> register(@RequestBody UserRequest.UserJoin userJoin) {
        return ResponseEntity.ok(principalDetailsService.join(userJoin));
    }
}

package dev.umc.auth.domain.user.controller;

import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v0/auth/join")
    public ResponseEntity<?> join(@RequestBody UserRequest.JoinDto joinDto) {
        return ResponseEntity.ok(userService.join(joinDto));
    }
}

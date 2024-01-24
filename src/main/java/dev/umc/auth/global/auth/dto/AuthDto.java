package dev.umc.auth.global.auth.dto;

import lombok.*;

public class AuthDto {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDto {
        private String accessToken;
        private String refreshToken;
    }
}

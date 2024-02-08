package dev.umc.auth.global.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class AuthResponse {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TokenDto {
        private String access_token;
        private String refresh_token;
    }
}

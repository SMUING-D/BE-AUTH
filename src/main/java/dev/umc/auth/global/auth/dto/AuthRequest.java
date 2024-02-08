package dev.umc.auth.global.auth.dto;

import lombok.*;

public class AuthRequest {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginDto {
        private Long student_id;
        private String password;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReissueDto {
        private String access_token;
        private String refresh_token;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccessTokenDto {
        private String access_token;
    }

    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RefreshTokenDto {
        private String refresh_token;
    }
}

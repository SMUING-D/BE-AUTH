package dev.umc.auth.domain.user.dto;

import lombok.Builder;
import lombok.Data;

public class UserResponse {

    @Data @Builder
    public static class UserJoin {
        private String username;
    }
}

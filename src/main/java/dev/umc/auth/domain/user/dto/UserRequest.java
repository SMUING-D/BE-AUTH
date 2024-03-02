package dev.umc.auth.domain.user.dto;

import dev.umc.auth.domain.user.enums.Grade;
import dev.umc.auth.domain.user.enums.UndergraduateGraduate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserRequest {
    @Data @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JoinDto {
        private String username;
        private Long student_id;
        private String password;
        private String major;
        private String sub_major;
        private String email;
        private String nickname;
        private String desired_employment;
        private String skill;
        private Grade grade;
        private String college;
        private UndergraduateGraduate graduation;
        private String profile_img;
    }
}

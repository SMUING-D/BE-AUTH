package dev.umc.auth.domain.user.converter;

import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.entity.User;
import dev.umc.auth.global.config.SecurityConfig;

public class UserConverter {
    public static User toUser(UserRequest.JoinDto joinDto) {
        return User.builder()
                .username(joinDto.getUsername())
                .studentId(joinDto.getStudent_id())
                .password(SecurityConfig.passwordEncoder().encode(joinDto.getPassword()))
                .role("USER")
                .major(joinDto.getMajor())
                .email(joinDto.getEmail())
                .nickname(joinDto.getNickname())
                .desiredEmployment(joinDto.getDesired_employment())
                .skill(joinDto.getSkill())
                .grade(joinDto.getGrade())
                .college(joinDto.getCollege())
                .undergraduateGraduate(joinDto.getUndergraduate_graduate())
                .profileImg(joinDto.getProfile_img())
                .build();
    }
}

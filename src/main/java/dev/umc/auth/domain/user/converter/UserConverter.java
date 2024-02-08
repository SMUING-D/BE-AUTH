package dev.umc.auth.domain.user.converter;

import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.entity.User;
import dev.umc.auth.global.config.SecurityConfig;

public class UserConverter {
    public static User toUser(UserRequest.JoinDto joinDto) {
        return User.builder()
                .username(joinDto.getUsername())
                .password(SecurityConfig.passwordEncoder().encode(joinDto.getPassword()))
                .role("USER")
                .studentId(joinDto.getStudent_id())
                .major(joinDto.getMajor())
                .subMajor(joinDto.getSub_major())
                .email(joinDto.getEmail())
                .nickname(joinDto.getNickname())
                .desiredEmployment(joinDto.getDesired_employment())
                .skill(joinDto.getSkill())
                .grade(joinDto.getGrade())
                .college(joinDto.getCollege())
                .graduation(joinDto.getGraduation())
                .profileImg(joinDto.getProfile_img())
                .build();
    }
}

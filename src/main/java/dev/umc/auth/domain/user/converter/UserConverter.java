package dev.umc.auth.domain.user.converter;

import dev.umc.auth.domain.user.domain.UserEntity;
import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.dto.UserResponse;

import static dev.umc.auth.global.config.SecurityConfig.passwordEncoder;

public class UserConverter {

    public static UserEntity toUserEntity(UserRequest.UserJoin userJoin) {
        return UserEntity.builder()
                .username(userJoin.getUsername())
                .password(passwordEncoder().encode(userJoin.getPassword()))
                .role(userJoin.getRole())
                .build();
    }

    public static UserResponse.UserJoin toUserDto(UserEntity user) {
        return UserResponse.UserJoin.builder()
                .username(user.getUsername())
                .build();
    }
}

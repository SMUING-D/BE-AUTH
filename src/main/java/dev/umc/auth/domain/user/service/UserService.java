package dev.umc.auth.domain.user.service;

import dev.umc.auth.domain.user.converter.UserConverter;
import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.dto.UserResponse;
import dev.umc.auth.domain.user.entity.User;
import dev.umc.auth.domain.user.entity.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse.JoinSuccessDto join(UserRequest.JoinDto joinDto) {
        User user = UserConverter.toUser(joinDto);
        userRepository.save(user);
        return new UserResponse.JoinSuccessDto(user.getId());
    }
}

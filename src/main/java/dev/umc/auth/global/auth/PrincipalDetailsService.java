package dev.umc.auth.global.auth;

import dev.umc.auth.domain.user.converter.UserConverter;
import dev.umc.auth.domain.user.domain.UserEntity;
import dev.umc.auth.domain.user.domain.UserRepository;
import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with the name " + username));
        return new PrincipalDetails(userEntity);
    }

    public UserResponse.UserJoin join(UserRequest.UserJoin userJoin) {
        UserEntity newUser = UserConverter.toUserEntity(userJoin);
        return UserConverter.toUserDto(userRepository.save(newUser));
    }
}

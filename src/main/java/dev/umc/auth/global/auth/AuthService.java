package dev.umc.auth.global.auth;

import dev.umc.auth.domain.user.converter.UserConverter;
import dev.umc.auth.domain.user.domain.UserEntity;
import dev.umc.auth.domain.user.domain.UserRepository;
import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.domain.user.dto.UserResponse;
import dev.umc.auth.global.auth.dto.AuthResponse;
import dev.umc.auth.global.auth.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final PrincipalDetailsService principalDetailsService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    private final static String TOKEN_PREFIX = "Bearer ";

    @Transactional
    public UserResponse.JoinDto join(UserRequest.JoinDto userJoin) {
        UserEntity newUser = UserConverter.toUserEntity(userJoin);
        return UserConverter.toUserDto(userRepository.save(newUser));
    }

    @Transactional
    public AuthResponse.TokenDto login(@RequestBody UserRequest.LoginDto loginDto) {
        try {
            Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authenticate);
        } catch (DisabledException exception) {
            throw new DisabledException("DISABLED_EXCEPTION", exception);
        } catch (LockedException exception) {
            throw new LockedException("LOCKED_EXCEPTION", exception);
        } catch (BadCredentialsException exception) {
            throw new BadCredentialsException("BAD_CREDENTIALS_EXCEPTION", exception);
        }

        UserDetails userDetails = principalDetailsService.loadUserByUsername(loginDto.getUsername());
        return tokenProvider.createToken(userDetails);
    }

    public boolean validate(String requestAccessToken) {
        String bearerToken = resolveToken(requestAccessToken);
        return tokenProvider.isAccessTokenExpired(bearerToken);
    }

    public AuthResponse.TokenDto reissue(String requestAccessToken, String requestRefreshToken) {
        String accessToken = resolveToken(requestAccessToken);
        Authentication authentication = tokenProvider.getAuthentication(accessToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = principalDetailsService.loadUserByUsername(authentication.getName());
        return tokenProvider.createToken(userDetails);
    }

    private String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

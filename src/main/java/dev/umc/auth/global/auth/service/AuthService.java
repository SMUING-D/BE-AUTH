package dev.umc.auth.global.auth.service;

import dev.umc.auth.domain.token.entity.Token;
import dev.umc.auth.domain.token.entity.TokenRepository;
import dev.umc.auth.domain.user.entity.User;
import dev.umc.auth.domain.user.entity.UserRepository;
import dev.umc.auth.global.auth.dto.AuthRequest;
import dev.umc.auth.global.auth.dto.AuthResponse;
import dev.umc.auth.global.jwt.JwtTokenProvider;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class AuthService {

    // private final PrincipalDetailsService principalDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    private final static String TOKEN_PREFIX = "Bearer ";

    @Transactional
    public AuthResponse.TokenDto login(@RequestBody AuthRequest.LoginDto loginDto) {
        Authentication authentication;
        User user = userRepository.findByStudentId(loginDto.getStudent_id())
                .orElseThrow(() -> new EntityNotFoundException("Cannot found the student id : " + loginDto.getStudent_id()));

        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), loginDto.getPassword()));  // 무조건 username 고정?
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException exception) {
            throw new DisabledException("DISABLED_EXCEPTION", exception);
        } catch (LockedException exception) {
            throw new LockedException("LOCKED_EXCEPTION", exception);
        } catch (BadCredentialsException exception) {
            throw new BadCredentialsException("BAD_CREDENTIALS_EXCEPTION", exception);
        }

        // TODO: Need refactoring
        // UserDetails userDetails = principalDetailsService.loadUserByUsername(user.getUsername());
        AuthResponse.TokenDto tokenDto = generateToken(user.getStudentId(), getAuthorities(authentication));
        return tokenDto;
    }

    @Transactional
    public AuthResponse.TokenDto reissue(AuthRequest.ReissueDto reissueDto) {
        String requestAccessToken = reissueDto.getAccess_token();
        String requestRefreshToken = reissueDto.getRefresh_token();

        System.out.println("requestAccessToken = " + requestAccessToken);
        System.out.println("requestRefreshToken = " + requestRefreshToken);

        Token tokens = tokenRepository.findByAccessToken(requestAccessToken)
                .orElseThrow(() -> new EntityNotFoundException());

        System.out.println("tokens.getAccessToken() = " + tokens.getAccessToken());
        System.out.println("tokens.getRefreshToken() = " + tokens.getRefreshToken());

        // 다른 사용자의 access token 탈취한 경우
        if (tokens.getRefreshToken() == null) {
            deleteTokensInRedis(requestAccessToken);  // 탈취 위험 사용자 로그아웃 처리
            return new AuthResponse.TokenDto(null, null);  // 반환형 생각해보자!
        }

        if (!tokens.getRefreshToken().equals(requestRefreshToken)) {
            deleteTokensInRedis(requestAccessToken);  // 탈취 위험 사용자 로그아웃 처리
            return new AuthResponse.TokenDto(null, null);  // 반환형 생각해보자!
        }

        deleteTokensInRedis(requestAccessToken);  // 토큰 재발급 위해 Redis 내부에 저장된 기존 정보 삭제 후 추가
        Authentication authentication = jwtTokenProvider.getAuthentication(requestAccessToken);
        User user = userRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException());

        AuthResponse.TokenDto tokenDto = generateToken(user.getStudentId(), getAuthorities(authentication));
        return tokenDto;
    }

    @Transactional
    public void logout(AuthRequest.AccessTokenDto requestAccessTokenDto) {
        String requestAccessToken = resolveToken(requestAccessTokenDto.getAccess_token());
        deleteTokensInRedis(requestAccessToken);
        SecurityContextHolder.clearContext();
    }

    @Transactional
    public void withdraw(AuthRequest.AccessTokenDto requestAccessTokenDto) {
        logout(requestAccessTokenDto);

        String requestAccessToken = resolveToken(requestAccessTokenDto.getAccess_token());
        String sid = jwtTokenProvider.getClaims(requestAccessToken).get("student_id").toString();// 여기 토큰 만료 처리는 어떻게?
        Long studentId = Long.parseLong(sid);
        User user = userRepository.findByStudentId(studentId)
                .orElseThrow(() -> new EntityNotFoundException());
        userRepository.delete(user);
    }

    private AuthResponse.TokenDto generateToken(Long studentId, String authorities) {
        AuthResponse.TokenDto tokenDto = jwtTokenProvider.createToken(studentId, authorities);
        saveTokensInRedis(tokenDto.getAccess_token(), tokenDto.getRefresh_token());
        return tokenDto;
    }

    private String getAuthorities(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private void saveTokensInRedis(String accessToken, String refreshToken) {
        tokenRepository.save(Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build());
    }

    private void deleteTokensInRedis(String accessToken) {
        Token tokens = tokenRepository.findByAccessToken(accessToken)
                .orElseThrow(() -> new EntityNotFoundException());
        tokenRepository.delete(tokens);
    }

    private String resolveToken(String bearerToken) {
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}

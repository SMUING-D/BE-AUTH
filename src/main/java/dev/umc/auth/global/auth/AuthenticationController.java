package dev.umc.auth.global.auth;

import dev.umc.auth.domain.user.dto.UserRequest;
import dev.umc.auth.global.auth.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthenticationController {

    private final PrincipalDetailsService principalDetailsService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    @PostMapping("/api/v1/join")
    public ResponseEntity<?> join(@RequestBody UserRequest.UserJoin userJoin) {
        return ResponseEntity.ok(principalDetailsService.join(userJoin));
    }

    @PostMapping("/api/v1/authenticate")
    public ResponseEntity<?> createAuthenticateToken(@RequestBody UserRequest.UserAuthenticate userAuthenticate) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    userAuthenticate.getUsername(), userAuthenticate.getPassword()));
        } catch (DisabledException exception) {
            throw new DisabledException("DISABLED_EXCEPTION", exception);
        } catch (LockedException exception) {
            throw new LockedException("LOCKED_EXCEPTION", exception);
        } catch (BadCredentialsException exception) {
            throw new BadCredentialsException("BAD_CREDENTIALS_EXCEPTION", exception);
        }

        UserDetails userDetails = principalDetailsService.loadUserByUsername(userAuthenticate.getUsername());
        String accessToken = tokenProvider.createToken(userDetails);
        return ResponseEntity.ok(accessToken);
    }
}

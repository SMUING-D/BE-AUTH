package dev.umc.auth.global.auth.token;

import dev.umc.auth.global.auth.PrincipalDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;
    private final PrincipalDetailsService principalDetailsService;

    private static final String TOKEN_KEY = "username";

    @Transactional
    public String createToken(UserDetails userDetails) {
        Long now = System.currentTimeMillis();

        String accessToken = Jwts.builder()
                .setHeaderParam("alg", "HS512")
                .setHeaderParam("typ", "JWT")
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + (jwtProperties.getAccessTokenValidityInSeconds() * 1000)))
                .setSubject("access-token")
                .claim("username", userDetails.getUsername())
                .claim("role", userDetails.getAuthorities())
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS512)
                .compact();

        return accessToken;
    }

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey())
                    .build()
                    .parseClaimsJws(accessToken);
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            log.error("UnsupportedJwtException | MalformedJwtException | SignatureException");
            throw new JwtException("Validate Access Token Exception");
        } catch (ExpiredJwtException e) {
            log.error("ExpiredJwtException");
            throw new JwtException("Validate Access Token Exception");  // ExpiredJwtException 던지고 싶은데 고민
        } catch (IllegalArgumentException e) {
            log.error("IllegalArgumentException");
            throw new IllegalArgumentException("IllegalArgumentException");
        }
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Authentication getAuthentication(String token) {
        String username = getClaims(token).get(TOKEN_KEY).toString();
        UserDetails userDetails = principalDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}

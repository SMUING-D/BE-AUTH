package dev.umc.auth.global.jwt;

import dev.umc.auth.global.auth.dto.AuthResponse;
import dev.umc.auth.global.auth.service.PrincipalDetailsService;
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
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final PrincipalDetailsService principalDetailsService;

    private static final String TOKEN_KEY = "student_id";

    @Transactional
    public AuthResponse.TokenDto createToken(Long studentId, String authorities) {
        Long now = System.currentTimeMillis();
        String accessToken = createAccessToken(studentId, authorities, now);
        String refreshToken = createRefreshToken(now);
        return new AuthResponse.TokenDto(accessToken, refreshToken);
    }

    private String createAccessToken(Long studentId, String authorities, Long now) {
        return Jwts.builder()
                .setHeaderParam("alg", "HS512")
                .setHeaderParam("typ", "JWT")
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + (jwtProperties.getAccessTokenValidityInSeconds() * 1000)))
                .setSubject("access")
                .claim("student_id", studentId)
                .claim("role", authorities)
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private String createRefreshToken(Long now) {
        return Jwts.builder()
                .setHeaderParam("alg", "HS512")
                .setHeaderParam("typ", "JWT")
                .setIssuer(jwtProperties.getIssuer())
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + (jwtProperties.getRefreshTokenValidityInSeconds() * 1000)))
                .setSubject("refresh")
                .signWith(jwtProperties.getKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public Claims getClaims(String requestAccessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(jwtProperties.getKey())
                    .build()
                    .parseClaimsJws(requestAccessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
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

    public Authentication getAuthentication(String accessToken) {
        Long studentId = Long.parseLong(getClaims(accessToken).get(TOKEN_KEY).toString());
        UserDetails userDetails = principalDetailsService.loadUserByStudentId(studentId);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}

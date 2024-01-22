package dev.umc.auth.global.auth.token;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RequiredArgsConstructor
@Service
public class TokenProvider {

    private final JwtProperties jwtProperties;

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
}

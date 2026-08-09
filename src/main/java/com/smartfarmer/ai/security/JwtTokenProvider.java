package com.smartfarmer.ai.security;

import com.smartfarmer.ai.common.enums.TokenType;
import com.smartfarmer.ai.common.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {

    private static final int MINIMUM_SECRET_LENGTH = 32;

    private final SecretKey key;
    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        String secret = jwtProperties.secret();
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < MINIMUM_SECRET_LENGTH) {
            throw new IllegalStateException("app.jwt.secret must be configured with at least "
                    + MINIMUM_SECRET_LENGTH + " characters");
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String createAccessToken(String userId, Set<UserRole> roles) {
        Instant now = Instant.now();
        String rolesClaim = roles.stream().map(Enum::name).sorted().collect(Collectors.joining(","));
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userId)
                .claim("roles", rolesClaim)
                .claim("type", TokenType.ACCESS.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(accessTokenValidity())))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(userId)
                .claim("type", TokenType.REFRESH.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plus(refreshTokenValidity())))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public boolean isTokenOfType(String token, TokenType expected) {
        try {
            return expected == getTokenType(token);
        } catch (RuntimeException ex) {
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRolesFromToken(String token) {
        return parseClaims(token).get("roles", String.class);
    }

    public TokenType getTokenType(String token) {
        return TokenType.valueOf(parseClaims(token).get("type", String.class));
    }

    public Duration accessTokenValidity() {
        return Duration.ofSeconds(jwtProperties.accessExpiration());
    }

    public Duration refreshTokenValidity() {
        return Duration.ofSeconds(jwtProperties.refreshExpiration());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

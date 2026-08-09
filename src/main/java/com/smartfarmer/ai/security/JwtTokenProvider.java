package com.smartfarmer.ai.security;

import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.common.enums.TokenType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey key;
    private final JwtProperties jwtProperties;

    public Key getKey() {
        return this.key;
    }

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes());
    }

    public String createAccessToken(String userId, Set<UserRole> roles) {
        Instant now = Instant.now();
        String rolesStr = roles.stream().map(Enum::name).collect(Collectors.joining(","));
        return Jwts.builder()
                .subject(userId)
                .claim("roles", rolesStr)
                .claim("type", TokenType.ACCESS.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtProperties.accessExpiration())))
                .signWith(key)
                .compact();
    }

    public String createRefreshToken(String userId) {
        Instant now = Instant.now();
        return Jwts.builder()
                .subject(userId)
                .claim("type", TokenType.REFRESH.name())
                .issuedAt(Date.from(now))
                .expiration(Date.from(now.plusMillis(jwtProperties.refreshExpiration())))
                .signWith(key)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception ex) {
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

    public long getRefreshTokenValidityMs() {
        return jwtProperties.refreshExpiration();
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

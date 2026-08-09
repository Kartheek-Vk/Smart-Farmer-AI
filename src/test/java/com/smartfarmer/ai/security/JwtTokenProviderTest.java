package com.smartfarmer.ai.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.smartfarmer.ai.common.enums.TokenType;
import com.smartfarmer.ai.common.enums.UserRole;
import java.time.Duration;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private static final String SECRET = "test-secret-test-secret-test-secret-1234";

    private final JwtTokenProvider provider = new JwtTokenProvider(new JwtProperties(SECRET, 900, 2_592_000));

    @Test
    void rejectsSecretsShorterThanThirtyTwoBytes() {
        assertThatThrownBy(() -> new JwtTokenProvider(new JwtProperties("too-short", 900, 900)))
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void accessTokenCarriesSubjectRolesAndAccessType() {
        String userId = UUID.randomUUID().toString();

        String token = provider.createAccessToken(userId, Set.of(UserRole.FARMER, UserRole.ADMIN));

        assertThat(provider.validateToken(token)).isTrue();
        assertThat(provider.getUserIdFromToken(token)).isEqualTo(userId);
        assertThat(provider.getRolesFromToken(token)).isEqualTo("ADMIN,FARMER");
        assertThat(provider.getTokenType(token)).isEqualTo(TokenType.ACCESS);
        assertThat(provider.isTokenOfType(token, TokenType.REFRESH)).isFalse();
    }

    @Test
    void refreshTokenIsTypedAsRefresh() {
        String token = provider.createRefreshToken(UUID.randomUUID().toString());

        assertThat(provider.isTokenOfType(token, TokenType.REFRESH)).isTrue();
        assertThat(provider.isTokenOfType(token, TokenType.ACCESS)).isFalse();
    }

    @Test
    void expirationPropertiesAreInterpretedAsSeconds() {
        assertThat(provider.accessTokenValidity()).isEqualTo(Duration.ofMinutes(15));
        assertThat(provider.refreshTokenValidity()).isEqualTo(Duration.ofDays(30));
    }

    @Test
    void tokensSignedWithAnotherSecretAreRejected() {
        String foreign = new JwtTokenProvider(new JwtProperties("another-secret-another-secret-1234567", 900, 900))
                .createAccessToken(UUID.randomUUID().toString(), Set.of(UserRole.FARMER));

        assertThat(provider.validateToken(foreign)).isFalse();
    }
}

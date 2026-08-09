package com.smartfarmer.ai.authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.smartfarmer.ai.authentication.dto.LoginRequest;
import com.smartfarmer.ai.authentication.dto.OtpRequest;
import com.smartfarmer.ai.authentication.dto.RefreshTokenRequest;
import com.smartfarmer.ai.authentication.dto.RegisterRequest;
import com.smartfarmer.ai.authentication.dto.ResetPasswordRequest;
import com.smartfarmer.ai.common.enums.OtpPurpose;
import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.support.IntegrationTest;
import com.smartfarmer.ai.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;

class AuthFlowIntegrationTest extends IntegrationTest {

    private static final String PASSWORD = "Str0ngPassword!";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registersHashesThePasswordAndNeverReturnsIt() throws Exception {
        String email = uniqueEmail("register");

        JsonNode data = register(email, PASSWORD);

        assertThat(data.path("accessToken").asText()).isNotBlank();
        assertThat(data.path("refreshToken").asText()).isNotBlank();
        assertThat(data.toString()).doesNotContain(PASSWORD).doesNotContain("passwordHash");
        User stored = userRepository.findByEmail(email).orElseThrow();
        assertThat(stored.getPasswordHash()).isNotEqualTo(PASSWORD);
        assertThat(passwordEncoder.matches(PASSWORD, stored.getPasswordHash())).isTrue();
        assertThat(stored.getRolesAsEnumSet()).containsExactly(UserRole.FARMER);
    }

    @Test
    void rejectsInvalidRegistrationPayloadWithBadRequest() throws Exception {
        RegisterRequest invalid = new RegisterRequest("A", "", "not-an-email", "short", UserRole.FARMER);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void rejectsDuplicateEmailWithConflict() throws Exception {
        String email = uniqueEmail("duplicate");
        register(email, PASSWORD);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new RegisterRequest("Test", "User", email, PASSWORD, UserRole.FARMER))))
                .andExpect(status().isConflict());
    }

    @Test
    void refusesToSelfAssignTheAdminRole() throws Exception {
        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest(
                                "Test", "User", uniqueEmail("admin"), PASSWORD, UserRole.ADMIN))))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginReturnsTokensAndRejectsWrongPassword() throws Exception {
        String email = uniqueEmail("login");
        register(email, PASSWORD);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(email, PASSWORD))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.accessToken").isNotEmpty());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(email, "WrongPassword1"))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void meRequiresAnAccessTokenAndRejectsRefreshTokens() throws Exception {
        String email = uniqueEmail("me");
        JsonNode tokens = register(email, PASSWORD);

        mockMvc.perform(get("/api/v1/auth/me"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/auth/me")
                        .header(HttpHeaders.AUTHORIZATION, bearer(tokens.path("refreshToken").asText())))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/v1/auth/me")
                        .header(HttpHeaders.AUTHORIZATION, bearer(tokens.path("accessToken").asText())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.email").value(email));
    }

    @Test
    void refreshRotatesTheTokenAndInvalidatesThePreviousOne() throws Exception {
        JsonNode tokens = register(uniqueEmail("refresh"), PASSWORD);
        String firstRefresh = tokens.path("refreshToken").asText();

        String body = mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest(firstRefresh))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String secondRefresh = objectMapper.readTree(body).path("data").path("refreshToken").asText();
        assertThat(secondRefresh).isNotEqualTo(firstRefresh);

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest(firstRefresh))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void logoutRevokesTheRefreshToken() throws Exception {
        JsonNode tokens = register(uniqueEmail("logout"), PASSWORD);
        String refreshToken = tokens.path("refreshToken").asText();

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest(refreshToken))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RefreshTokenRequest(refreshToken))))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void forgotPasswordDoesNotRevealUnknownAccountsAndResetChangesThePassword() throws Exception {
        String email = uniqueEmail("reset");
        register(email, PASSWORD);

        String unknown = mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new OtpRequest(uniqueEmail("unknown"), OtpPurpose.RESET_PASSWORD))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        assertThat(objectMapper.readTree(unknown).path("data").path("code").isNull()).isTrue();

        String issued = mockMvc.perform(post("/api/v1/auth/forgot-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new OtpRequest(email, OtpPurpose.RESET_PASSWORD))))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String code = objectMapper.readTree(issued).path("data").path("code").asText();
        assertThat(code).isNotBlank();

        String newPassword = "Even5trongerPass!";
        mockMvc.perform(post("/api/v1/auth/reset-password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ResetPasswordRequest(email, code, newPassword))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(email, newPassword))))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(email, PASSWORD))))
                .andExpect(status().isUnauthorized());
    }
}

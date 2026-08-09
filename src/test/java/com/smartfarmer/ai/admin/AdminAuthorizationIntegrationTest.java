package com.smartfarmer.ai.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smartfarmer.ai.support.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

class AdminAuthorizationIntegrationTest extends IntegrationTest {

    private static final String PASSWORD = "Str0ngPassword!";

    @Test
    void adminEndpointsRejectAnonymousAndNonAdminCallers() throws Exception {
        mockMvc.perform(get("/api/v1/admin/users"))
                .andExpect(status().isUnauthorized());

        String farmerToken = registerAndGetAccessToken(uniqueEmail("farmer"), PASSWORD);
        mockMvc.perform(get("/api/v1/admin/users").header(HttpHeaders.AUTHORIZATION, bearer(farmerToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanListUsersAndAuditLogs() throws Exception {
        String email = uniqueEmail("admin");
        register(email, PASSWORD);
        promoteToAdmin(email);
        String adminToken = login(email, PASSWORD);

        mockMvc.perform(get("/api/v1/admin/users").header(HttpHeaders.AUTHORIZATION, bearer(adminToken)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/admin/audit-logs").header(HttpHeaders.AUTHORIZATION, bearer(adminToken)))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/v1/admin/stats").header(HttpHeaders.AUTHORIZATION, bearer(adminToken)))
                .andExpect(status().isOk());
    }
}

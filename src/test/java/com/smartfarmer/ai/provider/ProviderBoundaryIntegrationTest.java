package com.smartfarmer.ai.provider;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smartfarmer.ai.support.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

/**
 * The backend must never answer with invented provider data: with no weather provider and no AI
 * service configured the corresponding endpoints report the unavailable state instead.
 */
class ProviderBoundaryIntegrationTest extends IntegrationTest {

    @Test
    void weatherEndpointsReportUnavailableWhenNoProviderIsConfigured() throws Exception {
        String token = registerAndGetAccessToken(uniqueEmail("weather"), "Str0ngPassword!");

        mockMvc.perform(get("/api/v1/weather/current")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .param("location", "Nashik")
                        .param("latitude", "19.99")
                        .param("longitude", "73.78"))
                .andExpect(status().isServiceUnavailable())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void healthEndpointIsPublic() throws Exception {
        mockMvc.perform(get("/api/v1/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("UP"));
    }

    @Test
    void openApiDocumentIsPublished() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.paths['/api/v1/farms']").exists());
    }
}

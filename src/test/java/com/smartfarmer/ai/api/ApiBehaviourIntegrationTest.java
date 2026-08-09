package com.smartfarmer.ai.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smartfarmer.ai.common.enums.UserStatus;
import com.smartfarmer.ai.support.IntegrationTest;
import com.smartfarmer.ai.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

class ApiBehaviourIntegrationTest extends IntegrationTest {

    private static final String PASSWORD = "Str0ngPassword!";

    private static final String FARM_PAYLOAD = """
            {"name":"Test Farm","location":"Nashik","latitude":19.99,"longitude":73.78,
             "area":5,"areaUnit":"ACRE","soilType":"%s","irrigationType":"DRIP","ownershipType":"OWNED"}
            """;

    @Test
    void frameworkLevelErrorsAreMappedToClientStatusesInsteadOfServerErrors() throws Exception {
        String token = registerAndGetAccessToken(uniqueEmail("errors"), PASSWORD);

        mockMvc.perform(post("/api/v1/farms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FARM_PAYLOAD.formatted("BANANA")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.errors[0]").value(org.hamcrest.Matchers.startsWith("soilType: must be one of")));

        mockMvc.perform(post("/api/v1/farms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/farms/not-a-uuid").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/weather/current")
                        .param("location", "Nashik")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/does-not-exist").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isNotFound());

        mockMvc.perform(delete("/api/v1/health").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isMethodNotAllowed());

        mockMvc.perform(get("/api/v1/farms")
                        .param("sort", "nonexistentField,asc")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors[0]")
                        .value(org.hamcrest.Matchers.containsString("unknown sort or filter property")));
    }

    @Test
    void deactivatingAUserImmediatelyInvalidatesTheirAccessToken() throws Exception {
        String email = uniqueEmail("deactivated");
        String token = registerAndGetAccessToken(email, PASSWORD);

        mockMvc.perform(get("/api/v1/farms").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk());

        transactionTemplate.executeWithoutResult(status -> {
            User user = userRepository.findByEmail(email).orElseThrow();
            user.setStatus(UserStatus.INACTIVE);
            userRepository.save(user);
        });

        mockMvc.perform(get("/api/v1/farms").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void reportsCanBeGeneratedListedAndFetched() throws Exception {
        String token = registerAndGetAccessToken(uniqueEmail("reports"), PASSWORD);

        MvcResult created = mockMvc.perform(post("/api/v1/reports")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"reportType\":\"FARM_SUMMARY\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.status").value("COMPLETED"))
                .andExpect(jsonPath("$.data.metadataJson").value(org.hamcrest.Matchers.containsString("\"farms\"")))
                .andReturn();

        String reportId = objectMapper.readTree(created.getResponse().getContentAsString())
                .path("data").path("id").asText();

        mockMvc.perform(get("/api/v1/reports").header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));

        mockMvc.perform(get("/api/v1/reports/" + reportId).header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.reportType").value("FARM_SUMMARY"));

        String otherToken = registerAndGetAccessToken(uniqueEmail("reports-other"), PASSWORD);
        mockMvc.perform(get("/api/v1/reports/" + reportId).header(HttpHeaders.AUTHORIZATION, bearer(otherToken)))
                .andExpect(status().isForbidden());
    }

    @Test
    void resourceCreationRespondsWithCreated() throws Exception {
        String token = registerAndGetAccessToken(uniqueEmail("created"), PASSWORD);

        mockMvc.perform(post("/api/v1/farms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(FARM_PAYLOAD.formatted("LOAM")))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/v1/assistant/conversations")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Planning\"}"))
                .andExpect(status().isCreated());
    }
}

package com.smartfarmer.ai.farm;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.smartfarmer.ai.common.enums.AreaUnit;
import com.smartfarmer.ai.common.enums.IrrigationType;
import com.smartfarmer.ai.common.enums.OwnershipType;
import com.smartfarmer.ai.common.enums.SoilType;
import com.smartfarmer.ai.farm.dto.CreateFarmRequest;
import com.smartfarmer.ai.farm.dto.UpdateFarmRequest;
import com.smartfarmer.ai.support.IntegrationTest;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

class FarmOwnershipIntegrationTest extends IntegrationTest {

    private static final String PASSWORD = "Str0ngPassword!";

    @Test
    void ownerCanManageTheFarmWhileOtherUsersAreForbidden() throws Exception {
        String ownerToken = registerAndGetAccessToken(uniqueEmail("owner"), PASSWORD);
        String intruderToken = registerAndGetAccessToken(uniqueEmail("intruder"), PASSWORD);

        String created = mockMvc.perform(post("/api/v1/farms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(ownerToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest())))
                .andExpect(status().is2xxSuccessful())
                .andReturn().getResponse().getContentAsString();
        String farmId = objectMapper.readTree(created).path("data").path("id").asText();

        mockMvc.perform(get("/api/v1/farms/" + farmId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(ownerToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.name").value("Green Acres"));

        mockMvc.perform(get("/api/v1/farms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(ownerToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(1));

        mockMvc.perform(get("/api/v1/farms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(intruderToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.totalElements").value(0));

        mockMvc.perform(get("/api/v1/farms/" + farmId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(intruderToken)))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/v1/farms/" + farmId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(intruderToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest())))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/v1/farms/" + farmId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(intruderToken)))
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/v1/farms/" + farmId)
                        .header(HttpHeaders.AUTHORIZATION, bearer(ownerToken)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void unknownFarmReturnsNotFoundAndInvalidPayloadReturnsBadRequest() throws Exception {
        String token = registerAndGetAccessToken(uniqueEmail("farmer"), PASSWORD);

        mockMvc.perform(get("/api/v1/farms/" + UUID.randomUUID())
                        .header(HttpHeaders.AUTHORIZATION, bearer(token)))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/api/v1/farms")
                        .header(HttpHeaders.AUTHORIZATION, bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void farmsRequireAuthentication() throws Exception {
        mockMvc.perform(get("/api/v1/farms"))
                .andExpect(status().isUnauthorized());
    }

    private CreateFarmRequest createRequest() {
        return new CreateFarmRequest("Green Acres", "Nashik", new BigDecimal("19.9975"), new BigDecimal("73.7898"),
                new BigDecimal("12.50"), AreaUnit.ACRE, SoilType.LOAM, IrrigationType.DRIP, OwnershipType.OWNED);
    }

    private UpdateFarmRequest updateRequest() {
        return new UpdateFarmRequest("Hacked Acres", "Nashik", new BigDecimal("19.9975"), new BigDecimal("73.7898"),
                new BigDecimal("12.50"), AreaUnit.ACRE, SoilType.LOAM, IrrigationType.DRIP, OwnershipType.OWNED);
    }
}

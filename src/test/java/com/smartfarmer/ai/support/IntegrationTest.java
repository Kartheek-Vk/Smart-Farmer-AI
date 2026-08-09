package com.smartfarmer.ai.support;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartfarmer.ai.TestcontainersConfiguration;
import com.smartfarmer.ai.authentication.dto.RegisterRequest;
import com.smartfarmer.ai.common.enums.UserRole;
import com.smartfarmer.ai.user.entity.Role;
import com.smartfarmer.ai.user.entity.User;
import com.smartfarmer.ai.user.repository.RoleRepository;
import com.smartfarmer.ai.user.repository.UserRepository;
import java.util.UUID;
import com.smartfarmer.ai.authentication.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(TestcontainersConfiguration.class)
public abstract class IntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;

    @Autowired
    protected TransactionTemplate transactionTemplate;

    protected String uniqueEmail(String prefix) {
        return prefix + "-" + UUID.randomUUID() + "@example.com";
    }

    protected JsonNode register(String email, String password) throws Exception {
        RegisterRequest request = new RegisterRequest("Test", "User", email, password, UserRole.FARMER);
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString()).path("data");
    }

    protected String registerAndGetAccessToken(String email, String password) throws Exception {
        return register(email, password).path("accessToken").asText();
    }

    protected String login(String email, String password) throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new LoginRequest(email, password))))
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
                .path("data").path("accessToken").asText();
    }

    protected String bearer(String token) {
        return "Bearer " + token;
    }

    protected void promoteToAdmin(String email) {
        transactionTemplate.executeWithoutResult(status -> {
            User user = userRepository.findByEmail(email).orElseThrow();
            Role adminRole = roleRepository.findByName(UserRole.ADMIN).orElseThrow();
            user.getRoles().add(adminRole);
            userRepository.save(user);
        });
    }
}

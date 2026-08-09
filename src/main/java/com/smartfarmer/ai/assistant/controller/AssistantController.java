package com.smartfarmer.ai.assistant.controller;

import com.smartfarmer.ai.assistant.dto.ConversationDetailResponse;
import com.smartfarmer.ai.assistant.dto.ConversationResponse;
import com.smartfarmer.ai.assistant.dto.CreateConversationRequest;
import com.smartfarmer.ai.assistant.dto.MessageResponse;
import com.smartfarmer.ai.assistant.dto.SendMessageRequest;
import com.smartfarmer.ai.assistant.service.AssistantService;
import com.smartfarmer.ai.common.api.ApiResponse;
import com.smartfarmer.ai.common.api.ApiResponseFactory;
import com.smartfarmer.ai.common.api.PageResponse;
import com.smartfarmer.ai.security.CurrentUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/assistant")
public class AssistantController {

    private final AssistantService assistantService;
    private final CurrentUserService currentUserService;
    private final ApiResponseFactory apiResponseFactory;

    public AssistantController(AssistantService assistantService,
                               CurrentUserService currentUserService,
                               ApiResponseFactory apiResponseFactory) {
        this.assistantService = assistantService;
        this.currentUserService = currentUserService;
        this.apiResponseFactory = apiResponseFactory;
    }

    @PostMapping("/conversations")
    public ApiResponse<ConversationResponse> createConversation(
            @Valid @RequestBody CreateConversationRequest request,
            HttpServletRequest httpServletRequest) {
        ConversationResponse response = assistantService.createConversation(currentUserService.currentUser(), request);
        return apiResponseFactory.success("Conversation created successfully", response, httpServletRequest);
    }

    @GetMapping("/conversations")
    public ApiResponse<PageResponse<ConversationResponse>> listConversations(
            Pageable pageable,
            HttpServletRequest request) {
        Page<ConversationResponse> page = assistantService.listConversations(currentUserService.currentUser(), pageable);
        return apiResponseFactory.page("Conversations retrieved successfully", page, request);
    }

    @GetMapping("/conversations/{id}")
    public ApiResponse<ConversationDetailResponse> getConversation(
            @PathVariable UUID id,
            HttpServletRequest request) {
        ConversationDetailResponse response = assistantService.getConversation(id, currentUserService.currentUser());
        return apiResponseFactory.success("Conversation retrieved successfully", response, request);
    }

    @PostMapping("/conversations/{id}/messages")
    public ApiResponse<MessageResponse> sendMessage(
            @PathVariable UUID id,
            @Valid @RequestBody SendMessageRequest requestBody,
            HttpServletRequest request) {
        MessageResponse response = assistantService.sendMessage(id, currentUserService.currentUser(), requestBody);
        return apiResponseFactory.success("Message sent successfully", response, request);
    }

    @DeleteMapping("/conversations/{id}")
    public ApiResponse<Void> deleteConversation(
            @PathVariable UUID id,
            HttpServletRequest request) {
        assistantService.deleteConversation(id, currentUserService.currentUser());
        return apiResponseFactory.success("Conversation deleted successfully", null, request);
    }
}

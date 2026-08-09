package com.smartfarmer.ai.assistant.service;

import com.smartfarmer.ai.assistant.dto.ConversationDetailResponse;
import com.smartfarmer.ai.assistant.dto.ConversationResponse;
import com.smartfarmer.ai.assistant.dto.CreateConversationRequest;
import com.smartfarmer.ai.assistant.dto.MessageResponse;
import com.smartfarmer.ai.assistant.dto.SendMessageRequest;
import com.smartfarmer.ai.assistant.entity.AIConversation;
import com.smartfarmer.ai.assistant.entity.AIMessage;
import com.smartfarmer.ai.assistant.repository.AIConversationRepository;
import com.smartfarmer.ai.assistant.repository.AIMessageRepository;
import com.smartfarmer.ai.common.enums.ConversationRole;
import com.smartfarmer.ai.exception.ResourceNotFoundException;
import com.smartfarmer.ai.user.entity.User;
import org.springframework.security.access.AccessDeniedException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssistantService {

    private static final int MAX_CONTENT_LENGTH = 4000;

    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;
    private final AssistantProvider assistantProvider;

    public AssistantService(AIConversationRepository conversationRepository,
                            AIMessageRepository messageRepository,
                            AssistantProvider assistantProvider) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
        this.assistantProvider = assistantProvider;
    }

    @Transactional
    public ConversationResponse createConversation(User user, CreateConversationRequest request) {
        AIConversation conversation = new AIConversation();
        conversation.setUser(user);
        conversation.setTitle(request.title());
        return mapToConversationResponse(conversationRepository.save(conversation));
    }

    @Transactional(readOnly = true)
    public Page<ConversationResponse> listConversations(User user, Pageable pageable) {
        return conversationRepository.findByUserId(user.getId(), pageable)
                .map(this::mapToConversationResponse);
    }

    @Transactional(readOnly = true)
    public ConversationDetailResponse getConversation(UUID id, User user) {
        AIConversation conversation = getConversationEntity(id, user);
        List<MessageResponse> messages = messageRepository.findByConversationIdOrderByCreatedAtAsc(id).stream()
                .map(this::mapToMessageResponse)
                .toList();

        return new ConversationDetailResponse(
                conversation.getId(),
                conversation.getTitle(),
                messages,
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );
    }

    /**
     * Stores the user message and answers with the configured provider. If no provider is
     * configured the provider raises a 503 and the whole exchange is rolled back, so the
     * conversation never contains a placeholder answer.
     */
    @Transactional
    public MessageResponse sendMessage(UUID conversationId, User user, SendMessageRequest request) {
        AIConversation conversation = getConversationEntity(conversationId, user);

        List<AssistantProvider.AssistantTurn> history =
                messageRepository.findByConversationIdOrderByCreatedAtAsc(conversationId).stream()
                        .map(message -> new AssistantProvider.AssistantTurn(message.getRole().name(), message.getContent()))
                        .toList();

        AIMessage userMessage = new AIMessage();
        userMessage.setConversation(conversation);
        userMessage.setRole(ConversationRole.USER);
        userMessage.setContent(request.content());
        messageRepository.save(userMessage);

        String answer = assistantProvider.reply(history, request.content());

        AIMessage aiMessage = new AIMessage();
        aiMessage.setConversation(conversation);
        aiMessage.setRole(ConversationRole.ASSISTANT);
        aiMessage.setContent(truncate(answer));
        aiMessage = messageRepository.save(aiMessage);

        conversationRepository.save(conversation);
        return mapToMessageResponse(aiMessage);
    }

    @Transactional
    public void deleteConversation(UUID id, User user) {
        AIConversation conversation = getConversationEntity(id, user);
        messageRepository.deleteAll(messageRepository.findByConversationIdOrderByCreatedAtAsc(id));
        conversationRepository.delete(conversation);
    }

    private AIConversation getConversationEntity(UUID id, User user) {
        AIConversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + id));
        if (!conversation.getUser().getId().equals(user.getId())) {
            throw new AccessDeniedException("You do not have access to this conversation");
        }
        return conversation;
    }

    private String truncate(String content) {
        return content.length() <= MAX_CONTENT_LENGTH ? content : content.substring(0, MAX_CONTENT_LENGTH);
    }

    private ConversationResponse mapToConversationResponse(AIConversation conversation) {
        return new ConversationResponse(
                conversation.getId(),
                conversation.getTitle(),
                conversation.getCreatedAt(),
                conversation.getUpdatedAt()
        );
    }

    private MessageResponse mapToMessageResponse(AIMessage message) {
        return new MessageResponse(
                message.getId(),
                message.getRole().name(),
                message.getContent(),
                message.getCreatedAt()
        );
    }
}

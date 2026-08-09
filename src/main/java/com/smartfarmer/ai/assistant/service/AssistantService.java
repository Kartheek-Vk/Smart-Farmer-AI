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
import com.smartfarmer.ai.exception.UnauthorizedException;
import com.smartfarmer.ai.user.entity.User;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssistantService {

    private final AIConversationRepository conversationRepository;
    private final AIMessageRepository messageRepository;

    public AssistantService(AIConversationRepository conversationRepository,
                            AIMessageRepository messageRepository) {
        this.conversationRepository = conversationRepository;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public ConversationResponse createConversation(User user, CreateConversationRequest request) {
        AIConversation conversation = new AIConversation();
        conversation.setUser(user);
        conversation.setTitle(request.title());
        conversation = conversationRepository.save(conversation);
        return mapToConversationResponse(conversation);
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

    @Transactional
    public MessageResponse sendMessage(UUID conversationId, User user, SendMessageRequest request) {
        AIConversation conversation = getConversationEntity(conversationId, user);

        AIMessage userMessage = new AIMessage();
        userMessage.setConversation(conversation);
        userMessage.setRole(ConversationRole.USER);
        userMessage.setContent(request.content());
        messageRepository.save(userMessage);

        AIMessage aiMessage = new AIMessage();
        aiMessage.setConversation(conversation);
        aiMessage.setRole(ConversationRole.ASSISTANT);
        aiMessage.setContent("AI response integration pending via Gemini/FastAPI");
        aiMessage = messageRepository.save(aiMessage);

        // Update conversation's updatedAt timestamp
        conversationRepository.save(conversation);

        return mapToMessageResponse(aiMessage);
    }

    @Transactional
    public void deleteConversation(UUID id, User user) {
        AIConversation conversation = getConversationEntity(id, user);
        conversationRepository.delete(conversation);
    }

    private AIConversation getConversationEntity(UUID id, User user) {
        AIConversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found with id: " + id));
        if (!conversation.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You do not have access to this conversation");
        }
        return conversation;
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

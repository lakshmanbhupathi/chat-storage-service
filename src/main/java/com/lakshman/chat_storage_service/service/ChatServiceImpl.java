package com.lakshman.chat_storage_service.service;

import com.lakshman.chat_storage_service.dto.CreateMessageRequest;
import com.lakshman.chat_storage_service.dto.CreateSessionRequest;
import com.lakshman.chat_storage_service.dto.MessageResponse;
import com.lakshman.chat_storage_service.dto.SessionResponse;
import com.lakshman.chat_storage_service.dto.UpdateSessionRequest;
import com.lakshman.chat_storage_service.entity.ChatMessage;
import com.lakshman.chat_storage_service.entity.ChatSession;
import com.lakshman.chat_storage_service.exception.ResourceNotFoundException;
import com.lakshman.chat_storage_service.repository.ChatMessageRepository;
import com.lakshman.chat_storage_service.repository.ChatSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatSessionRepository sessionRepository;

    @Autowired
    private ChatMessageRepository messageRepository;

    @Override
    public SessionResponse createSession(CreateSessionRequest request) {
        log.info("Creating new chat session for user: {}", request.getUserId());
        ChatSession session = new ChatSession();
        session.setUserId(request.getUserId());
        session.setTitle(request.getTitle());
        session.setFavorite(false);

        ChatSession savedSession = sessionRepository.save(session);
        log.debug("Created session with ID: {}", savedSession.getId());
        return SessionResponse.fromEntity(savedSession);
    }

    @Override
    public SessionResponse getSession(UUID sessionId) {
        log.debug("Fetching session with ID: {}", sessionId);
        ChatSession session = sessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    log.error("Session not found with ID: {}", sessionId);
                    return new ResourceNotFoundException("Session not found with id: " + sessionId);
                });
        return SessionResponse.fromEntity(session);
    }

    @Override
    public List<SessionResponse> getUserSessions(String userId) {
        log.debug("Fetching sessions for user: {}", userId);
        return sessionRepository.findByUserId(userId).stream()
                .map(SessionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public SessionResponse updateSession(UUID sessionId, UpdateSessionRequest request) {
        log.info("Updating session with ID: {}", sessionId);
        ChatSession session = findSessionEntity(sessionId);
        if (request.getTitle() != null) {
            log.debug("Updating title for session {}: {}", sessionId, request.getTitle());
            session.setTitle(request.getTitle());
        }
        if (request.getIsFavorite() != null) {
            log.debug("Updating favorite status for session {}: {}", sessionId, request.getIsFavorite());
            session.setFavorite(request.getIsFavorite());
        }
        ChatSession updatedSession = sessionRepository.save(session);
        return SessionResponse.fromEntity(updatedSession);
    }

    @Transactional
    @Override
    public void deleteSession(UUID sessionId) {
        log.info("Deleting session with ID: {}", sessionId);
        if (!sessionRepository.existsById(sessionId)) {
            log.error("Attempted to delete non-existent session with ID: {}", sessionId);
            throw new ResourceNotFoundException("Session not found with id: " + sessionId);
        }
        messageRepository.deleteBySessionId(sessionId);
        sessionRepository.deleteById(sessionId);
        log.debug("Successfully deleted session and messages for ID: {}", sessionId);
    }

    private ChatSession findSessionEntity(UUID sessionId) {
        return sessionRepository.findById(sessionId)
                .orElseThrow(() -> {
                    log.error("Session not found with ID: {}", sessionId);
                    return new ResourceNotFoundException("Session not found with id: " + sessionId);
                });
    }

    @Override
    public MessageResponse addMessage(UUID sessionId, CreateMessageRequest request) {
        log.info("Adding message to session: {}", sessionId);
        ChatSession session = findSessionEntity(sessionId);

        ChatMessage message = new ChatMessage();
        message.setSession(session);
        try {
            message.setSender(ChatMessage.Sender.valueOf(request.getSender().name().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid sender type: " + request.getSender());
        }
        message.setContent(request.getContent());
        message.setContext(request.getContext());

        ChatMessage savedMessage = messageRepository.save(message);
        log.debug("Added message with ID: {} to session: {}", savedMessage.getId(), sessionId);
        return MessageResponse.fromEntity(savedMessage);
    }

    @Override
    public Page<MessageResponse> getSessionMessages(UUID sessionId, Pageable pageable) {
        log.debug("Fetching messages for session: {}", sessionId);
        if (!sessionRepository.existsById(sessionId)) {
            log.error("Session not found with ID:: {}", sessionId);
            throw new ResourceNotFoundException("Session not found with id: " + sessionId);
        }
        return messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId, pageable)
                .map(MessageResponse::fromEntity);
    }

}

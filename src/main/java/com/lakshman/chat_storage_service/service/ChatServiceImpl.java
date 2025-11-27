package com.lakshman.chat_storage_service.service;

import com.lakshman.chat_storage_service.dto.CreateSessionRequest;
import com.lakshman.chat_storage_service.dto.SessionResponse;
import com.lakshman.chat_storage_service.entity.ChatSession;
import com.lakshman.chat_storage_service.exception.ResourceNotFoundException;
import com.lakshman.chat_storage_service.repository.ChatMessageRepository;
import com.lakshman.chat_storage_service.repository.ChatSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

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


}

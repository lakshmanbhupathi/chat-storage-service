package com.lakshman.chat_storage_service.service;

import com.lakshman.chat_storage_service.dto.CreateSessionRequest;
import com.lakshman.chat_storage_service.dto.SessionResponse;
import com.lakshman.chat_storage_service.entity.ChatSession;
import com.lakshman.chat_storage_service.repository.ChatMessageRepository;
import com.lakshman.chat_storage_service.repository.ChatSessionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}

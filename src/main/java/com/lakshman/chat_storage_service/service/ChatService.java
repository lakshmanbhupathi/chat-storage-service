package com.lakshman.chat_storage_service.service;

import com.lakshman.chat_storage_service.dto.CreateMessageRequest;
import com.lakshman.chat_storage_service.dto.CreateSessionRequest;
import com.lakshman.chat_storage_service.dto.MessageResponse;
import com.lakshman.chat_storage_service.dto.SessionResponse;
import com.lakshman.chat_storage_service.dto.UpdateSessionRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    SessionResponse createSession(CreateSessionRequest request);

    SessionResponse getSession(UUID sessionId);

    List<SessionResponse> getUserSessions(String userId);

    SessionResponse updateSession(UUID sessionId, UpdateSessionRequest request);

    void deleteSession(UUID sessionId);

    MessageResponse addMessage(UUID sessionId, CreateMessageRequest request);

    Page<MessageResponse> getSessionMessages(UUID sessionId, Pageable pageable);
}

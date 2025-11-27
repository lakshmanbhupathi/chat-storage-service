package com.lakshman.chat_storage_service.service;

import com.lakshman.chat_storage_service.dto.CreateSessionRequest;
import com.lakshman.chat_storage_service.dto.SessionResponse;

import java.util.List;
import java.util.UUID;

public interface ChatService {
    SessionResponse createSession(CreateSessionRequest request);

    SessionResponse getSession(UUID sessionId);

    List<SessionResponse> getUserSessions(String userId);
}

package com.lakshman.chat_storage_service.service;

import com.lakshman.chat_storage_service.dto.CreateSessionRequest;
import com.lakshman.chat_storage_service.dto.SessionResponse;

public interface ChatService {
    SessionResponse createSession(CreateSessionRequest request);
}

package com.lakshman.chat_storage_service.service;

import com.lakshman.chat_storage_service.repository.ChatMessageRepository;
import com.lakshman.chat_storage_service.repository.ChatSessionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatServiceImpl implements ChatService {

    @Autowired
    private ChatSessionRepository sessionRepository;

    @Autowired
    private ChatMessageRepository messageRepository;
}

package com.lakshman.chat_storage_service.repository;

import com.lakshman.chat_storage_service.entity.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    Page<ChatMessage> findBySessionIdOrderByCreatedAtAsc(UUID sessionId, Pageable pageable);

    void deleteBySessionId(UUID sessionId);
}

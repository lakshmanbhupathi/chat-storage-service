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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChatServiceTest {

    @Mock
    private ChatSessionRepository sessionRepository;

    @Mock
    private ChatMessageRepository messageRepository;

    @InjectMocks
    private ChatServiceImpl chatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createSession_ShouldReturnSavedSession() {
        CreateSessionRequest request = new CreateSessionRequest();
        request.setUserId("user123");
        request.setTitle("Test Session");

        ChatSession savedSession = new ChatSession();
        savedSession.setId(UUID.randomUUID());
        savedSession.setUserId("user123");
        savedSession.setTitle("Test Session");
        savedSession.setFavorite(false);

        when(sessionRepository.save(any(ChatSession.class))).thenReturn(savedSession);

        SessionResponse result = chatService.createSession(request);

        assertNotNull(result);
        assertEquals("user123", result.getUserId());
        assertEquals("Test Session", result.getTitle());
        verify(sessionRepository, times(1)).save(any(ChatSession.class));
    }

    @Test
    void getSession_ShouldReturnSession_WhenExists() {
        UUID sessionId = UUID.randomUUID();
        ChatSession session = new ChatSession();
        session.setId(sessionId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));

        SessionResponse result = chatService.getSession(sessionId);

        assertEquals(sessionId, result.getId());
    }

    @Test
    void getSession_ShouldThrowException_WhenNotFound() {
        UUID sessionId = UUID.randomUUID();
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> chatService.getSession(sessionId));
    }

    @Test
    void getUserSessions_ShouldReturnListOfSessions() {
        String userId = "user123";
        ChatSession session1 = new ChatSession();
        session1.setId(UUID.randomUUID());
        session1.setUserId(userId);

        ChatSession session2 = new ChatSession();
        session2.setId(UUID.randomUUID());
        session2.setUserId(userId);

        when(sessionRepository.findByUserId(userId)).thenReturn(Arrays.asList(session1, session2));

        List<SessionResponse> result = chatService.getUserSessions(userId);

        assertEquals(2, result.size());
        assertEquals(session1.getId(), result.get(0).getId());
        assertEquals(session2.getId(), result.get(1).getId());
    }

    @Test
    void updateSession_ShouldUpdateTitleAndFavorite() {
        UUID sessionId = UUID.randomUUID();
        UpdateSessionRequest request = new UpdateSessionRequest();
        request.setTitle("New Title");
        request.setIsFavorite(true);

        ChatSession existingSession = new ChatSession();
        existingSession.setId(sessionId);
        existingSession.setTitle("Old Title");
        existingSession.setFavorite(false);

        ChatSession updatedSession = new ChatSession();
        updatedSession.setId(sessionId);
        updatedSession.setTitle("New Title");
        updatedSession.setFavorite(true);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(existingSession));
        when(sessionRepository.save(any(ChatSession.class))).thenReturn(updatedSession);

        SessionResponse result = chatService.updateSession(sessionId, request);

        assertEquals("New Title", result.getTitle());
        assertTrue(result.isFavorite());
    }

    @Test
    void deleteSession_ShouldDeleteSessionAndMessages() {
        UUID sessionId = UUID.randomUUID();
        when(sessionRepository.existsById(sessionId)).thenReturn(true);

        chatService.deleteSession(sessionId);

        verify(messageRepository, times(1)).deleteBySessionId(sessionId);
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void deleteSession_ShouldThrowException_WhenNotFound() {
        UUID sessionId = UUID.randomUUID();
        when(sessionRepository.existsById(sessionId)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> chatService.deleteSession(sessionId));
    }

    @Test
    void addMessage_ShouldReturnSavedMessage() {
        UUID sessionId = UUID.randomUUID();
        CreateMessageRequest request = new CreateMessageRequest();
        request.setSender(ChatMessage.Sender.USER);
        request.setContent("Hello");

        ChatSession session = new ChatSession();
        session.setId(sessionId);

        ChatMessage savedMessage = new ChatMessage();
        savedMessage.setId(UUID.randomUUID());
        savedMessage.setSession(session);
        savedMessage.setSender(ChatMessage.Sender.USER);
        savedMessage.setContent("Hello");

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(session));
        when(messageRepository.save(any(ChatMessage.class))).thenReturn(savedMessage);

        MessageResponse result = chatService.addMessage(sessionId, request);

        assertNotNull(result);
        assertEquals("Hello", result.getContent());
        assertEquals(sessionId, result.getSessionId());
    }

    @Test
    void getSessionMessages_ShouldReturnPagedMessages() {
        UUID sessionId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);
        ChatSession session = new ChatSession();
        session.setId(sessionId);

        ChatMessage message1 = new ChatMessage();
        message1.setId(UUID.randomUUID());
        message1.setSession(session);
        message1.setSender(ChatMessage.Sender.USER); // Set sender to avoid null pointer in fromEntity

        ChatMessage message2 = new ChatMessage();
        message2.setId(UUID.randomUUID());
        message2.setSession(session);
        message2.setSender(ChatMessage.Sender.ASSISTANT); // Set sender

        Page<ChatMessage> messagePage = new PageImpl<>(Arrays.asList(message1, message2));

        when(sessionRepository.existsById(sessionId)).thenReturn(true);
        when(messageRepository.findBySessionIdOrderByCreatedAtAsc(sessionId, pageable)).thenReturn(messagePage);

        Page<MessageResponse> result = chatService.getSessionMessages(sessionId, pageable);

        assertEquals(2, result.getTotalElements());
    }
}

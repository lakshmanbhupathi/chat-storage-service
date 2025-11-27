package com.lakshman.chat_storage_service.dto;

import com.lakshman.chat_storage_service.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Chat message response")
public class MessageResponse {
    @Schema(description = "Unique identifier of the message", example = "675ccc4b-61c2-4f42-ac89-36eaf53bd9c0")
    private UUID id;

    @Schema(description = "Session ID this message belongs to", example = "6d8cab31-dbd6-44b6-afc8-939888f566cb")
    private UUID sessionId;

    @Schema(description = "Sender of the message", example = "USER")
    private ChatMessage.Sender sender;

    @Schema(description = "Content of the message", example = "Hello, how are you?")
    private String content;

    @Schema(description = "Optional context or metadata", example = "{\"model\": \"gpt-4\"}")
    private String context;

    @Schema(description = "Timestamp when the message was created")
    private LocalDateTime createdAt;

    public static MessageResponse fromEntity(ChatMessage message) {
        return MessageResponse.builder()
                .id(message.getId())
                .sessionId(message.getSession().getId())
                .sender(message.getSender())
                .content(message.getContent())
                .context(message.getContext())
                .createdAt(message.getCreatedAt())
                .build();
    }
}

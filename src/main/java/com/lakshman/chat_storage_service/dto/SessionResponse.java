package com.lakshman.chat_storage_service.dto;

import com.lakshman.chat_storage_service.entity.ChatSession;
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
@Schema(description = "Chat session response")
public class SessionResponse {
    @Schema(description = "Unique identifier of the session", example = "6d8cab31-dbd6-44b6-afc8-939888f566cb")
    private UUID id;

    @Schema(description = "User ID who owns this session", example = "user123")
    private String userId;

    @Schema(description = "Title of the session", example = "My First Chat")
    private String title;

    @Schema(description = "Whether the session is marked as favorite", example = "false")
    private boolean isFavorite;

    @Schema(description = "Timestamp when the session was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the session was last updated")
    private LocalDateTime updatedAt;

    public static SessionResponse fromEntity(ChatSession session) {
        return SessionResponse.builder()
                .id(session.getId())
                .userId(session.getUserId())
                .title(session.getTitle())
                .isFavorite(session.isFavorite())
                .createdAt(session.getCreatedAt())
                .updatedAt(session.getUpdatedAt())
                .build();
    }
}

package com.lakshman.chat_storage_service.dto;

import com.lakshman.chat_storage_service.entity.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Request to add a new message to a session")
public class CreateMessageRequest {
    @NotNull(message = "Sender is required")
    @Schema(description = "Sender of the message (USER or ASSISTANT)", example = "USER")
    private ChatMessage.Sender sender;

    @NotBlank(message = "Content is required")
    @Schema(description = "Content of the message", example = "Hello, how are you?")
    private String content;

    @Schema(description = "Optional context or metadata for the message", example = "{\"model\": \"gpt-4\"}")
    private String context;
}

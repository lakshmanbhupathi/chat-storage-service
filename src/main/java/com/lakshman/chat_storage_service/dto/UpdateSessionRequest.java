package com.lakshman.chat_storage_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Request to update an existing chat session")
public class UpdateSessionRequest {

    @Schema(description = "New title of the chat session", example = "Updated Project Discussion")
    private String title;

    @Schema(description = "Mark session as favorite", example = "true")
    private Boolean isFavorite;
}

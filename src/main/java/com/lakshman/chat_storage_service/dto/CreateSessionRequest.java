package com.lakshman.chat_storage_service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request to create a new chat session")
public class CreateSessionRequest {

    @Schema(description = "Title of the chat session", example = "Project Discussion")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "User ID associated with the session", example = "user-123")
    @NotBlank(message = "User ID is required")
    private String userId;

    public CreateSessionRequest() {
    }

    public CreateSessionRequest(String title, String userId) {
        this.title = title;
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}

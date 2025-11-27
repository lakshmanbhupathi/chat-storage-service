package com.lakshman.chat_storage_service.controller;

import com.lakshman.chat_storage_service.dto.CreateMessageRequest;
import com.lakshman.chat_storage_service.dto.CreateSessionRequest;
import com.lakshman.chat_storage_service.dto.MessageResponse;
import com.lakshman.chat_storage_service.dto.SessionResponse;
import com.lakshman.chat_storage_service.dto.UpdateSessionRequest;
import com.lakshman.chat_storage_service.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/sessions")
public class ChatController {

    @Autowired
    ChatService chatService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new chat session", description = "Creates a new chat session for a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Session created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public SessionResponse createSession(@Valid @RequestBody CreateSessionRequest request) {
        log.info("Received request to create session for user: {}", request.getUserId());
        return chatService.createSession(request);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get a specific session", description = "Retrieves details of a specific chat session by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session found"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public SessionResponse getSession(
            @Parameter(description = "UUID of the session to retrieve", required = true) @PathVariable UUID id) {
        log.info("Received request to get session: {}", id);
        return chatService.getSession(id);
    }


    @GetMapping
    @Operation(summary = "Get all sessions for a user", description = "Retrieves a list of all chat sessions belonging to the specified user.")
    @ApiResponse(responseCode = "200", description = "List of sessions retrieved successfully")
    public List<SessionResponse> getUserSessions(
            @Parameter(description = "ID of the user to fetch sessions for", required = true, example = "user123") @RequestParam String userId) {
        log.info("Received request to get sessions for user: {}", userId);
        return chatService.getUserSessions(userId);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update a session", description = "Updates the title or favorite status of a chat session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session updated successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public SessionResponse updateSession(
            @Parameter(description = "UUID of the session to update", required = true) @PathVariable UUID id,
            @RequestBody UpdateSessionRequest request) {
        log.info("Received request to update session: {}", id);
        return chatService.updateSession(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a session", description = "Deletes a chat session and all its associated messages.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Session deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    public void deleteSession(
            @Parameter(description = "UUID of the session to delete", required = true) @PathVariable UUID id) {
        log.info("Received request to delete session: {}", id);
        chatService.deleteSession(id);
    }

    @PostMapping("/{id}/messages")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a message to a session", description = "Adds a new message to an existing chat session.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Message added successfully"),
            @ApiResponse(responseCode = "404", description = "Session not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public MessageResponse addMessage(
            @Parameter(description = "UUID of the session to add the message to", required = true) @PathVariable UUID id,
            @Valid @RequestBody CreateMessageRequest request) {
        log.info("Received request to add message to session: {}", id);
        return chatService.addMessage(id, request);
    }

}

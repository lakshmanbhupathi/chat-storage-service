package com.lakshman.chat_storage_service.controller;

import com.lakshman.chat_storage_service.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/sessions")
public class ChatController {

    @Autowired
    ChatService chatService;

}

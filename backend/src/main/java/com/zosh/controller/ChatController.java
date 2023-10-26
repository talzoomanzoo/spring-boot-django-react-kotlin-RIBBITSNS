package com.zosh.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.zosh.model.Message;

@Controller
public class ChatController {

    @MessageMapping("/chat/{userId}")
    @SendTo("/chat/{userId}")
    public Message sendMessage(@DestinationVariable String userId, Message message) {
        return message;
    }
}

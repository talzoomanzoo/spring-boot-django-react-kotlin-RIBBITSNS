package com.zosh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.zosh.dto.ChatResponseDto;
import com.zosh.dto.ChatUserDto;
import com.zosh.dto.MessageDto;
import com.zosh.dto.mapper.ChatUserDtoMapper;
import com.zosh.dto.mapper.MessageDtoMapper;
import com.zosh.model.ChatUser;
import com.zosh.model.Message;
import com.zosh.model.Status;
import com.zosh.service.ChatUserService;
import com.zosh.service.MessageService;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private ChatUserService chatUserService;
    
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ResponseEntity<ChatResponseDto> receiveMessage(@Payload Message message) {
    	System.out.println("receive message: "+message);
    	
    	ChatUserDto chatUserDto = new ChatUserDto();
    	
    	if(message.getStatus() == Status.JOIN) {
    		ChatUser chatUser = new ChatUser();
    		chatUser.setUsername(message.getSenderName());
    		chatUser = chatUserService.insertuser(chatUser);
    		chatUserDto = ChatUserDtoMapper.chatUserDto(chatUser);
    		
    	}
    	
    	MessageDto messageDto = new MessageDto();
    	
    	if(message.getStatus() == Status.MESSAGE) {
    		Message message2 = messageService.createMessage(message);
        	messageDto = MessageDtoMapper.toEntity(message2);
    	}
    	
    	ChatResponseDto responseDto = new ChatResponseDto();
        responseDto.setChatUserDto(chatUserDto);
        responseDto.setMessageDto(messageDto);
    	
    	return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    	
    }
    
    @MessageMapping("/private-message")
    public Message recMessage(@Payload Message message) {
    	simpMessagingTemplate.convertAndSendToUser(message.getReceiverName(), "/private", message);
    	System.out.println("message: "+message);
    	return message;
    }
}

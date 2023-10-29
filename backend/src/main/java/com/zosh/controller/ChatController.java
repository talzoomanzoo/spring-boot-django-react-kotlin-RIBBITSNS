package com.zosh.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.zosh.dto.ChatroomDto;
import com.zosh.dto.MessageDto;
import com.zosh.dto.mapper.ChatroomDtoMapper;
import com.zosh.dto.mapper.MessageDtoMapper;
import com.zosh.model.Chatroom;
import com.zosh.model.Message;
import com.zosh.model.Status;
import com.zosh.service.ChatroomService;
import com.zosh.service.MessageService;

@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private ChatroomService chatroomService;
    
    @MessageMapping("/message")
    @SendTo("/chatroom/public")
    public ResponseEntity<MessageDto> receiveMessage(@Payload Message message) {
    	System.out.println("receive message: "+message);
    	
    	MessageDto messageDto = new MessageDto();
    	
    	if(message.getStatus() == Status.MESSAGE) {
    		Message message2 = messageService.createMessage(message);
        	messageDto = MessageDtoMapper.toEntity(message2);
    	}
    	
    	return new ResponseEntity<>(messageDto,HttpStatus.CREATED);
    	
    }
    
    @PostMapping("/createroom")
    public ResponseEntity<ChatroomDto> createChatRoom(@RequestBody Chatroom chatroom){
    	Chatroom chatroom2 = chatroomService.createroom(chatroom);
    	ChatroomDto chatroomDto = ChatroomDtoMapper.chatUserDto(chatroom2);
    	return new ResponseEntity<>(chatroomDto,HttpStatus.CREATED);
    }
    
    @GetMapping("/allrooms")
    public ResponseEntity<List<Chatroom>> getAllChatRooms(){
    	List<Chatroom> chatrooms = chatroomService.getallrooms();
    	return new ResponseEntity<>(chatrooms,HttpStatus.OK);
    }
    
}

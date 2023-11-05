package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ChatDto;
import com.zosh.dto.ChatRoomDto;
import com.zosh.dto.mapper.ChatDtoMapper;
import com.zosh.dto.mapper.ChatRoomDtoMapper;
import com.zosh.model.Chat;
import com.zosh.model.Chat.MessageType;
import com.zosh.model.ChatRoom;
import com.zosh.service.ChatService;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
	
	private final ChatService service;
	
	private final SimpMessagingTemplate messagingTemplate;

    @PostMapping("/createroom")
    public ResponseEntity<ChatRoomDto> createRoom(@RequestBody String name){
    	ChatRoom createdroom = service.createRoom(name);
    	ChatRoomDto chatRoomDto = ChatRoomDtoMapper.toChatRoomDto(createdroom);
        return new ResponseEntity<>(chatRoomDto,HttpStatus.CREATED);
    }

    @GetMapping("/allrooms")
    public ResponseEntity<List<ChatRoomDto>> findAllRooms(){
    	List<ChatRoom> allrooms = service.findAllRoom();
    	List<ChatRoomDto> chatRoomDtos = ChatRoomDtoMapper.toChatRoomDtos(allrooms);
    	return new ResponseEntity<List<ChatRoomDto>>(chatRoomDtos,HttpStatus.OK);
    }

    @MessageMapping("/savechat")
    public ResponseEntity<ChatDto> savechat(@Payload Chat chat){
    	Chat chat2 = service.saveChat(chat);
    	ChatDto chatDto = ChatDtoMapper.toChatDto(chat2);
    	return new ResponseEntity<>(chatDto,HttpStatus.CREATED);
    }
	
    @MessageMapping("/enter")
    public ResponseEntity<List<Chat>> enterchatroom(@Payload Chat chat){
    	
    	System.out.println("enterchat: "+chat);
    	MessageType type = chat.getType();
    	String roomId = chat.getRoomId();
    	String sender = chat.getSender();
    	
    	Chat savechat = new Chat();
    	savechat.setType(type);
    	savechat.setRoomId(roomId);
    	savechat.setSender(sender);
    	service.saveChat(savechat);
    	
    	
    
    	List<Chat> chatHistory = service.chathistory(roomId);
    	
		return new ResponseEntity<>(chatHistory,HttpStatus.OK);
    }
}

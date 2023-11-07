package com.zosh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

@Controller
@Slf4j
@RequiredArgsConstructor
//@RequestMapping("/chat")
public class ChatController {
	
	@Autowired
	private ChatService service;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

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

    @MessageMapping("/savechat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ResponseEntity<ChatDto> savechat(@Payload Chat chat, @DestinationVariable String roomId) {
        System.out.println("chat: " + chat);
        Chat chat2 = service.saveChat(chat);
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat2);
        
        messagingTemplate.convertAndSend("/topic/"+ roomId, chatDto);
        
        return new ResponseEntity<>(chatDto, HttpStatus.CREATED);
    }
	
    @PostMapping("/getchat")
    public ResponseEntity<List<ChatDto>> getchathistory(@RequestBody String roomId){
    	System.out.println(roomId);
    	
    	
    	List<Chat> chatHistory = service.chathistory(roomId);
    	System.out.println("chathistory: "+chatHistory);
    	
    	List<ChatDto> chatDtos = ChatDtoMapper.chatDtos(chatHistory);
    	System.out.println("chatDtos: "+chatDtos);
    	
		return new ResponseEntity<List<ChatDto>>(chatDtos,HttpStatus.OK);
    }
}

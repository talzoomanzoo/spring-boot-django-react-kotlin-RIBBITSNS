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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ChatDto;
import com.zosh.dto.ChatRoomDto;
import com.zosh.dto.ChatSenderListDto;
import com.zosh.dto.UserDto;
import com.zosh.dto.mapper.ChatDtoMapper;
import com.zosh.dto.mapper.ChatRoomDtoMapper;
import com.zosh.dto.mapper.ChatSenderListDtoMapper;
import com.zosh.dto.mapper.UserDtoMapper;
import com.zosh.model.Chat;
import com.zosh.model.Chat.MessageType;
import com.zosh.model.ChatRoom;
import com.zosh.model.ChatSenderList;
import com.zosh.model.User;
import com.zosh.service.ChatSenderListService;
import com.zosh.service.ChatService;
import com.zosh.service.UserService;

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
	private ChatSenderListService chatSenderListService;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;

    @PostMapping("/createroom")
    public ResponseEntity<ChatRoomDto> createRoom(@RequestBody ChatRoom chatRoom){//채팅방 만들기
    	ChatRoom createdroom = service.createRoom(chatRoom);
    	
    	ChatSenderList senderList = new ChatSenderList();//채팅방 만들때 만드는 사람의 이메일(Email), 이름(sender)가 들어감
    	senderList.setEmail(createdroom.getCreatorEmail());
    	senderList.setRoomId(createdroom.getRoomId());
    	senderList.setSender(createdroom.getCreator());
    	chatSenderListService.saveUsers(senderList);
    	
    	ChatRoomDto chatRoomDto = ChatRoomDtoMapper.toChatRoomDto(createdroom);
        return new ResponseEntity<>(chatRoomDto,HttpStatus.CREATED);
    }

    @GetMapping("/allrooms")
    public ResponseEntity<List<ChatRoomDto>> findAllRooms(){//채팅방 내역 출력
    	List<ChatRoom> allrooms = service.findAllRoom();
    	List<ChatRoomDto> chatRoomDtos = ChatRoomDtoMapper.toChatRoomDtos(allrooms);
    	return new ResponseEntity<List<ChatRoomDto>>(chatRoomDtos,HttpStatus.OK);
    }

    @MessageMapping("/savechat/{roomId}")
    @SendTo("/topic/{roomId}")
    public ResponseEntity<ChatDto> savechat(@Payload Chat chat, @DestinationVariable String roomId) {//채팅
        System.out.println("chat: " + chat);
        Chat chat2 = service.saveChat(chat);
        ChatDto chatDto = ChatDtoMapper.toChatDto(chat2);
        
        messagingTemplate.convertAndSend("/topic/"+ roomId, chatDto);
        
        return new ResponseEntity<>(chatDto, HttpStatus.CREATED);
    }
	
    @PostMapping("/getchat")
    public ResponseEntity<List<ChatDto>> getchathistory(@RequestBody String roomId){//해당방 기존 채팅 내역 출력
    	System.out.println(roomId);
    	
    	
    	List<Chat> chatHistory = service.chathistory(roomId);
    	List<ChatDto> chatDtos = ChatDtoMapper.chatDtos(chatHistory);
    	
		return new ResponseEntity<List<ChatDto>>(chatDtos,HttpStatus.OK);
    }
    
    @PostMapping("/addusers")
    public ResponseEntity<ChatSenderListDto> adduserstolist(@RequestBody ChatSenderList list){//채팅방에 유저 초대
    	ChatSenderList list2 = chatSenderListService.saveUsers(list);
    	ChatSenderListDto dto = ChatSenderListDtoMapper.chatSenderListDto(list2);
    	
    	return new ResponseEntity<>(dto,HttpStatus.CREATED);
    }
    
    @PostMapping("/findusers")
    public ResponseEntity<List<ChatSenderListDto>> findusersinRoom(@RequestBody String roomId){//해당 채팅방에 있는 유저 내역 출력
    	List<ChatSenderList> allusers = chatSenderListService.findusers(roomId);
    	List<ChatSenderListDto> dtos = ChatSenderListDtoMapper.chatSenderListDtos(allusers);
    	
    	return new ResponseEntity<List<ChatSenderListDto>>(dtos,HttpStatus.OK);
    }
    
    @PostMapping("/finduser")//채팅방에 있는 사람 검색
    public ResponseEntity<ChatSenderListDto> finduserinRoom(@RequestBody ChatSenderList list){
    	ChatSenderList senderList = chatSenderListService.finduserinRoom(list);
    	if(senderList != null) {
	    	ChatSenderListDto dto = ChatSenderListDtoMapper.chatSenderListDto(senderList);
			return new ResponseEntity<>(dto,HttpStatus.OK);
    	} else{
    		return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
    	}
    	
    }
    
    @Transactional
    @PostMapping("/deleteusers")//채팅방 나가기
    public ResponseEntity<ChatSenderListDto> deleteuserinRoom(@RequestBody ChatSenderList list){
    	System.out.println("list: "+list.getEmail()+", "+list.getRoomId());
		chatSenderListService.deleteUser(list);
		return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @PostMapping("/editroom")//채팅방 이름 수정
    public ResponseEntity<ChatRoomDto> editRoom(@RequestBody ChatRoom chatRoom){
    	ChatRoom room = service.editRoom(chatRoom);
    	ChatRoomDto dto = ChatRoomDtoMapper.toChatRoomDto(room);
    	
    	return new ResponseEntity<>(dto,HttpStatus.OK);
    }
}

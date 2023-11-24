package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.dto.ChatRoomDto;
import com.zosh.model.Chat;
import com.zosh.model.ChatRoom;
import com.zosh.repository.ChatRepository;
import com.zosh.repository.ChatRoomRepository;
import com.zosh.repository.UserRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@Service
public class ChatService {
	private final ObjectMapper mapper;
    private Map<String, ChatRoomDto> chatRooms;
    
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    
    @Autowired
	private UserService userService;
    
    public Chat saveChat(Chat chat) {//채팅저장
    	Chat chat1 = new Chat();
    	
    	chat1.setSender(chat.getSender());
    	chat1.setEmail(chat.getEmail());
    	chat1.setRoomId(chat.getRoomId());
    	chat1.setMessage(chat.getMessage());
    	chat1.setTimestamp(LocalDateTime.now());
    	chat1.setType(chat.getType());
    	
		return chatRepository.save(chat1);
    }

    public List<ChatRoom> findAllRoom(){//전체 채팅방 출력
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(String roomId){//roomId로 채팅방 찾기
        return chatRoomRepository.findByRoomId(roomId);
    }
    
    public ChatRoom editRoom(ChatRoom room) {//채팅방 이름 수정
    	
    	ChatRoom chatRoom = chatRoomRepository.findByRoomId(room.getRoomId());
    	chatRoom.setName(room.getName());
    	
    	chatRoomRepository.save(chatRoom);
    	
    	return chatRoom;
    }

    public ChatRoom createRoom(ChatRoom room) {//채팅방 만들기
        String roomId = UUID.randomUUID().toString(); // 랜덤한 방 아이디 생성
        
        ChatRoom room1 = new ChatRoom();
        room1.setRoomId(roomId);
        room1.setName(room.getName());
        room1.setCreator(room.getCreator());
        room1.setCreatorEmail(room.getCreatorEmail());
        
        return chatRoomRepository.save(room1);
    }
    
    public List<Chat> chathistory(String roomId){//기존 채팅 내역 출력
    	return chatRepository.findByRoomId(roomId);
    }
}

package com.zosh.service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.dto.ChatDto;
import com.zosh.dto.ChatRoomDto;
import com.zosh.model.Chat;
import com.zosh.model.ChatRoom;
import com.zosh.repository.ChatRepository;
import com.zosh.repository.ChatRoomRepository;

import jakarta.annotation.PostConstruct;
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
    
    public Chat saveChat(Chat chat) {
    	Chat chat1 = new Chat();
    	chat1.setSender(chat.getSender());
    	chat1.setRoomId(chat.getRoomId());
    	chat1.setMessage(chat.getMessage());
    	chat1.setTimestamp(LocalDateTime.now());
    	chat1.setType(chat.getType());
    	
		return chatRepository.save(chat1);
    }

    public List<ChatRoom> findAllRoom(){
        return chatRoomRepository.findAll();
    }

    public ChatRoom findRoomById(String roomId){
        return chatRoomRepository.findByRoomId(roomId);
    }

    public ChatRoom createRoom(String name) {
        String roomId = UUID.randomUUID().toString(); // 랜덤한 방 아이디 생성
        
        ChatRoom room1 = new ChatRoom();
        room1.setRoomId(roomId);
        room1.setName(name);
        
        return chatRoomRepository.save(room1);
    }
    
    public List<Chat> chathistory(String roomId){
    	List<Chat> chat = chatRepository.findByRoomId(roomId);
    	return chat;
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}

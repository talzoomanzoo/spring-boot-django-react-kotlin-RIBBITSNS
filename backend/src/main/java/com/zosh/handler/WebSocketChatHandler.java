package com.zosh.handler;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zosh.dto.ChatDto;
import com.zosh.dto.ChatRoomDto;
import com.zosh.dto.mapper.ChatDtoMapper;
import com.zosh.model.Chat;
import com.zosh.model.ChatRoom;
import com.zosh.service.ChatService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler{
	private final ObjectMapper mapper;
	
	private final ChatService service;
	
	private Set<WebSocketSession> sessions = new HashSet<>();
	
	public void handleAction(WebSocketSession session, Chat message, ChatService service) {
        // message 에 담긴 타입을 확인한다.
        // 이때 message 에서 getType 으로 가져온 내용이
        // ChatDTO 의 열거형인 MessageType 안에 있는 ENTER 과 동일한 값이라면
        if (message.getType().equals(Chat.MessageType.ENTER)) {
            // sessions 에 넘어온 session 을 담고,
            sessions.add(session);

            // message 에는 입장하였다는 메시지를 띄운다
            message.setMessage(message.getSender() + " 님이 입장하셨습니다");
            sendMessage(message, service);
        } else if (message.getType().equals(Chat.MessageType.TALK)) {
            message.setMessage(message.getMessage());
            sendMessage(message, service);
        }
    }

    public <T> void sendMessage(T message, ChatService service) {
        sessions.parallelStream().forEach(session -> service.sendMessage(session, message));
    }
	
	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		String payload = message.getPayload();
		log.info("payload {}",payload);
		
		Chat chatMessage = mapper.readValue(payload, Chat.class);
        log.info("session {}", chatMessage.toString());
        Chat chat = service.saveChat(chatMessage);
        System.out.println("chat: "+chat);
        //ChatDto chatDto = ChatDtoMapper.toChatDto(chat);
        
        ChatRoom room = service.findRoomById(chatMessage.getRoomId());
        log.info("room {}", room.toString());

        handleAction(session, chatMessage, service);
	}
}

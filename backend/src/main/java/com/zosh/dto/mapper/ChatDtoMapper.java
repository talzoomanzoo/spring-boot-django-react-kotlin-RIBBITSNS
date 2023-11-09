package com.zosh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.zosh.dto.ChatDto;
import com.zosh.model.Chat;

public class ChatDtoMapper {
	
	public static ChatDto toChatDto(Chat chat) {
		ChatDto chatDto = new ChatDto();
		chatDto.setId(chat.getId());
		chatDto.setMessage(chat.getMessage());
		chatDto.setRoomId(chat.getRoomId());
		chatDto.setSender(chat.getSender());
		chatDto.setEmail(chat.getEmail());
		chatDto.setTime(chat.getTimestamp());
		chatDto.setType(ChatDto.MessageType.valueOf(chat.getType().name()));
		
		return chatDto;
	}
	
	public static List<ChatDto> chatDtos(List<Chat> chats){
		List<ChatDto> chats2 = new ArrayList<>();
		for (Chat chat : chats) {
			ChatDto chatDto = toChatDto(chat);
			chats2.add(chatDto);
		}
		
		return chats2;
	}

}

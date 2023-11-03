package com.zosh.dto.mapper;

import com.zosh.dto.ChatDto;
import com.zosh.model.Chat;

public class ChatDtoMapper {
	
	public static ChatDto toChatDto(Chat chat) {
		ChatDto chatDto = new ChatDto();
		chatDto.setId(chat.getId());
		chatDto.setMessage(chat.getMessage());
		chatDto.setRoomId(chat.getRoomId());
		chatDto.setSender(chat.getSender());
		chatDto.setTime(chat.getTimestamp());
		chatDto.setType(ChatDto.MessageType.valueOf(chat.getType().name()));
		
		return chatDto;
	}

}

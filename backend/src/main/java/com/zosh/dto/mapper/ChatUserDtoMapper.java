package com.zosh.dto.mapper;

import com.zosh.dto.ChatUserDto;
import com.zosh.model.ChatUser;

public class ChatUserDtoMapper {
	
	public static ChatUserDto chatUserDto(ChatUser chatUser) {
		ChatUserDto chatUserDto = new ChatUserDto();
		chatUserDto.setUsername(chatUser.getUsername());
		
		return chatUserDto;
	}

}

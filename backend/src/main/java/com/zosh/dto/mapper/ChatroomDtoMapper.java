package com.zosh.dto.mapper;

import com.zosh.dto.ChatroomDto;
import com.zosh.model.Chatroom;

public class ChatroomDtoMapper {
	
	public static ChatroomDto chatUserDto(Chatroom chatroom) {
		ChatroomDto chatroomDto = new ChatroomDto();
		chatroomDto.setName(chatroom.getName());
		chatroomDto.setTime(chatroom.getTime());
		chatroomDto.setParticipants(chatroom.getParticipants());
		
		return chatroomDto;
	}

}

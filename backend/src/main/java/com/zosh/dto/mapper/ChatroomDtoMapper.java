package com.zosh.dto.mapper;

import com.zosh.dto.ChatRoomDto;
import com.zosh.model.ChatRoom;

public class ChatRoomDtoMapper {
	
	public static ChatRoomDto toChatRoomDto(ChatRoom chatRoom) {
		ChatRoomDto chatRoomDto = new ChatRoomDto();
		chatRoomDto.setId(chatRoom.getId());
		chatRoomDto.setName(chatRoom.getName());
		chatRoomDto.setRoomId(chatRoom.getRoomId());
		
		return chatRoomDto;
	}

}

package com.zosh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

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

	public static List<ChatRoomDto> toChatRoomDtos(List<ChatRoom> chatRooms){
		List<ChatRoomDto> chatRoomDtos = new ArrayList<>();
		for (ChatRoom chatRoom : chatRooms) {
			ChatRoomDto chatRoomDto = toChatRoomDto(chatRoom);
			chatRoomDtos.add(chatRoomDto);
		}
		
		return chatRoomDtos;
	}
}

package com.zosh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.zosh.dto.ChatSenderListDto;
import com.zosh.model.ChatSenderList;

public class ChatSenderListDtoMapper {
	public static ChatSenderListDto chatSenderListDto(ChatSenderList list) {
		ChatSenderListDto listDto = new ChatSenderListDto();
		listDto.setId(list.getId());
		listDto.setRoomId(list.getRoomId());
		listDto.setEmail(list.getEmail());
		listDto.setSender(list.getSender());
		listDto.setEntertime(list.getEntertime());
		
		return listDto;
	}
	
	public static List<ChatSenderListDto> chatSenderListDtos(List<ChatSenderList> lists){
		List<ChatSenderListDto> dtos = new ArrayList<>();
		for (ChatSenderList senderList : lists) {
			ChatSenderListDto dto = chatSenderListDto(senderList);
			dtos.add(dto);
		}
		return dtos;
	}

}

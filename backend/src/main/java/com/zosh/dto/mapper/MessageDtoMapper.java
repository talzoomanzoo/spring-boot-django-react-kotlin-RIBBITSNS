package com.zosh.dto.mapper;

import com.zosh.dto.MessageDto;
import com.zosh.model.Message;

public class MessageDtoMapper {
	
	public static MessageDto toEntity(Message message) {
		
		MessageDto messageDto = new MessageDto();
		messageDto.setSenderName(message.getSenderName());
		messageDto.setReceiverName(message.getReceiverName());
		messageDto.setMessage(message.getMessage());
		messageDto.setDate(message.getDate());
		
		return messageDto;
		
	}

}

package com.zosh.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.Message;
import com.zosh.repository.ChatRepository;

@Service
public class MessageServiceImplementation implements MessageService{

	@Autowired
	private ChatRepository chatRepository;
	
	@Override
	public Message createMessage(Message message) {
		// TODO Auto-generated method stub
		Message message2 = new Message();
		message2.setSenderName(message.getSenderName());
		message2.setRoomid(message.getRoomid());
		message2.setMessage(message.getMessage());
		message2.setDate(LocalDateTime.now());
		message2.setStatus(message.getStatus());
		
		return chatRepository.save(message2);
	}

}

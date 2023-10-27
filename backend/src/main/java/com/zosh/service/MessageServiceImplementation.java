package com.zosh.service;

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
		message2.setReceiverName(message.getReceiverName());
		message2.setMessage(message.getMessage());
		message2.setDate(message.getDate());
		
		return chatRepository.save(message2);
	}

}

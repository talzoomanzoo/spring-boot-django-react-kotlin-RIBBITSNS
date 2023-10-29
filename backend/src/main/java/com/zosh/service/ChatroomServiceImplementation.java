package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.Chatroom;
import com.zosh.repository.ChatroomRepository;

@Service
public class ChatroomServiceImplementation implements ChatroomService{

	@Autowired
	private ChatroomRepository chatroomRepository;

	@Override
	public Chatroom createroom(Chatroom chatroom) {
		// TODO Auto-generated method stub
		Chatroom chatroom2 = new Chatroom();
		chatroom2.setName(chatroom.getName());
		chatroom2.setTime(LocalDateTime.now());
		return chatroomRepository.save(chatroom2);
	}

	@Override
	public List<Chatroom> getallrooms() {
		// TODO Auto-generated method stub
		return chatroomRepository.findAll();
	}
	
	

}

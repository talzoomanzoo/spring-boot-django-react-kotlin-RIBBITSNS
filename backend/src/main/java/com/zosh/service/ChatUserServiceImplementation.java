package com.zosh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.ChatUser;
import com.zosh.repository.ChatUserRepository;

@Service
public class ChatUserServiceImplementation implements ChatUserService{

	@Autowired
	private ChatUserRepository chatUserRepository;
	
	@Override
	public ChatUser insertuser(ChatUser chatUser) {
		// TODO Auto-generated method stub
		ChatUser chatUser2 = new ChatUser();
		chatUser2.setUsername(chatUser.getUsername());
		
		return chatUserRepository.save(chatUser2);
	}

}

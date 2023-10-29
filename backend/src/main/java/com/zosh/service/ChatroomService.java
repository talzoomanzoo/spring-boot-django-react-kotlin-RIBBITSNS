package com.zosh.service;

import java.util.List;

import com.zosh.model.Chatroom;

public interface ChatroomService {
	public Chatroom createroom(Chatroom chatUser);
	
	public List<Chatroom> getallrooms();
}

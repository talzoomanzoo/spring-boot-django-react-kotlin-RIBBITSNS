package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zosh.model.ChatSenderList;
import com.zosh.repository.ChatSenderListRepository;

@Service
public class ChatSenderListService {
	
	@Autowired
	private ChatSenderListRepository repository;
	
	public ChatSenderList saveUsers(ChatSenderList list) {//해당 채팅방 소속 유저 저장
		ChatSenderList senderList = new ChatSenderList();
		
		senderList.setEmail(list.getEmail());
		senderList.setEntertime(LocalDateTime.now());
		senderList.setRoomId(list.getRoomId());
		senderList.setSender(list.getSender());
		
		return repository.save(senderList);
	}
	
	public List<ChatSenderList> findusers(String roomId){//해당 채팅방 소속 유저 출력
		return repository.findByRoomId(roomId);
	}
	
	public void deleteUser(ChatSenderList list) {//현 채팅방에서 해당 유저 나가기
		repository.deleteByEmailAndRoomId(list.getEmail(), list.getRoomId());
	}
	
	public ChatSenderList finduserinRoom(ChatSenderList list) {//해당 채팅방에 있는 유저 검색
		return repository.findByEmailAndRoomId(list.getEmail(), list.getRoomId());
	}
}

package com.zosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.ChatSenderList;
import java.util.List;


public interface ChatSenderListRepository extends JpaRepository<ChatSenderList, Long>{
	List<ChatSenderList> findByRoomId(String roomId);
	
	
	void deleteByEmailAndRoomId(String email, String roomId);
	
	ChatSenderList findByEmailAndRoomId(String email, String roomId);
}

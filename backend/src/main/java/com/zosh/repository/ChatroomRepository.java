package com.zosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.ChatRoom;
import java.util.List;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, String>{
	ChatRoom findByRoomId(String roomId);
}

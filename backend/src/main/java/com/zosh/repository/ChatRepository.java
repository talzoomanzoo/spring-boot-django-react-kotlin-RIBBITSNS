package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long>{
	List<Chat> findByRoomId(String roomId);
}

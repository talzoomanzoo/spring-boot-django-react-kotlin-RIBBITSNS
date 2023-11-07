package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Chat;

public interface ChatRepository extends JpaRepository<Chat, Long>{
	
//	@Query("SELECT c FROM Chat c WHERE c.roomId = :roomId")
//	List<Chat> findByRoomId(@Param("roomId") String roomId);
	
	List<Chat> findByRoomId(String roomId);
}

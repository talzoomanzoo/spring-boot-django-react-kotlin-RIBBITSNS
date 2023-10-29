package com.zosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.Chatroom;

public interface ChatroomRepository extends JpaRepository<Chatroom, Long>{

}

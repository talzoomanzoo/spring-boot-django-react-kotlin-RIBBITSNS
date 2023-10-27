package com.zosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.ChatUser;

public interface ChatUserRepository extends JpaRepository<ChatUser, Long>{

}

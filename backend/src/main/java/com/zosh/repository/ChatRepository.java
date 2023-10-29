package com.zosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.Message;

public interface ChatRepository extends JpaRepository<Message, Long>{

}

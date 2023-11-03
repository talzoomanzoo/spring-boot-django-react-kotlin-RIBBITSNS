package com.zosh.model;

import java.time.LocalDateTime;

import com.zosh.dto.ChatDto.MessageType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
public class Chat {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String roomId; // 방 번호
    private String message;
    private LocalDateTime timestamp;
    
    @Enumerated(EnumType.STRING)
    private MessageType type;
    public enum MessageType{
        ENTER, TALK
    }
    
//    @ManyToOne
//    @JoinColumn(name = "chat_room_id")
//    private ChatRoom chatRoom;
}

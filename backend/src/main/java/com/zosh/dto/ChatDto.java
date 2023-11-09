package com.zosh.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {

	
	private Long id;
    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 채팅을 보낸 사람
    private String email; //보낸 사람 이메일(프로필 사진 추출용)
    private String message; // 메시지
    private LocalDateTime time; // 채팅 발송 시간간
    public enum MessageType{
        ENTER, TALK
    }
}

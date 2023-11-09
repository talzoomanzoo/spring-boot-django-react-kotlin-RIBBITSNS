package com.zosh.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomDto {

	private Long id;
	private String roomId; // 채팅방 아이디
    private String name; // 채팅방 이름
    private String creator;//방 만든 사람
    private String creatorEmail;//만든사람 이메일

    
}

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

    
}

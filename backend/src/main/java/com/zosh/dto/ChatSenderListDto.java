package com.zosh.dto;

import java.time.LocalDateTime;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatSenderListDto {
	private Long id;
	
	private String roomId;
	
	private String email;
	private String sender;
	
	private LocalDateTime entertime;
}

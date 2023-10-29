package com.zosh.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.zosh.model.Chatuser;
import com.zosh.model.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatroomDto {
	private Long id;
	
	private String name;
	
	private LocalDateTime time;
	
	private List<Chatuser> participants;
}

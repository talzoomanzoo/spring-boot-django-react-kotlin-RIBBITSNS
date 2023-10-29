package com.zosh.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.zosh.model.Status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageDto {
	private Long id;
	
	private String senderName;
    private String roomid;
    private String message;
    private LocalDateTime date;
    private Status status;
}

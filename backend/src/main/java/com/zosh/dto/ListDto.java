package com.zosh.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ListDto {
	
	private Long id;
	
	private UserDto user;
	
	private LocalDateTime createdAt;
	
	private String listName;
	
	private String description;
	
	private String backgroundImage;
	
	private boolean privateMode;
	
	private List<UserDto> followingsl=new ArrayList<>();
}

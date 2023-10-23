package com.zosh.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TwitDto {
	
	private Long id;

	private String content;
	
	private String image;
	
	private String video;
	
	private UserDto user;
	
	private LocalDateTime createdAt;
	
	private String editedAt;
	
	private int totalLikes;
	
	private int totalReplies;
	
	private int totalRetweets;

	private int viewCount;
	
	private boolean isEdited;
	
    private boolean isLiked;
    
    private boolean isRetwit;
    
    private boolean isReply;
    
    private List<Long> retwitUsersId;
    
    private List<TwitDto> replyTwits;
	
}

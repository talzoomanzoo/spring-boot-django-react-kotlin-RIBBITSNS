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
	
	private ComDto community;
	
	private LocalDateTime createdAt;
	
	private LocalDateTime editedAt;
	
	private int totalLikes;
	
	private int totalReplies;
	
	private int totalRetweets;

	private int viewCount;
	
	private boolean isEdited;
	
    private boolean isLiked;
    
    private boolean isRetwit;
    
    private boolean isCom;
    
    private boolean isReply;

	private String location;
    
    private List<Long> retwitUsersId;

    private List<TwitDto> replyTwits;

	private String comName;
	
    private String ethicrate; //윤리수치 저장
    private int ethiclabel; //수치중 가장 큰것을 라벨값으로 저장
    private int ethicrateMAX;// 최고수치 저장
}

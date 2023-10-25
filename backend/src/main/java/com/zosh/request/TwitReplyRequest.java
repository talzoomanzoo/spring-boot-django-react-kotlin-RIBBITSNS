package com.zosh.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TwitReplyRequest {

	  
	    private String content;
	    
	    private Long twitId;

	    private LocalDateTime createdAt;

	    private String image; 


}

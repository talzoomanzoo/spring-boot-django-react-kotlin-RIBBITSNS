package com.zosh.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name="likes")

public class Like {
	
  	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	  
  	@ManyToOne(fetch = FetchType.LAZY)
  	private User user;
  	
  	
  	@ManyToOne(fetch = FetchType.LAZY)
  	//@JsonIgnoreProperties("likes") // Twit 엔티티의 likes 속성을 무시
  	private Twit twit;

  	
  	//@ManyToOne(fetch = FetchType.LAZY)
  	//private Community community;
}

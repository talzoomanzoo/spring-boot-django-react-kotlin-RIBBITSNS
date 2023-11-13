package com.zosh.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name="likes")

public class Like {
	
  	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	 
  	@JsonIgnore
  	@ManyToOne(fetch = FetchType.LAZY)
  	@JsonBackReference
  	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
  	private User user;
  	
  	//private Long userId;
  	
  	@JsonIgnore
  	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
  	@ManyToOne(fetch = FetchType.LAZY)
  	@JsonBackReference
  	private Twit twit;
  	
  	//private Long twitId;

  	
  	//@ManyToOne(fetch = FetchType.LAZY)
  	//private Community community;
}

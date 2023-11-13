package com.zosh.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

//@Entity
//@Getter
//@Setter
//@RequiredArgsConstructor
@Entity
@Data
@RequiredArgsConstructor
@Table(name="notifications")
public class Notification {
	
  	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	  
  	@ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "user_id")
  	private User user;
  	
  	@ManyToOne(fetch = FetchType.LAZY)
  	@JoinColumn(name = "twit_id")
  	private Twit twit;
  	
}

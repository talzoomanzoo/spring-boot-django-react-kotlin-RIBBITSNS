package com.zosh.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class Community {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
	//@JoinColumn(name = "user_id")
	private User user;
	
	//private Long userId;
	
	@JsonIgnore
	@OneToMany(cascade = {CascadeType.ALL, CascadeType.REMOVE})
	@JsonManagedReference
    //@JoinColumn(name = "twit_id")
	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
    private List<Twit> comTwits = new ArrayList<>();
	
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private String comName;

	private String description;

	private String backgroundImage;

	private boolean privateMode;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.DETACH)
	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
	private List<User> followingsc = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.DETACH)
	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
	private List<User> followingscReady = new ArrayList<>();
}

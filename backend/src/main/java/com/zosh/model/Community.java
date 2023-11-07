package com.zosh.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Entity
@Data
public class Community {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "com_user_id")
	private User user;
	
	@OneToMany(cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JoinColumn(name = "com_twit_id")
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
	private List<User> followingsc = new ArrayList<>();
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.DETACH)
	private List<User> followingscReady = new ArrayList<>();
}

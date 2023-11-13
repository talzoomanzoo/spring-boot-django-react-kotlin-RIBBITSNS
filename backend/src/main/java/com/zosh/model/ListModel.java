package com.zosh.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class ListModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JsonBackReference
	@JoinColumn(name = "user_id")
	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
	private User user;
	
	//private Long userId;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	@Column(nullable= false)
	private String listName;

	private String description;

	private String backgroundImage;

	private boolean privateMode;

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.DETACH)
	@JsonIgnoreProperties(value = {"twit", "likes", "user"})
	private List<User> followingsl = new ArrayList<>();

}

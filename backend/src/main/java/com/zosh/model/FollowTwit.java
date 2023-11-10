package com.zosh.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Data
@RequiredArgsConstructor
public class FollowTwit {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	@JsonIgnore
	@ManyToMany(cascade= CascadeType.DETACH)
	private List<User> followingssub = new ArrayList<>();
	
//	@JsonIgnore
//	@ManyToMany(cascade=CascadeType.DETACH)
//	private List<Twit> followingstwit = new ArrayList<>();
	
}

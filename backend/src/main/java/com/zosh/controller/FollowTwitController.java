package com.zosh.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.TwitDto;
import com.zosh.dto.mapper.TwitDtoMapper;
import com.zosh.exception.FollowTwitException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.FollowTwit;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.service.FollowTwitService;
import com.zosh.service.UserService;

@RestController
@RequestMapping("/api/followtwit")
public class FollowTwitController {
	private FollowTwitService followTwitService;
	private UserService userService;
	
	public FollowTwitController(FollowTwitService followTwitService, UserService userService) {
		this.followTwitService = followTwitService;
		this.userService = userService;
	}
	
	
	@GetMapping("/")
	public ResponseEntity<List<TwitDto>> getUserFollowTwit(@RequestHeader("Authorization") String jwt) throws UserException, TwitException, FollowTwitException{
		User reqUser=userService.findUserProfileByJwt(jwt);
		FollowTwit followTwit = followTwitService.findFollowTwitByUserId(reqUser.getId());
		System.out.println("followTwit + " + followTwit);
		List<Twit> twits=followTwitService.findTwitFollowedByFollowTwit(followTwit.getId());		
		System.out.println("twits + " + twits);
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits, reqUser);
		for (int i = 0; i < twitDtos.size(); i++) {
			System.out.println("twitDtos + " + twitDtos.get(i).getReplyTwits());
		}
		return new ResponseEntity<List<TwitDto>>(twitDtos, HttpStatus.OK);
		
	}
}

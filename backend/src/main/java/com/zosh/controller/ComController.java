package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ComDto;
import com.zosh.dto.mapper.ComDtoMapper1;
import com.zosh.exception.ComException;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.User;
import com.zosh.service.ComService;
import com.zosh.service.UserService;

@RestController
@RequestMapping("/api/communities")
public class ComController {

private ComService comService;
private UserService userService;

	public ComController(ComService comService, UserService userService) {
		this.comService=comService;
		this.userService=userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<ComDto> createCom(@RequestBody Community req,
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user= userService.findUserProfileByJwt(jwt);
		Community community=comService.createCom(req, user);
		ComDto comDto=ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<ComDto>> getAllComs(@RequestHeader("Authorization") String jwt) 
			throws ComException, UserException{
		
		User reqUser=userService.findUserProfileByJwt(jwt);
		List<Community> communities = comService.findAllCom(reqUser);
		List<ComDto> comDtos=ComDtoMapper1.toComDtos(communities,reqUser);
		return new ResponseEntity<>(comDtos, HttpStatus.ACCEPTED);
	}
	
}

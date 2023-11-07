package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ComDto;
import com.zosh.dto.ListDto;
import com.zosh.dto.UserDto;
import com.zosh.dto.mapper.ComDtoMapper1;
import com.zosh.dto.mapper.ListDtoMapper;
import com.zosh.dto.mapper.UserDtoMapper;
import com.zosh.exception.ComException;
import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.ListModel;
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
			throws ComException, UserException {
		System.out.println("jwt reqUser check");
		User reqUser=userService.findUserProfileByJwt(jwt);
		List<Community> communities = comService.findAllCom(reqUser);
		List<ComDto> comDtos=ComDtoMapper1.toComDtos(communities, reqUser);
		return new ResponseEntity<>(comDtos, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/{comId}/add2/{userId}")
	public ResponseEntity<ComDto> addUserHandler2(@PathVariable Long userId,
			@PathVariable Long comId,
			@RequestHeader("Authorization") String jwt)
		throws ListException, UserException, ComException {
		User user= userService.findUserProfileByJwt(jwt);
		User updatedUser = userService.followCom(userId, comId);
		Community updatedCom=comService.addUser(userId, comId);
		ComDto comDto=ComDtoMapper1.toComDto(updatedCom, user);
		UserDto userDto=UserDtoMapper.toUserDto(updatedUser);
		return new ResponseEntity<>(comDto, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/update")
	public ResponseEntity<ComDto> editCom(@RequestBody Community req,
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Community community = comService.editCom(req, user);
		ComDto comDto = ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto, HttpStatus.OK);
	}
	
	@PostMapping("/{comId}/signup")
	public ResponseEntity<ComDto> signupCom(@RequestBody Long comId, 
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Community community = comService.addUserSignUp(comId, user);
		ComDto comDto = ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto,HttpStatus.OK);
	}
	
	@GetMapping("/{comId}")
	public ResponseEntity<ComDto> findComById(@PathVariable Long comId,
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Community community = comService.findById(comId);
		ComDto comDto = ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto, HttpStatus.ACCEPTED);
	}
	
}

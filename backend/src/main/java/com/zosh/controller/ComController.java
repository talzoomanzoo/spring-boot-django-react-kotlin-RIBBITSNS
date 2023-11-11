package com.zosh.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ComDto;
import com.zosh.dto.UserDto;
import com.zosh.dto.mapper.ComDtoMapper1;
import com.zosh.dto.mapper.UserDtoMapper;
import com.zosh.exception.ComException;
import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.User;
import com.zosh.response.ApiResponse;
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
		System.out.println("================");
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
	
	@PostMapping("/{comId}/add2/{userId}") // 가입자 추가 및 삭제
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
	
	@PostMapping("/{comId}/signup") // 가입신청
	public ResponseEntity<ComDto> signupCom(@PathVariable Long comId, 
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Community community = comService.addUserSignUp(comId, user);
		ComDto comDto = ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto,HttpStatus.OK);
	}
	
	
	@PostMapping("/{comId}/signupok/{userId}") // 가입승인
	public ResponseEntity<ComDto> signupComOk(@PathVariable Long comId, @PathVariable Long userId, 
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Community community = comService.addUserSignUpOk(comId, userId, user);
		ComDto comDto = ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto, HttpStatus.OK);
	}
	
	@PostMapping("/{comId}/signout")
	public ResponseEntity<ComDto> signOut(@PathVariable Long comId, @RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Community community = comService.signoutUser(comId, user);
		ComDto comDto = ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto, HttpStatus.OK);
	}
	
	@GetMapping("/{comId}")
	public ResponseEntity<ComDto> findComById(@PathVariable Long comId,
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		Community community = comService.findById(comId);
		ComDto comDto = ComDtoMapper1.toComDto(community, user);
		return new ResponseEntity<>(comDto, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/{comId}")
	public ResponseEntity<ApiResponse> deleteComById(@PathVariable Long comId,
			@RequestHeader("Authorization") String jwt) throws ComException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		comService.deleteComById(comId, user.getId());
		
		ApiResponse res = new ApiResponse();
		res.setMessage("list deleted successfully");
		res.setStatus(true);
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
}

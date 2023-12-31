package com.zosh.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.ListDto;
import com.zosh.dto.UserDto;
import com.zosh.dto.mapper.ListDtoMapper;
import com.zosh.dto.mapper.UserDtoMapper;
import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.ListModel;
import com.zosh.model.User;
import com.zosh.repository.UserRepository;
import com.zosh.response.ApiResponse;
import com.zosh.service.ListService;
import com.zosh.service.UserService;
import com.zosh.util.ListUtil;

@RestController
@RequestMapping("/api/lists")
public class ListController {
@Autowired
private ListService listService;
@Autowired
private UserService userService;
@Autowired
private UserRepository userRepository;

	public ListController(ListService listService, UserService userService) {
		this.listService=listService;
		this.userService=userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<ListDto> createList(@RequestBody ListModel req,
			@RequestHeader("Authorization") String jwt) throws ListException, UserException {
		User user= userService.findUserProfileByJwt(jwt);
		ListModel listModel=listService.createList(req, user);
		ListDto listDto=ListDtoMapper.toListDto(listModel, user);
		System.out.println("check" + user.getId());
		return new ResponseEntity<>(listDto, HttpStatus.CREATED);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<ListDto>> getAllPublicLists(@RequestHeader("Authorization") String jwt) 
			throws ListException, UserException{
		
		User reqUser=userService.findUserProfileByJwt(jwt);
		//List<ListModel> listModels= listService.findAllPublicListByReqUser(reqUser);
		List<ListModel> listModels = listService.findAllList(reqUser);
		List<ListDto> listDtos=ListDtoMapper.toListDtos(listModels,reqUser);
		return new ResponseEntity<>(listDtos, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/update")
	public ResponseEntity<ListDto> editList(@RequestBody ListModel req,
		@RequestHeader("Authorization") String jwt) throws ListException, UserException {
		User user= userService.findUserProfileByJwt(jwt);
		ListModel listModel= listService.editList(req, user);
		ListDto listDto =ListDtoMapper.toListDto(listModel, user);
		return new ResponseEntity<>(listDto, HttpStatus.OK);
	}
	
	@PostMapping("/{listId}/add1/{userId}")
	public ResponseEntity<ListDto> addUserHandler1(@PathVariable Long userId,
			@PathVariable Long listId,
			@RequestHeader("Authorization") String jwt)
		throws ListException, UserException {
		User user= userService.findUserProfileByJwt(jwt);
		User updatedUser = userService.followList(userId, listId);
		ListModel updatedList=listService.addUser(userId, listId);
		ListDto listDto=ListDtoMapper.toListDto(updatedList, user);
		UserDto userDto=UserDtoMapper.toUserDto(updatedUser);
		userDto.setHasFollowedLists(ListUtil.isFollowedByReqList(updatedList, updatedUser));
		return new ResponseEntity<>(listDto, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{listId}/getUsers")
	public ResponseEntity<ListDto> getUserList(@PathVariable Long listId,
			@RequestHeader("Authorization") String jwt) throws ListException, UserException {
		User user= userService.findUserProfileByJwt(jwt);
		ListModel listModel=listService.findById(listId);
		ListDto listDto=ListDtoMapper.toListDto(listModel, user);
		System.out.println("listDto followers + " + listDto.getFollowingsl());
		return new ResponseEntity<>(listDto, HttpStatus.OK);
	}
	
	@DeleteMapping("/{listId}")
	public ResponseEntity<ApiResponse> deleteListById(@PathVariable Long listId,
			@RequestHeader("Authorization") String jwt) throws ListException, UserException {
		
		User user = userService.findUserProfileByJwt(jwt);
		listService.deleteListById(listId, user.getId());
		
		ApiResponse res = new ApiResponse();
		res.setMessage("list deleted successfully");
		res.setStatus(true);
		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/{listId}")
	public ResponseEntity<ListDto> findListById(@PathVariable Long listId,
			@RequestHeader("Authorization") String jwt) throws ListException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		ListModel listModel = listService.findById(listId);
		ListDto listDto = ListDtoMapper.toListDto(listModel, user);
		return new ResponseEntity<>(listDto, HttpStatus.ACCEPTED);
	}
	
	@PostMapping("/{listId}/setPrivate")
	public ResponseEntity<ListDto> setPrivate(@PathVariable Long listId, 
			@RequestHeader("Authorization") String jwt) throws ListException, UserException {
		User user = userService.findUserProfileByJwt(jwt);
		ListModel listModel=listService.setPrivateById(listId);
		ListDto listDto=ListDtoMapper.toListDto(listModel, user);
		System.out.println("======================");
		return new ResponseEntity<>(listDto, HttpStatus.CREATED);
	}
	
	
}

package com.zosh.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zosh.dto.TwitDto;
import com.zosh.dto.mapper.TwitDtoMapper;
import com.zosh.exception.ListException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.ListModel;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.request.TwitReplyRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.ListService;
import com.zosh.service.TwitService;
import com.zosh.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/twits")
@Tag(name="Twit Management", description = "Endpoints for managing twits")
public class TwitController {
	
	private TwitService twitService;
	private UserService userService;
	private ListService listService;
	
	public TwitController(TwitService twitService,UserService userService) {
		this.twitService=twitService;
		this.userService=userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<TwitDto> createTwit(@RequestBody Twit req, 
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		System.out.println("content + "+req);
		User user=userService.findUserProfileByJwt(jwt);
		Twit twit=twitService.createTwit(req, user);
		//System.out.println("edit + "+req.isEdited()+req.getEditedAt());
		TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
		
		return new ResponseEntity<>(twitDto,HttpStatus.CREATED);
	}
	
	
	@PostMapping("/reply")
	public ResponseEntity<TwitDto> replyTwit(@RequestBody TwitReplyRequest req, 
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		
		User user=userService.findUserProfileByJwt(jwt);
		Twit twit=twitService.createReply(req, user);
		TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
		
		return new ResponseEntity<>(twitDto,HttpStatus.CREATED);
	}
	
	@PostMapping("/edit")
	public ResponseEntity<TwitDto> editTwit(@RequestBody Twit req, 
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		
		User user=userService.findUserProfileByJwt(jwt);
		Twit twit=twitService.editTwit(req, user);
		
		TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
		
		return new ResponseEntity<>(twitDto,HttpStatus.OK);
	}
	
	@PutMapping("/{twitId}/retwit")
	public ResponseEntity<TwitDto> retwit( @PathVariable Long twitId,
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		User user=userService.findUserProfileByJwt(jwt);
		
		Twit twit=twitService.retwit(twitId, user);
		
		TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
		
		return new ResponseEntity<>(twitDto,HttpStatus.OK);
	}
	
	@DeleteMapping("/{twitId}")
	public ResponseEntity<ApiResponse> deleteTwitById( @PathVariable Long twitId,
		@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		User user=userService.findUserProfileByJwt(jwt);
		
		twitService.deleteTwitById(twitId, user.getId());
		
		ApiResponse res=new ApiResponse();
		res.setMessage("twit deleted successfully");
		res.setStatus(true);
		
		return new ResponseEntity<>(res,HttpStatus.OK);
		
	}
	
	@GetMapping("/{twitId}")
	public ResponseEntity<TwitDto> findTwitById( @PathVariable Long twitId, 
			@RequestHeader("Authorization") String jwt) throws TwitException, UserException{
		User user=userService.findUserProfileByJwt(jwt);
		Twit twit=twitService.findById(twitId);
		
		TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
		
		return new ResponseEntity<>(twitDto,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{listId}/listTwit") // ambiguous handler
	public void findTwitByListId (@PathVariable Long listId,
			@RequestHeader("Authorization") String jwt) throws TwitException, ListException, UserException {
		User user= userService.findUserProfileByJwt(jwt);
		ListModel listModel=listService.findById(listId);
		System.out.println("-----------------------------");
	}
	
	@GetMapping("/")
	public ResponseEntity<List<TwitDto>> findAllTwits(@RequestHeader("Authorization") String jwt) throws UserException{
		User user=userService.findUserProfileByJwt(jwt);
		List<Twit> twits=twitService.findAllTwit();
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits,user);
		return new ResponseEntity<List<TwitDto>>(twitDtos,HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}")
	public ResponseEntity<List<TwitDto>> getUsersTwits(@PathVariable Long userId,
			@RequestHeader("Authorization") String jwt) 
			throws UserException{
		User reqUser=userService.findUserProfileByJwt(jwt);
		User user=userService.findUserById(userId);
		List<Twit> twits=twitService.getUsersTwit(user);
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits,reqUser);
		return new ResponseEntity<List<TwitDto>>(twitDtos,HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/replies")
	public ResponseEntity<List<TwitDto>> getUsersReplies(@PathVariable Long userId,
			@RequestHeader("Authorization") String jwt)
			throws UserException{
		User reqUser=userService.findUserProfileByJwt(jwt);
		List<Twit> twits=twitService.getUsersReplies(userId);
		System.out.println("reply check controller"+ userId);
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits, reqUser);
		return new ResponseEntity<List<TwitDto>>(twitDtos,HttpStatus.OK);
	}
	
	@GetMapping("/user/{userId}/likes")
	public ResponseEntity<List<TwitDto>> findTwitByLikesContainsUser(@PathVariable Long userId,
			@RequestHeader("Authorization") String jwt) 
			throws UserException{
		User reqUser=userService.findUserProfileByJwt(jwt);
		User user=userService.findUserById(userId);
		List<Twit> twits=twitService.findByLikesContainsUser(user);
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits,reqUser);
		return new ResponseEntity<List<TwitDto>>(twitDtos,HttpStatus.OK);
	}

	@PostMapping({"/{twitId}/count"})
	public ResponseEntity<TwitDto> count(@PathVariable Long twitId, @RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserProfileByJwt(jwt);
		Twit twit = twitService.findById(twitId);
		twit.setViewCount(twit.getViewCount()+1);
		twitService.updateView(twit);
		TwitDto twitDto = TwitDtoMapper.toTwitDto(twit, user);
		return new ResponseEntity<>(twitDto, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/followTwit")
	public ResponseEntity<List<TwitDto>> getUserFollowTwit(@RequestHeader("Authorization") String jwt) throws UserException{
		User reqUser=userService.findUserProfileByJwt(jwt);
		System.out.println("reqUser + " + reqUser);
		List<Twit> twits=twitService.findTwitFollowedByReqUser(reqUser);
		System.out.println("twits + " + twits);
		
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits, reqUser);
		return new ResponseEntity<>(twitDtos, HttpStatus.ACCEPTED);
		
	} 
	
	@GetMapping("/search2")
	public ResponseEntity<List<TwitDto>> searchTwitHandler(@RequestParam String query, 
			@RequestHeader("Authorization") String jwt) 
			throws UserException{
		
		User reqUser=userService.findUserProfileByJwt(jwt);
		
		List<Twit> twits=twitService.searchTwit(query);
		
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits, reqUser);
		
		return new ResponseEntity<>(twitDtos,HttpStatus.ACCEPTED);
	}
	

	@GetMapping("/toplikes")
	public ResponseEntity<List<TwitDto>> findTwitsByTopLikes(@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		User user=userService.findUserProfileByJwt(jwt);
		List<Twit> twits=twitService.findTwitsByTopLike();
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits,user);
		return new ResponseEntity<List<TwitDto>>(twitDtos,HttpStatus.OK);
	}
	
	@GetMapping("/topviews")
	public ResponseEntity<List<TwitDto>> findTwitsByTopViews(@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		User user=userService.findUserProfileByJwt(jwt);
		List<Twit> twits=twitService.findTwitsByTopView();
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits,user);
		return new ResponseEntity<List<TwitDto>>(twitDtos,HttpStatus.OK);
	}
}

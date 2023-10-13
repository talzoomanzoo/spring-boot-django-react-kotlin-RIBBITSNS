package com.zosh.controller;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
import org.springframework.web.client.RestTemplate;

import com.zosh.dto.TwitDto;
import com.zosh.dto.mapper.TwitDtoMapper;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.request.TwitReplyRequest;
import com.zosh.response.ApiResponse;
import com.zosh.service.TwitService;
import com.zosh.service.UserService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.ManyToOne;

@RestController
@RequestMapping("/api/twits")
@Tag(name="Twit Management", description = "Endpoints for managing twits")
public class TwitController {
	
	private TwitService twitService;
	private UserService userService;
	
	public TwitController(TwitService twitService,UserService userService) {
		this.twitService=twitService;
		this.userService=userService;
	}
	
	@PostMapping("/create")
	public ResponseEntity<TwitDto> createTwit(@RequestBody Twit req, 
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		try {
			System.out.println("content + "+req.getContent());
			
			User user=userService.findUserProfileByJwt(jwt);
			Twit twit=twitService.createTwit(req, user);
			System.out.println("twit: "+twit);
			System.out.println("edit + "+req.isEdited()+req.getEditedAt());
			TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
			
			//비동기를 적용했다. content를 views.py의 nlp함수로 보내서 윤리수치 분석을 진행한다.
			CompletableFuture<ResponseEntity<TwitDto>> ethicfuture = CompletableFuture.supplyAsync(()->{
				String url = "http://localhost:8000/ethic/";
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				String request = "{\"text\": \"" + req.getContent() + "\"}";
				System.out.println("request: "+request);
				HttpEntity<String> entity = new HttpEntity<>(request,headers);
				ResponseEntity<String> responseEntity = restTemplate.exchange(
					url, 
					HttpMethod.POST, 
					entity, 
					String.class
				);
				System.out.println("예측결과: "+responseEntity.getBody());
				String ethicrate = responseEntity.getBody();
				
				//윤리수치가 나오면 해당 sns게시물의 행의 ethicrate에 저장된다.
				Twit ethictwit = null;
				try {
					ethictwit = twitService.inputethic(twit.getId(), ethicrate);
				} catch (TwitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				TwitDto ethicdto = TwitDtoMapper.toTwitDto(ethictwit, user);
				
				System.out.println("게시물 id: "+req.getId());
				System.out.println("ethicdto: "+ethicdto.getEthicrate());
				return new ResponseEntity<>(ethicdto,HttpStatus.OK);
			});
			
			//단 ethicrate가 나오기까지 시간이 걸려 ethicrate가 나오면 그때 sns내용을 db로 전송한다.
			CompletableFuture<ResponseEntity<TwitDto>> twitdtoFuture = CompletableFuture.completedFuture(
				new ResponseEntity<>(twitDto,HttpStatus.CREATED)
			);
			
			CompletableFuture<ResponseEntity<TwitDto>> combinedFuture = ethicfuture.thenCombine(twitdtoFuture, (ethicResponse, twitResponse) -> {
	            return twitResponse;
	        });
			System.out.println("cf: "+combinedFuture.get());
			
			return combinedFuture.get();
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<TwitDto>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping("/request")
	public ResponseEntity<TwitDto> requestethic( @RequestBody Long twitId, 
			@RequestHeader("Authorization") String jwt) throws TwitException, UserException{
		User user=userService.findUserProfileByJwt(jwt);
		Twit twit=twitService.findById(twitId);
		
		TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
		
		return new ResponseEntity<>(twitDto,HttpStatus.ACCEPTED);
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
	
	@GetMapping("/{twitId}")
	public ResponseEntity<TwitDto> findTwitById( @PathVariable Long twitId, 
			@RequestHeader("Authorization") String jwt) throws TwitException, UserException{
		User user=userService.findUserProfileByJwt(jwt);
		Twit twit=twitService.findById(twitId);
		
		TwitDto twitDto=TwitDtoMapper.toTwitDto(twit,user);
		
		return new ResponseEntity<>(twitDto,HttpStatus.ACCEPTED);
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
	
	@GetMapping("/search2")
	public ResponseEntity<List<TwitDto>> searchTwitHandler(@RequestParam String query, 
			@RequestHeader("Authorization") String jwt) 
			throws UserException{
		
		User reqUser=userService.findUserProfileByJwt(jwt);
		
		List<Twit> twits=twitService.searchTwit(query);
		
		List<TwitDto> twitDtos=TwitDtoMapper.toTwitDtos(twits, reqUser);
		
		return new ResponseEntity<>(twitDtos,HttpStatus.ACCEPTED);
	}
	

}

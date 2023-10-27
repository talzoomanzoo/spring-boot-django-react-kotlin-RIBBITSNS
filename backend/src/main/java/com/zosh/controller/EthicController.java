package com.zosh.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.zosh.dto.TwitDto;
import com.zosh.dto.mapper.TwitDtoMapper;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.service.TwitService;
import com.zosh.service.UserService;

@RestController
@RequestMapping("/api/ethic")
public class EthicController {
	
	@Autowired
	private TwitService twitService;
	
	@Autowired
	private UserService userService;
	
	@PostMapping("/reqsentence")
	public ResponseEntity<TwitDto> reqsentence(@RequestBody Twit req,
			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
		
		User user=userService.findUserProfileByJwt(jwt);
		
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
			ethictwit = twitService.inputethic(req.getId(), ethicrate);
		} catch (TwitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TwitDto ethicdto = TwitDtoMapper.toTwitDto(ethictwit, user);
		
		System.out.println("게시물 id: "+req.getId());
		System.out.println("ethicdto: "+ethicdto.getEthicrate());
		System.out.println("ethicdto sentence: "+ethicdto.getSentence());
		return new ResponseEntity<>(ethicdto,HttpStatus.OK);

	}
}

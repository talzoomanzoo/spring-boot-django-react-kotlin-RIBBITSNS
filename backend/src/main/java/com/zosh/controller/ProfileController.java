package com.zosh.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ProfileController {
	
	private String image_url;

	//키워드를 입력하면 views.py의 making_image함수로 전송해 키워드에 맞는 webp url을 전송한다.
	@GetMapping("/sendprompt")	
	public String keyword(@RequestParam String keyword){
        String url = "http://localhost:8000";
        
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> entity = restTemplate.getForEntity(url+"?keyword="+keyword, String.class);
        
        String str = entity.getBody();
        ObjectMapper mapper = new ObjectMapper();
        try {
			JsonNode node = mapper.readTree(str);
			String image = node.get("image_url").asText();
			image_url=image;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return entity.getBody();
	}
	
	//이미지 다운로드를 누르면 webp사진을 jpg로 변환해 다운로드 한다.
	@GetMapping("/download")
	public ResponseEntity<byte[]> download(){
        
		try {
			RestTemplate restTemplate = new RestTemplate();
			ResponseEntity<byte[]> response = restTemplate.getForEntity(new URI(image_url), byte[].class);
			
			HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            headers.setContentLength(response.getBody().length);
            headers.setContentDispositionFormData("attachment", "generated_image.jpg");

            return new ResponseEntity<>(response.getBody(), headers, HttpStatus.OK);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return new ResponseEntity<byte[]>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	//이미지 선택을 누르면 webp url을 views.py의 webptojpg로 전송해 webp를 jpg로 변환하고 cloudinary로 전송한다.
	@PostMapping("/webptojpg")
	public ResponseEntity<byte[]> webptojpg(@RequestBody String karlourl) {
	    try {
	        RestTemplate restTemplate = new RestTemplate();
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.IMAGE_JPEG); // Content-Type을 이미지 형식으로 설정

	        ResponseEntity<byte[]> responseEntity = restTemplate.postForEntity(
	            "http://localhost:8000/webptojpg/",
	            karlourl,
	            byte[].class
	        );

	        // 스프링 부트에서 이미지 데이터를 받아온 후, 그대로 클라이언트에게 리턴
	        HttpHeaders responseHeaders = new HttpHeaders();
	        responseHeaders.setContentType(MediaType.IMAGE_JPEG);
	        return new ResponseEntity<>(responseEntity.getBody(), responseHeaders, HttpStatus.OK);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.badRequest().build();
	    }
	}
}

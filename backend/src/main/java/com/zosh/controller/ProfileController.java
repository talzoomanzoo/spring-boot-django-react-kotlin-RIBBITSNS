package com.zosh.controller;

import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class ProfileController {
	
	private String image_url;

	@GetMapping("/sendprompt")	
	public String keyword(/*@RequestParam String keyword*/){
        String url = "http://localhost:8000";
        
        String keyword = "castle";
        
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
        
        System.out.println(image_url);
        
        return entity.getBody();
	}
	
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
}

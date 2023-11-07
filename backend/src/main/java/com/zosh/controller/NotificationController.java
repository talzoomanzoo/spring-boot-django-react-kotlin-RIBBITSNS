//package com.zosh.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.zosh.dto.NotificationDto;
//import com.zosh.dto.mapper.NotificationDtoMapper;
//import com.zosh.exception.NotificationException;
//import com.zosh.exception.TwitException;
//import com.zosh.exception.UserException;
//import com.zosh.model.Notification;
//import com.zosh.model.User;
//import com.zosh.service.NotificationsService;
//import com.zosh.service.UserService;
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//
//@RestController
//@RequestMapping("/api")
//@Tag(name="Notification-UnNotification Twit")
//public class NotificationController {
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private NotificationsService notificationService;
//
//	
//	public NotificationController(UserService userService,  NotificationsService notificationService) {
//		this.userService=userService;
//		this.notificationService=notificationService;
//	}
//	
//	@PostMapping("/{twitId}/notification")
//	public ResponseEntity<NotificationDto>notificationTwit(
//			@PathVariable Long twitId, 
//			@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
//		
//		User user=userService.findUserProfileByJwt(jwt);
//		Notification notification =notificationService.notificationTwit(twitId, user);
//		
//		NotificationDto notificationDto=NotificationDtoMapper.toNotificationDto(notification,user);
//		
//		return new ResponseEntity<>(notificationDto,HttpStatus.CREATED);
//	}
//	@DeleteMapping("/{twitId}/unnotification")
//	public ResponseEntity<NotificationDto>unnotificationTwit(
//			@PathVariable Long twitId, 
//			@RequestHeader("Authorization") String jwt) throws UserException, TwitException, NotificationException{
//		
//		User user=userService.findUserProfileByJwt(jwt);
//		Notification notification =notificationService.notificationTwit(twitId, user);
//		
//		
//		NotificationDto notificationDto=NotificationDtoMapper.toNotificationDto(notification,user);
//		return new ResponseEntity<>(notificationDto,HttpStatus.CREATED);
//	}
//	
//	@GetMapping("/notification/{twitId}")
//	public ResponseEntity<List<NotificationDto>>getAllNotification(
//			@PathVariable Long twitId,@RequestHeader("Authorization") String jwt) throws UserException, TwitException{
//		User user=userService.findUserProfileByJwt(jwt);
//		
//		List<Notification> notifications =notificationService.getAllNotifications(twitId);
//		
//		List<NotificationDto> notificationDtos=NotificationDtoMapper.toNotificationDtos(notifications,user);
//		
//		return new ResponseEntity<>(notificationDtos,HttpStatus.CREATED);
//	}
//}
//

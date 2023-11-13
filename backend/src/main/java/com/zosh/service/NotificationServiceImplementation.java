package com.zosh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zosh.exception.NotificationException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Notification;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.repository.NotificationRepository;
import com.zosh.repository.TwitRepository;

@Service
public class NotificationServiceImplementation implements NotificationsService {

	private NotificationRepository notificationRepository;
	private TwitService twitService;
	private TwitRepository twitRepository;
	
	public NotificationServiceImplementation(
			NotificationRepository notificationRepository,
			TwitService twitService,
			TwitRepository twitRepository) {
		this.notificationRepository=notificationRepository;
		this.twitService=twitService;
		this.twitRepository=twitRepository;
	}

	@Override
	public Notification notificationTwit(Long twitId, User user) throws UserException, TwitException, NotificationException{
		
//		Notification isNotificationExist=notificationRepository.isNotificationExist(user.getId(), twitId);
		
//		if(isNotificationExist!=null) {
//			notificationRepository.deleteById(isNotificationExist.getId());
//			return isNotificationExist;
//		}
		
		Twit twit=twitService.findById(twitId);
		Notification notification=new Notification();
		if (twit.getUser() != user) {
		notification.setTwit(twit);
		notification.setUser(user);
		
		notificationRepository.save(notification);
		
		
		twit.getNotifications().add(notification);
		twitRepository.save(twit);
		
		}
		return notification; 
	}

	@Override
	public Notification unnotificationTwit(Long twitId, User user) throws UserException, TwitException, NotificationException {
		Notification notification=notificationRepository.findById(twitId).orElseThrow(()->new NotificationException("Notification Not Found"));
		
		if(notification.getUser().getId().equals(user.getId())) {
			throw new UserException("somthing went wrong...");
		}
		
		notificationRepository.deleteById(notification.getId());
		return notification;
	}

	@Override
	public List<Notification> getAllNotifications(Long userId) throws UserException, TwitException, NotificationException{
		//Twit twit=twitService.findById(userId);
		
		List<Notification> notifications=notificationRepository.findByUserId(userId);
		return notifications;
	}
}

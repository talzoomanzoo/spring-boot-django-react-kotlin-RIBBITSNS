//package com.zosh.service;
//
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import com.zosh.exception.NotificationException;
//import com.zosh.exception.TwitException;
//import com.zosh.exception.UserException;
//import com.zosh.model.Notification;
//import com.zosh.model.Twit;
//import com.zosh.model.User;
//import com.zosh.repository.NotificationRepository;
//import com.zosh.repository.TwitRepository;
//
//@Service
//public class NotificationServiceImplementation implements NotificationsService {
//
//	private NotificationRepository notificationRepository;
//	private TwitService twitService;
//	private TwitRepository twitRepository;
//	
//	public NotificationServiceImplementation(
//			NotificationRepository notificationRepository,
//			TwitService twitService,
//			TwitRepository twitRepository) {
//		this.notificationRepository=notificationRepository;
//		this.twitService=twitService;
//		this.twitRepository=twitRepository;
//	}
//
//	@Override
//	public Notification notificationTwit(Long twitId, User user) throws UserException, TwitException {
//		
//		Notification isNotificationExist=notificationRepository.isNotificationExist(user.getId(), twitId);
//		
//		if(isNotificationExist!=null) {
//			notificationRepository.deleteById(isNotificationExist.getId());
//			return isNotificationExist;
//		}
//		
//		Twit twit=twitService.findById(twitId);
//		Notification notification=new Notification();
//		notification.setTwit(twit);
//		notification.setUser(user);
//		
//		Notification savedNotification=notificationRepository.save(notification);
//		
//		
//		twit.getNotifications().add(savedNotification);
//		twitRepository.save(twit);
//		
//		return savedNotification;
//	}
//
//	@Override
//	public Notification unnotificationTwit(Long twitId, User user) throws UserException, TwitException, NotificationException {
//		Notification notification=notificationRepository.findById(twitId).orElseThrow(()->new NotificationException("Notification Not Found"));
//		
//		if(notification.getUser().getId().equals(user.getId())) {
//			throw new UserException("somthing went wrong...");
//		}
//		
//		notificationRepository.deleteById(notification.getId());
//		return notification;
//	}
//
//	@Override
//	public List<Notification> getAllNotifications(Long twitId) throws TwitException {
//		Twit twit=twitService.findById(twitId);
//		
//		List<Notification> notifications=notificationRepository.findByTwitId(twit.getId());
//		return notifications;
//	}
//}

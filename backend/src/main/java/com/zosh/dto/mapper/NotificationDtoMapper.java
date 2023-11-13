package com.zosh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.zosh.dto.NotificationDto;
import com.zosh.dto.TwitDto;
import com.zosh.dto.UserDto;
import com.zosh.model.Notification;
import com.zosh.model.User;

public class NotificationDtoMapper {
    
    public static NotificationDto toNotificationDto(Notification notification,User reqUser) {
		
		UserDto user=UserDtoMapper.toUserDto(notification.getUser());
		UserDto reqUserDto=UserDtoMapper.toUserDto(reqUser);
		TwitDto twit =TwitDtoMapper.toTwitDto(notification.getTwit(),reqUser);
		
		NotificationDto notificationDto=new NotificationDto();
		notificationDto.setId(notification.getId());
		notificationDto.setTwit(twit);
		notificationDto.setUser(user);
		return notificationDto;
		
	}

	public static List<NotificationDto> toNotificationDtos(List<Notification> notifications,User reqUser) {
		
		List<NotificationDto>  notificationDtos=new ArrayList<>() ;
		
		for(Notification notification:notifications) {
			//UserDto user=UserDtoMapper.toUserDto(notification.getUser());
			//TwitDto twit =TwitDtoMapper.toTwitDto(notification.getTwit(),reqUser);
			
			NotificationDto notificationDto=new NotificationDto();
			notificationDto.setId(notification.getId());
			notificationDto.setTwit(TwitDtoMapper.toTwitDto(notification.getTwit(), reqUser));
			notificationDto.setUser(UserDtoMapper.toUserDto(notification.getUser()));
			
			notificationDtos.add(notificationDto);
		}
		
		
		
		return  notificationDtos;
		
	}

}

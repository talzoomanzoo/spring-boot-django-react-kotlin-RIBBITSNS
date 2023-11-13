package com.zosh.service;

import java.util.List;

import com.zosh.exception.NotificationException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Notification;
import com.zosh.model.User;

public interface NotificationsService {
    Notification notificationTwit(Long twitId, User user) throws UserException, TwitException, NotificationException;
    Notification unnotificationTwit(Long twitId, User user) throws UserException, TwitException, NotificationException;
    List<Notification> getAllNotifications(Long userId) throws UserException, TwitException, NotificationException;
}


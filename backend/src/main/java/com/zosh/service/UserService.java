package com.zosh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.User;

import io.swagger.v3.oas.annotations.servers.Server;

@Service
public interface UserService {

	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public User updateUser(Long userId,User user) throws UserException;
	
	public User followUser(Long userId,User user) throws UserException;
	
	public User followList(Long userId, Long listId) throws UserException, ListException;
	
	public List<User> searchUser(String query);
	
}

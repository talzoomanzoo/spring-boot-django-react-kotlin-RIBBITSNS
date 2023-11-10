package com.zosh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zosh.exception.ComException;
import com.zosh.exception.FollowTwitException;
import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.User;

@Service
public interface UserService {

	public User findUserById(Long userId) throws UserException;
	
	public User findUserProfileByJwt(String jwt) throws UserException;
	
	public User updateUser(Long userId,User user) throws UserException;
	
	public User followUser(Long userId,User user) throws UserException, FollowTwitException;
	
	public User followList(Long userId, Long listId) throws UserException, ListException;
	
	public User followCom(Long userId, Long comId) throws UserException, ComException;
	
	public List<User> searchUser(String query);
	
	public void deleteaccount(User user) throws UserException;//계정삭제
	
}

package com.zosh.service;

import java.util.List;

import com.zosh.exception.ComException;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.User;

public interface ComService {

	public Community createCom(Community req, User user) throws ComException, UserException;
	
	public List<Community> findAllCom(User reqUser) throws ComException, UserException;

	public Community addUser(Long userId, Long comId) throws ComException, UserException;
	
	public Community findById(Long comId) throws ComException;
	
	public Community editCom(Community req, User user) throws ComException, UserException;
	
	public Community addUserSignUp(Long comId, User user) throws ComException, UserException;
	
	public Community addUserSignUpOk(Long comId, Long userId, User user) throws ComException, UserException;
	
	public Community signoutUser(Long comId, User user) throws ComException, UserException;

	public void deleteComById(Long comId, Long id) throws ComException, UserException;
	
	public void deleteComWithUsers(Long comId) throws ComException;
}

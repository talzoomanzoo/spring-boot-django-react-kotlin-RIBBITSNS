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
}

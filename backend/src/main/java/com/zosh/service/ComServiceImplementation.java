package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zosh.exception.ComException;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.User;
import com.zosh.repository.ComRepository;
import com.zosh.repository.UserRepository;

@Service
public class ComServiceImplementation implements ComService{
	
	private ComRepository comRepository;
	private UserRepository userRepository;
	
	public ComServiceImplementation(ComRepository comRepository, UserRepository userRepository) {
		this.comRepository= comRepository;
		this.userRepository= userRepository;
	}

	@Override
	public Community createCom(Community req, User user) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = new Community();
		community.setUser(user);
		community.setCreatedAt(LocalDateTime.now());
		community.setComName(req.getComName());
		community.setDescription(req.getDescription());
		community.setBackgroundImage(req.getBackgroundImage());
		community.setPrivateMode(req.isPrivateMode());
		return comRepository.save(community);
	}

	@Override
	public List<Community> findAllCom(User reqUser) throws ComException, UserException {
		// TODO Auto-generated method stub
		return comRepository.findAllOrderByCreatedAtDesc();
	}

}

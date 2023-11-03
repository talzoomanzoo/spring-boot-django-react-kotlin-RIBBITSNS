package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zosh.exception.ComException;
import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.ListModel;
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
	
	public User findUserById(Long userId) throws UserException {
		User user = userRepository.findById(userId)
				.orElseThrow(() -> new UserException("user not found with id " + userId));
		return user;
	}
	
	@Override
	public Community findById(Long comId) throws ComException {
		// TODO Auto-generated method stub
		Community community = comRepository.findById(comId)
				.orElseThrow(() -> new ComException("Com Not Found with Id" + comId));
		return community;
	}


	@Override
	public Community addUser(Long userId, Long comId) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = findById(comId);
		User followToUser = findUserById(userId);
		if (community.getFollowings().contains(followToUser)) {
			community.getFollowings().remove(followToUser);
		} else {
			community.getFollowings().add(followToUser);
		}
		comRepository.save(community);
		return community;
	}

}

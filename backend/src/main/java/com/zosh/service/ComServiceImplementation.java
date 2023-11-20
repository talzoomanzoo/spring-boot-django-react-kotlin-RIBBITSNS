package com.zosh.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
		List<User> comfollowingsc = new ArrayList<>();
		comfollowingsc.add(user);
		community.setFollowingsc(comfollowingsc);
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


	@Override // 커뮤니티 사용자 추가 (관리자)
	public Community addUser(Long userId, Long comId) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = findById(comId);
		User followToUser = findUserById(userId);
		if (community.getFollowingsc().contains(followToUser)) {
			community.getFollowingsc().remove(followToUser);
		} else {
			community.getFollowingsc().add(followToUser);
		}
		comRepository.save(community);
		return community;
	}

	@Override // 커뮤니티 수정 (관리자)
	public Community editCom(Community req, User user) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = findById(req.getId());
		
		community.setBackgroundImage(req.getBackgroundImage());
		community.setDescription(req.getDescription());
		community.setComName(req.getComName());
		community.setPrivateMode(req.isPrivateMode());
		comRepository.save(community);
		
		return community;
	}

	@Override // 커뮤니티 가입 신청 및 취소
	public Community addUserSignUp(Long comId, User user) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = findById(comId);
		User followToUser = findUserById(user.getId());
		if (community.getFollowingscReady().contains(followToUser)) {
			community.getFollowingscReady().remove(followToUser);
		} else {
			community.getFollowingscReady().add(followToUser);
		}
		comRepository.save(community);
		return community;
	}

	@Override
	public Community addUserSignUpOk(Long comId, Long userId, User user) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = findById(comId);
		User followToUser = findUserById(userId);
		if (community.getUser() == user) {
			if (community.getFollowingscReady().contains(followToUser)) {
				community.getFollowingscReady().remove(followToUser);
				community.getFollowingsc().add(followToUser);
			} else {
				System.out.println("해당 사용자는 커뮤니티에 가입 신청을 하지 않았습니다."); }
			}
		else {
			System.out.println("해당 사용자는 커뮤니티에 접근할 수 없습니다.");
		}
		
		comRepository.save(community);
		return community;
	}

	@Override
	public Community signoutUser(Long comId, User user) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = findById(comId);
		User followToUser = findUserById(user.getId());
		if (community.getFollowingsc().contains(followToUser)) {
			community.getFollowingsc().remove(followToUser);
		} else {
			System.out.println("해당 사용자는 이미 커뮤니티에 가입되어 있지 않습니다.");
		}
		comRepository.save(community);
		return community;
	}

	@Override
	public void deleteComById(Long comId, Long id) throws ComException, UserException {
		// TODO Auto-generated method stub
		Community community = findById(comId);
		comRepository.deleteById(community.getId());
	}

	@Override
	public void deleteComWithUsers(Long comId) throws ComException {
		// TODO Auto-generated method stub
		Community community = findById(comId);
		List<User> users = community.getFollowingsc();
		
		for (User user : users) {
			user.getFollowedComs().remove(community);
		}
		
		community.getFollowingsc().clear();
		community.getFollowingscReady().clear();
		
		comRepository.save(community);
	}
	

}

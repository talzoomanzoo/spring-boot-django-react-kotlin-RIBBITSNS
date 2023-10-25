package com.zosh.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.zosh.config.JwtProvider;
import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.ListModel;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.repository.ListRepository;
import com.zosh.repository.TwitRepository;
import com.zosh.repository.UserRepository;

@Service
public class UserServiceImplementation implements UserService {
	
	private UserRepository userRepository;
	private ListRepository listRepository;
	private TwitRepository twitRepository;
	private JwtProvider jwtProvider;
	
	public UserServiceImplementation(
			UserRepository userRepository,
			ListRepository listRepository,
			TwitRepository twitRepository,
			JwtProvider jwtProvider) {
		
		this.userRepository=userRepository;
		this.listRepository=listRepository;
		this.twitRepository = twitRepository;
		this.jwtProvider=jwtProvider;
		
	}

	@Override
	public User findUserById(Long userId) throws UserException {
		User user=userRepository.findById(userId).orElseThrow(() ->  new UserException("user not found with id "+userId));
		return user;
	}

	@Override
	public User findUserProfileByJwt(String jwt) throws UserException {

		String email=jwtProvider.getEmailFromJwtToken(jwt);
		System.out.println("jwt: "+jwt);
		System.out.println("email: "+email);
		
		User user=userRepository.findByEmail(email);
		
		if(user==null) {
			throw new UserException("user not exist with email "+email);
		}
		System.out.println("email user: "+user.getEmail());
		return user;
	}

	@Override
	public User updateUser(Long userid,User req) throws UserException {
		
		User user=findUserById(userid);
		
		if(req.getFullName()!= null) {
			user.setFullName(req.getFullName());
		}
		if(req.getImage()!=null) {
			user.setImage(req.getImage());
		}
		if(req.getBackgroundImage()!=null) {
			user.setBackgroundImage(req.getBackgroundImage());
		}
		if(req.getBirthDate()!=null) {
			user.setBirthDate(req.getBirthDate());
		}
		if(req.getLocation()!=null) {
			user.setLocation(req.getLocation());
		}
		if(req.getBio()!=null) {
			user.setBio(req.getBio());
		}
		if(req.getWebsite()!=null) {
			user.setWebsite(req.getWebsite());
		}
		if(req.getEducation()!=null) {
			user.setEducation(req.getEducation());
		}
		
		return userRepository.save(user);
		
	}

	@Override
	public User followUser(Long userId, User user) throws UserException {
		User followToUser=findUserById(userId);
		
		if(user.getFollowings().contains(followToUser) && followToUser.getFollowers().contains(user)) {
			user.getFollowings().remove(followToUser);
			followToUser.getFollowers().remove(user);
		}
		else {
					followToUser.getFollowers().add(user);
					user.getFollowings().add(followToUser);
		}
		
		userRepository.save(user);
		userRepository.save(followToUser);
		return followToUser;
	}

	@Override
	public List<User> searchUser(String query) {
	
		return userRepository.searchUser(query);
	}

	public ListModel findById(Long listId) throws ListException {
		// TODO Auto-generated method stub
		ListModel listModel = listRepository.findById(listId)
				.orElseThrow(()-> new ListException("List Not Found with Id" + listId));
		return listModel;
	}
	@Override
	public User followList(Long userId, Long listId) throws UserException, ListException {
		// TODO Auto-generated method stub
		User user=findUserById(userId);
		ListModel listModel=findById(listId);
		if (user.getFollowedLists().contains(listModel) ) {
			user.getFollowedLists().remove(listModel);
		} else {
			user.getFollowedLists().add(listModel);
		}
		userRepository.save(user);
		return user;
	}

	@Override
	public void deleteaccount(User user) throws UserException {
		// TODO Auto-generated method stub
		List<Twit> retwittedtwits = twitRepository.findByRetwitUser(user);
		
		for(Twit twit: retwittedtwits) {
			twit.getRetwitUser().remove(user);
			twitRepository.save(twit);
		}
		
		//자신의 계정 삭제시 다른사람의 계정의 followers에 자신의 계정을 삭제
		List<User> followers = userRepository.findByFollowers(user);
		
		for(User follower: followers) {
			follower.getFollowers().remove(user);
			userRepository.save(follower);
		}
		
		//자신의 계정 삭제시 다른사람의 계정의 followings에 자신의 계정을 삭제
		List<User> followings = userRepository.findByFollowings(user);
		
		for(User following: followings) {
			following.getFollowings().remove(user);
			userRepository.save(following);
		}
		
		//자신의 계정 삭제시 Lists에서 자신의 followings 정보 삭제
//		List<ListModel> listfollowings = listRepository.findByFollowings(user);
//		System.out.println("listfollowings: "+listfollowings);
//		System.out.println("listrepos: "+listRepository.findByFollowings(user));
//		
//		for(ListModel listModel : listfollowings) {
//			System.out.println("Listmodel: "+listModel);
//			listModel.getFollowings().remove(user);
//			listRepository.save(listModel);
//		}
	
		userRepository.delete(user);
		
	}

}
   





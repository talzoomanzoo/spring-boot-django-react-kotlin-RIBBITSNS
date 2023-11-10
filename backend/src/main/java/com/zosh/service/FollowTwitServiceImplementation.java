package com.zosh.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.zosh.exception.FollowTwitException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.FollowTwit;
import com.zosh.model.Twit;
import com.zosh.repository.FollowTwitRepository;
import com.zosh.repository.TwitRepository;

@Service
public class FollowTwitServiceImplementation implements FollowTwitService{
	
	private TwitRepository twitRepository;
	private FollowTwitRepository followTwitRepository;
	
	public FollowTwitServiceImplementation(TwitRepository twitRepository, FollowTwitRepository followTwitRepository) {
		this.twitRepository = twitRepository;
		this.followTwitRepository=followTwitRepository;
	}

	@Override
	public List<Twit> findTwitFollowedByReqUser(Long userId) throws TwitException, UserException {
		// TODO Auto-generated method stub
		return twitRepository.findFollowTwitByreqUserId(userId);
	}

	@Override
	public FollowTwit findFollowTwitByUserId(Long userId) throws FollowTwitException {
		// TODO Auto-generated method stub
		return followTwitRepository.findByUserId(userId);
	}

	@Override
	public List<Twit> findTwitFollowedByFollowTwit(Long followTwitId) throws FollowTwitException, TwitException {
		// TODO Auto-generated method stub
		return twitRepository.findFollowTwitByFollowTwitId(followTwitId);
	}

}

package com.zosh.service;

import java.util.List;

import com.zosh.exception.FollowTwitException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.FollowTwit;
import com.zosh.model.Twit;

public interface FollowTwitService {

	public FollowTwit findFollowTwitByUserId(Long userId) throws FollowTwitException;
	
	public List<Twit> findTwitFollowedByReqUser(Long userId) throws TwitException, UserException;
	
	public List<Twit> findTwitFollowedByFollowTwit(Long followTwitId) throws FollowTwitException, TwitException;
}

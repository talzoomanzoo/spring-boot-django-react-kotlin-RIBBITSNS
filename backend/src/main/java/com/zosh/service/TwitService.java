package com.zosh.service;

import java.util.List;

import com.zosh.exception.ComException;
import com.zosh.exception.ListException;
import com.zosh.exception.TwitException;
import com.zosh.exception.UserException;
import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.request.TwitReplyRequest;

public interface TwitService {
	
	
	public Twit createTwit(Twit req,User user)throws UserException, TwitException;
	
	public Twit createComTwit(Twit req,Long comId, User user)throws UserException, TwitException, ComException;
	
	public Twit inputethic(Long twitId, String ethicrate) throws TwitException;
	//sns게시물에 윤리수치를 집어넣는 서비스이다.
	
	public List<Twit> findAllTwit();
	
	public Twit retwit(Long twitId, User user) throws UserException, TwitException;
	
	public Twit findById(Long twitId) throws TwitException;
	
	public void deleteTwitById(Long twitId,Long userId) throws TwitException, UserException;
	
	public Twit removeFromRetwit(Long twitId, User user) throws TwitException, UserException;
	
	public Twit createReply(TwitReplyRequest req,User user) throws TwitException;
	
	public Twit editTwit(Twit req,User user) throws UserException, TwitException;

	public void deleteReply(Long replyId, Long userId) throws TwitException, UserException;
	
	public List<Twit> getUsersTwit(User user);
	
	public List<Twit> getUsersRetwitTwit(User user);
	
	public List<Twit> getUsersReplies(Long userId);
	
	public List<Twit> findByLikesContainsUser(User user);

	public List<Twit> findTwitFollowedByReqUser(User user);

	public Twit updateView(Twit twit) throws TwitException;
	
	public List<Twit> searchTwit(String query);

	public List<Twit> findTwitsByListId(Long listId) throws ListException, UserException, TwitException;
		
	public List<Twit> findTwitsByTopLike() throws UserException, TwitException;

	public List<Twit> findTwitsByTopView() throws UserException, TwitException;

	public List<Twit> findTwitsByComId(Long comId) throws ComException;
	
	public List<Twit> findTwitsByAllComs(User user) throws UserException;
}

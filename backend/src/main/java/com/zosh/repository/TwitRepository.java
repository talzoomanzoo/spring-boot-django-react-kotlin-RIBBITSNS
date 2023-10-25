package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Twit;
import com.zosh.model.User;

public interface TwitRepository extends JpaRepository<Twit, Long> {

	List<Twit> findAllByIsTwitTrueOrderByCreatedAtDesc();
	List<Twit> findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(User user, Long userId);
	List<Twit> findByLikesContainingOrderByCreatedAtDesc(User user);
	
	@Query("SELECT t FROM Twit t WHERE t.id IN (SELECT t1.replyFor.id FROM Twit t1 WHERE t1.user.id=:userId AND t1.isReply=true)")
	List<Twit> findUsersReplies(Long userId);
	
	@Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId")
	List<Twit> findByLikesUser_Id(Long userId);
	
//  @Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId")
//  List<Twit> findTwitsByUserIdInLikes(Long userId);
	
	@Query("SELECT DISTINCT t FROM Twit t WHERE t.content LIKE %:query%")
	public List<Twit> searchTwit(@Param("query") String query);
	
	@Query("SELECT t FROM Twit t JOIN t.user u WHERE u.id IN (SELECT f.id FROM User u2 JOIN u2.followings f WHERE u2.id = :userId)")
	public List<Twit> searchFollowedTwit(Long userId);

}

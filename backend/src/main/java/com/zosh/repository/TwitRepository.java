package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Twit;
import com.zosh.model.User;

public interface TwitRepository extends JpaRepository<Twit, Long> {

	@Query(value = "select distinct t.* from twit t join likes l on t.id = l.twit_id where l.twit_id in (select * from (select distinct l2.twit_id from likes l2 group by l2.twit_id order by count(*) desc limit 3) as t2)", nativeQuery = true)
	public List<Twit> findTwitsByTopLike();

	@Query("SELECT t FROM Twit t ORDER BY t.viewCount DESC LIMIT 3")
	public List<Twit> findTwitsByTopView();
	
	@Query(value = "select distinct * from twit t join list_model_followingsl lmf on t.user_id = lmf.followingsl_id where list_model_id=:#{#listId} and is_twit=1", nativeQuery = true)
	public List<Twit> searchListFollowedTwit(Long listId);

	List<Twit> findAllByIsTwitTrueOrderByCreatedAtDesc();

	List<Twit> findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(User user, Long userId);

	List<Twit>findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByRetwitAtDesc(User user, Long userId);
	
	List<Twit> findByLikesContainingOrderByCreatedAtDesc(User user);

	@Query("SELECT t FROM Twit t WHERE t.id IN (SELECT t1.replyFor.id FROM Twit t1 WHERE t1.user.id=:userId AND t1.isReply=true)")
	List<Twit> findUsersReplies(Long userId);

	@Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId")
	List<Twit> findByLikesUser_Id(Long userId);

	// @Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId")
	// List<Twit> findTwitsByUserIdInLikes(Long userId);

	//@Query("SELECT DISTINCT t FROM Twit t WHERE t.content LIKE %:query%")
	@Query(value="select distinct t.* from twit t where t.content like ':#{#query}%'", nativeQuery=true)
	public List<Twit> searchTwit(@Param("query") String query);

	@Query("SELECT t FROM Twit t JOIN t.user u WHERE u.id IN (SELECT f.id FROM User u2 JOIN u2.followings f WHERE u2.id = :userId)")
	public List<Twit> searchFollowedTwit(Long userId);

	List<Twit> findByRetwitUser(User user);

}

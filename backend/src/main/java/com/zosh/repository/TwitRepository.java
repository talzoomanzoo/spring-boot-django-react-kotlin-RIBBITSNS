package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Twit;
import com.zosh.model.User;
import com.zosh.model.Community;

public interface TwitRepository extends JpaRepository<Twit, Long> {

	@Query(value = "select distinct t.* from twit t join likes l on t.id = l.twit_id where t.is_com= false and t.is_twit= true and l.twit_id in (select * from (select distinct l2.twit_id from likes l2 group by l2.twit_id order by count(*) desc limit 3) as t2)", nativeQuery = true)
	public List<Twit> findTwitsByTopLike();

	@Query("SELECT t FROM Twit t WHERE t.isCom= false and t.isTwit=true ORDER BY t.viewCount DESC LIMIT 3")
	public List<Twit> findTwitsByTopView();
	
	@Query(value = "select distinct * from twit t join list_model_followingsl lmf on t.user_id = lmf.followingsl_id where t.is_com= false and lmf.list_model_id=:#{#listId} and t.is_twit=1 ORDER BY t.created_at desc", nativeQuery = true)
	public List<Twit> searchListFollowedTwit(Long listId);

	List<Twit> findAllByIsTwitTrueAndIsComFalseOrderByCreatedAtDesc();
	
	@Query(value = "select t.* from twit t where user_id in (select ftf.follow_twit_id from follow_twit_followingssub ftf where ftf.followingssub_id = :#{#userId})", nativeQuery=true)
	public List<Twit> findFollowTwitByreqUserId(Long userId);
	
	@Query(value = "select t.* from twit t where user_id in (select ftf.followingssub_id from follow_twit_followingssub ftf where ftf.follow_twit_id = :#{#followTwitId})", nativeQuery= true)
	public List<Twit> findFollowTwitByFollowTwitId(Long followTwitId);
	
	public List<Twit> findByUser_IdAndIsTwitTrueAndIsComFalseOrderByCreatedAtDesc(Long userId);

	List<Twit> findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(User user, Long userId);

	List<Twit>findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByRetwitAtDesc(User user, Long userId);
	
	List<Twit> findByLikesContainingOrderByCreatedAtDesc(User user);

	@Query("SELECT t FROM Twit t WHERE t.isCom= false and t.id IN (SELECT t1.replyFor.id FROM Twit t1 WHERE t1.user.id=:userId AND t1.isReply=true)")
	List<Twit> findUsersReplies(Long userId);

	@Query("SELECT t FROM Twit t JOIN t.likes l WHERE t.isCom= false and l.user.id = :userId")
	List<Twit> findByLikesUser_Id(Long userId);

	// @Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId")
	// List<Twit> findTwitsByUserIdInLikes(Long userId);

	//@Query("SELECT DISTINCT t FROM Twit t WHERE t.content LIKE %:query%")
	@Query(value="select distinct t.* from twit t where t.is_com= false and t.content like  CONCAT('%',:query,'%')", nativeQuery=true)
	public List<Twit> searchTwit(@Param("query") String query);

	@Query(value="select distinct t.* from twit t where t.is_com= false and t.is_twit=true and t.user_id in (select followings_id from user_followings uf where uf.user_id=:#{#userId})", nativeQuery=true)
	public List<Twit> searchFollowedTwit(Long userId);

	List<Twit> findByRetwitUser(User user);
	
	@Query("SELECT t FROM Twit t JOIN t.community c WHERE c.id = :comId ORDER BY t.createdAt desc")
	public List<Twit> searchComFollowedTwit(Long comId);
	
	@Query(value="select distinct t.* from twit t join community c1 on t.com_id in (select c.id from community c join community_followingsc cf on c.id = cf.community_id where cf.followingsc_id = :#{#userId});", nativeQuery= true)
	public List<Twit> searchTwitsByAllComs(Long userId);

}

package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Twit;
import com.zosh.model.User;

public interface TwitRepository extends JpaRepository<Twit, Long> {

	// @Query("SELECT t FROM Twit t JOIN Like l ON t.id IN (SELECT l2.twit.id FROM
	// Like l2 GROUP BY l2.twit.id ORDER BY COUNT(*) DESC LIMIT 3)")
	// @Query("SELECT DISTINCT t FROM Twit t JOIN Like l ON t.id = l.twit.id WHERE
	// l.twit.id IN (SELECT * FROM (SELECT DISTINCT l2.twit.id FROM Like l2 GROUP BY
	// l2.twit.id ORDER BY COUNT(*) DESC LIMIT 3) as t2)")
	@Query(value = "select distinct t.* from twit t join likes l on t.id = l.twit_id where l.twit_id in (select * from (select distinct l2.twit_id from likes l2 group by l2.twit_id order by count(*) desc limit 3) as t2)", nativeQuery = true)
	public List<Twit> findTwitsByTopLike();

	// @Query("SELECT t FROM Twit t JOIN t.user u JOIN u.listModel l WHERE l.id =
	// :listId")
	// @Query("SELECT t FROM Twit t (JOIN ListModel l ON t.user.id =(SELECT f.id
	// FROM ListModel l2 JOIN l2.followings f)) WHERE l.id = :listId")
	// @Query("SELECT t FROM Twit t JOIN listModel l on l.user.id = (SELECT f.id
	// FROM listModel l2 JOIN l2.followings f on l2.id = f.id) WHERE l.id =
	// :listId")
	// @Query("SELECT t FROM Twit t JOIN ListModel l on t.user.id = (SELECT f.id
	// FROM ListModel l2 JOIN l2.followings f) WHERE l.id = :listId")
	@Query(value = "select distinct t.* from twit t join list_model_followings lmf on t.user_id = lmf.followings_id in (select lmf2.followings_id from list_model_followings lmf2) where lmf.list_model_id = :#{#listId}", nativeQuery = true)
	public List<Twit> searchListFollowedTwit(Long listId);

	@Query("SELECT t FROM Twit t ORDER BY t.viewCount DESC LIMIT 3")
	public List<Twit> findTwitsByTopView();

	List<Twit> findAllByIsTwitTrueOrderByCreatedAtDesc();

	List<Twit> findByRetwitUserContainsOrUser_IdAndIsTwitTrueOrderByCreatedAtDesc(User user, Long userId);

	List<Twit> findByLikesContainingOrderByCreatedAtDesc(User user);

	@Query("SELECT t FROM Twit t WHERE t.id IN (SELECT t1.replyFor.id FROM Twit t1 WHERE t1.user.id=:userId AND t1.isReply=true)")
	List<Twit> findUsersReplies(Long userId);

	@Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId")
	List<Twit> findByLikesUser_Id(Long userId);

	// @Query("SELECT t FROM Twit t JOIN t.likes l WHERE l.user.id = :userId")
	// List<Twit> findTwitsByUserIdInLikes(Long userId);

	@Query("SELECT DISTINCT t FROM Twit t WHERE t.content LIKE %:query%")
	public List<Twit> searchTwit(@Param("query") String query);

	@Query("SELECT t FROM Twit t JOIN t.user u WHERE u.id IN (SELECT f.id FROM User u2 JOIN u2.followings f WHERE u2.id = :userId)")
	public List<Twit> searchFollowedTwit(Long userId);

	List<Twit> findByRetwitUser(User user);

}

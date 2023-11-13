package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.zosh.model.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

	@Query("SELECT l From Notification l Where l.user.id=:userId AND l.twit.id=:twitId")
	public Notification isNotificationExist(@Param("userId") Long userId, @Param("twitId") Long twitId);
	
//	@Query("SELECT l From Notification l Where l.twit.id=:twitId")
//	public List<Notification> findByTwitId(@Param("twitId") Long twitId);
	
	// userId에 해당하는 twit들의 twitId 들
	//twitId들에 해당하는 notification들
	
	//@Query("SELECT n From Notification n Where n.user.id=:userId")
	@Query(value="select n.* from notifications n where n.twit_id in (select t.id from twit t where user_id = :#{#userId})", nativeQuery = true)
	public List<Notification> findByUserId(@Param("userId") Long userId);
}

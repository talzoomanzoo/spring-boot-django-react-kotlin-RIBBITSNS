//package com.zosh.repository;
//
//import java.util.List;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//
//import com.zosh.model.Notification;
//
//public interface NotificationRepository extends JpaRepository<Notification, Long> {
//
//	@Query("SELECT l From Notification l Where l.user.id=:userId AND l.twit.id=:twitId")
//	public Notification isNotificationExist(@Param("userId") Long userId, @Param("twitId") Long twitId);
//	
//	@Query("SELECT l From Notification l Where l.twit.id=:twitId")
//	public List<Notification> findByTwitId(@Param("twitId") Long twitId);
//}

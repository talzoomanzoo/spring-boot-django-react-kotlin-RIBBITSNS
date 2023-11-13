package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.zosh.model.User;

@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByEmail(String email);
	
	//@Query("SELECT DISTINCT u FROM User u WHERE u.fullName LIKE %:query% OR u.email LIKE %:query%")
	@Query(value="select distinct u.* from user u where u.full_name like CONCAT('%',:query,'%') or u.email like CONCAT('%',:query,'%')", nativeQuery=true)
	public List<User> searchUser(@Param("query") String query);

	List<User> findByFollowers(User user);
	List<User> findByFollowings(User user);
}

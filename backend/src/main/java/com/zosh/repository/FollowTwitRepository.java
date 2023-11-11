package com.zosh.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.zosh.model.FollowTwit;

@Transactional(readOnly = true)
public interface FollowTwitRepository extends JpaRepository<FollowTwit, Long>{

	@Query(value = "select ft.* from follow_twit ft where id=:#{#userId}", nativeQuery=true)
	public FollowTwit findByUserId(Long userId);
}

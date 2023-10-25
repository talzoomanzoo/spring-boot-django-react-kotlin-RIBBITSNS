package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.ListModel;
import com.zosh.model.User;

public interface ListRepository extends JpaRepository<ListModel, Long>{
	List<ListModel> findAllByOrderByCreatedAtDesc();
	
	List<ListModel> findByFollowings(User user);
}

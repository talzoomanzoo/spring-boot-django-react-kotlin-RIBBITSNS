package com.zosh.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.zosh.model.ListModel;

public interface ListRepository extends JpaRepository<ListModel, Long>{
	List<ListModel> findAllByOrderByCreatedAtDesc();
}

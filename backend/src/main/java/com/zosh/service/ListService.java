package com.zosh.service;

import java.util.List;

import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.ListModel;
import com.zosh.model.User;

public interface ListService {
//	public List<ListModel> findAllList();
	
	public List<ListModel> findAllPublicListByReqUser(User user) throws ListException, UserException;
	
	public List<ListModel> findAllPrivateListByReqUser(User user) throws ListException, UserException;
	
	public ListModel createList(ListModel req, User user) throws ListException, UserException;
	
	public ListModel editList(ListModel req, User user) throws UserException, ListException;
	
	public ListModel findById(Long listId) throws ListException;

	public ListModel addUser(Long userId, Long listId) throws ListException, UserException;
	
	public void deleteListById(Long listId, Long userId) throws ListException, UserException;
	
	public ListModel setPrivateById(Long listId, Long userId) throws ListException, UserException;
}

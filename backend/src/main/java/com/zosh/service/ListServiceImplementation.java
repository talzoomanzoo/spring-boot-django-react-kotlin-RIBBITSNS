package com.zosh.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.zosh.exception.ListException;
import com.zosh.exception.UserException;
import com.zosh.model.ListModel;
import com.zosh.model.User;
import com.zosh.repository.ListRepository;
import com.zosh.repository.UserRepository;

@Service
public class ListServiceImplementation implements ListService{
	
	private ListRepository listRepository;
	
	private UserRepository userRepository;
	
	public ListServiceImplementation(ListRepository listRepository, UserRepository userRepository) {
		this.listRepository = listRepository;
		this.userRepository = userRepository;
	}

	@Override
	public List<ListModel> findAllList() {
		// TODO Auto-generated method stub
		return listRepository.findAllByOrderByCreatedAtDesc();
	}
	
	@Override
	public ListModel findById(Long listId) throws ListException {
		// TODO Auto-generated method stub
		ListModel listModel = listRepository.findById(listId)
				.orElseThrow(()-> new ListException("List Not Found with Id" + listId));
		return listModel;
	}

	@Override
	public ListModel createList(ListModel req, User user) throws ListException, UserException {
		// TODO Auto-generated method stub
		ListModel listModel = new ListModel();
		listModel.setBackgroundImage(req.getBackgroundImage());
		listModel.setCreatedAt(LocalDateTime.now());
		listModel.setDescription(req.getDescription());
		listModel.setListName(req.getListName());
		listModel.setUser(user);
		
		return listRepository.save(listModel);
	}

	@Override
	public ListModel editList(ListModel req, User user) throws UserException, ListException {
		// TODO Auto-generated method stub
		ListModel listModel = findById(req.getId());
		
		listModel.setBackgroundImage(req.getBackgroundImage());
		listModel.setDescription(req.getDescription());
		listModel.setListName(req.getListName());
		listModel.setUser(user);
		listRepository.save(listModel);
		
		return listModel;
		
	}
	
	public User findUserById(Long userId) throws UserException {
		User user=userRepository.findById(userId).orElseThrow(() ->  new UserException("user not found with id "+userId));
		return user;
	}

	@Override
	public ListModel addUser(Long userId, Long listId) throws ListException, UserException {
		// TODO Auto-generated method stub
		ListModel listModel=findById(listId);
		User followToUser=findUserById(userId);
		if(listModel.getFollowings().contains(followToUser)) {
			listModel.getFollowings().remove(followToUser);
		}
		else {
			listModel.getFollowings().add(followToUser);
		}
		listRepository.save(listModel);
		return listModel;
	}

}

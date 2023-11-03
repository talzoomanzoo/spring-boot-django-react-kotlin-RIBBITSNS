package com.zosh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.zosh.dto.ListDto;
import com.zosh.model.ListModel;
import com.zosh.model.User;

public class ListDtoMapper {

	public static ListDto toListDto(ListModel listModel, User reqUser) {
		
		ListDto listDto=new ListDto();
		listDto.setId(listModel.getId());
		listDto.setBackgroundImage(listModel.getBackgroundImage());
		listDto.setDescription(listModel.getDescription());
		listDto.setListName(listModel.getListName());
		listDto.setFollowingsl(UserDtoMapper.toUserDtos(listModel.getFollowingsl()));
		listDto.setPrivateMode(listModel.isPrivateMode());
		listDto.setCreatedAt(listModel.getCreatedAt());
		listDto.setUser(UserDtoMapper.toUserDto(listModel.getUser()));
		System.out.println("UserDtoMapperTest + " + UserDtoMapper.toUserDtos(listModel.getFollowingsl()));
		System.out.println("listDtoCheck + " + listDto.getFollowingsl());
		return listDto;
	}
	
	public static List<ListDto> toListDtos(List<ListModel> listModels, User reqUser) {
		
		List<ListDto> listDtos=new ArrayList<>();
		
		for(ListModel listModel: listModels) {
			ListDto listDto=new ListDto();
			listDto.setId(listModel.getId());
			listDto.setBackgroundImage(listModel.getBackgroundImage());
			listDto.setDescription(listModel.getDescription());
			listDto.setListName(listModel.getListName());
			listDto.setFollowingsl(UserDtoMapper.toUserDtos(listModel.getFollowingsl()));
			listDto.setPrivateMode(listModel.isPrivateMode());
			listDto.setCreatedAt(listModel.getCreatedAt());
			listDto.setUser(UserDtoMapper.toUserDto(listModel.getUser()));
			listDtos.add(listDto);
		}
		
		return listDtos;
	}
	
//	public static ListDto toUsersListDto(ListModel listModel, User reqUSer) {
//		UserDto user=UserDtoMapper.toUserDto(listModel.getUser());
//		
//		//boolean isAdded=ListUtil.isAddedByReqUser(reqUser, listModel);
//		
//		
//	}
}

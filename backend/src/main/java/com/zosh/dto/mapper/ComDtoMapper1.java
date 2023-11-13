package com.zosh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.zosh.dto.ComDto;
import com.zosh.exception.UserException;
import com.zosh.model.Community;
import com.zosh.model.User;
import com.zosh.service.UserService;

public class ComDtoMapper1 {

	
	
	public static ComDto toComDto(Community community, User reqUser) {
		
		
		ComDto comDto= new ComDto();
		comDto.setId(community.getId());
		comDto.setUser(UserDtoMapper.toUserDto(community.getUser()));
		comDto.setComTwits(TwitDtoMapper.toTwitDtos(community.getComTwits(), reqUser));
		comDto.setComName(community.getComName());
		comDto.setDescription(community.getDescription());
		comDto.setBackgroundImage(community.getBackgroundImage());
		comDto.setFollowingsc(UserDtoMapper.toUserDtos(community.getFollowingsc()));
		comDto.setFollowingscReady(UserDtoMapper.toUserDtos(community.getFollowingscReady()));
		comDto.setPrivateMode(community.isPrivateMode());
		
		return comDto;
	}
	
	public static List<ComDto> toComDtos(List<Community> communities, User reqUser) {
		
		List<ComDto> comDtos=new ArrayList<>();
		
		for(Community community: communities) {
			ComDto comDto= new ComDto();
			comDto.setId(community.getId());
			comDto.setUser(UserDtoMapper.toUserDto(community.getUser()));
			comDto.setComTwits(TwitDtoMapper.toTwitDtos(community.getComTwits(), reqUser));
			comDto.setComName(community.getComName());
			comDto.setDescription(community.getDescription());
			comDto.setBackgroundImage(community.getBackgroundImage());
			comDto.setFollowingsc(UserDtoMapper.toUserDtos(community.getFollowingsc()));
			comDto.setFollowingscReady(UserDtoMapper.toUserDtos(community.getFollowingscReady()));
			comDto.setPrivateMode(community.isPrivateMode());
			comDtos.add(comDto);
			
		}
		
		return comDtos;
	}
}

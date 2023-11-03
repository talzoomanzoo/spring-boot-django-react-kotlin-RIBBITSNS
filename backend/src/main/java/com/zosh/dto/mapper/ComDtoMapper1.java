package com.zosh.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import com.zosh.dto.ComDto;
import com.zosh.model.Community;
import com.zosh.model.User;

public class ComDtoMapper1 {

	public static ComDto toComDto(Community community, User reqUser) {
		
		ComDto comDto= new ComDto();
		comDto.setId(community.getId());
		comDto.setUser(UserDtoMapper.toUserDto(community.getUser()));
		comDto.setComTwits(community.getComTwits());
		comDto.setComName(community.getComName());
		comDto.setDescription(community.getDescription());
		comDto.setBackgroundImage(community.getBackgroundImage());
		comDto.setFollowingsc(community.getFollowingsc());
		comDto.setPrivateMode(community.isPrivateMode());
		
		return comDto;
	}
	
	public static List<ComDto> toComDtos(List<Community> communities, User reqUser) {
		
		List<ComDto> comDtos=new ArrayList<>();
		
		for(Community community: communities) {
			ComDto comDto= new ComDto();
			comDto.setId(community.getId());
			comDto.setUser(UserDtoMapper.toUserDto(community.getUser()));
			comDto.setComTwits(community.getComTwits());
			comDto.setComName(community.getComName());
			comDto.setDescription(community.getDescription());
			comDto.setBackgroundImage(community.getBackgroundImage());
			comDto.setFollowingsc(community.getFollowingsc());
			comDto.setPrivateMode(community.isPrivateMode());
			comDtos.add(comDto);
		}
		
		return comDtos;
	}
}

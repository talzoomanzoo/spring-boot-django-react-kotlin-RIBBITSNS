package com.zosh.util;

import com.zosh.model.ListModel;
import com.zosh.model.User;

public class ListUtil {
	public static final boolean isFollowedByReqList(ListModel updatedList, User updatedUser) {
		return updatedList.getFollowingsl().contains(updatedUser);
	}
}

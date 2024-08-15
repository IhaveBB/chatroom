package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.model.Friend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @name: FriendMapper
 * @author: IhaveBB
 * @date: 2024-08-15 19:50
 **/
@Mapper
public interface FriendMapper {
	List<Friend> getFriendListById(int userId);
}

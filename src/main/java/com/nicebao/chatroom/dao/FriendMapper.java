package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @name: FriendMapper
 * @author: IhaveBB
 * @date: 2024-08-15 19:50
 **/
@Mapper
public interface FriendMapper {

	List<Friend> getFriendListById(int userId);

	Integer isExistMessageSession(@Param("userId") Integer userId,@Param("friendId") Integer friendId);

	List<Friend> searchFriendsByFriendName(String username);
	Integer addFriendByFriendId(@Param("friendId")Integer friendId, @Param("userId")Integer userId);
	Integer isFriendExists(@Param("userId") Integer userId, @Param("friendId")Integer friendId);
}

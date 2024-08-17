package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @name: UserMapper
 * @author: IhaveBB
 * @date: 2024-08-14 12:14
 **/
@Mapper
public interface UserMapper {
	User selectByName(String username);
	int insertUser(User user);
	int isUserIdExists(Integer userId);
}

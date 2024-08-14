package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.dto.RegisterRequest;
import com.nicebao.chatroom.model.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @name: UserMapper
 * @author: IhaveBB
 * @date: 2024-08-14 12:14
 **/
@Mapper
public interface UserMapper {
	User selectByName(String username);
	int insertUser(User user);
}

package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @name: UserMapper
 * @author: IhaveBB
 * @date: 2024-08-14 12:14
 **/
@Mapper
public interface UserMapper {
	@Select("SELECT * FROM user WHERE username = #{username}")
	User selectByName(String username);
}

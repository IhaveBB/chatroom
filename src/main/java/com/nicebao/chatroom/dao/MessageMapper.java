package com.nicebao.chatroom.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @name: MessageMapper
 * @author: IhaveBB
 * @date: 2024-08-15 22:46
 **/
@Mapper
public interface MessageMapper {
	List<Integer> getMessageSessionListByUserId(Integer userId);
}

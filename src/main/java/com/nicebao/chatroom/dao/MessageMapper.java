package com.nicebao.chatroom.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @name: MessageMapper
 * @author: IhaveBB
 * @date: 2024-08-16 09:21
 **/
@Mapper
public interface MessageMapper {
	String selectLastMessageBySessionId(Integer sessionId);
}

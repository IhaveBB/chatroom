package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.model.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @name: MessageMapper
 * @author: IhaveBB
 * @date: 2024-08-16 09:21
 **/
@Mapper
public interface MessageMapper {
	String selectLastMessageBySessionId(Integer sessionId);

	List<Message> getMessageBySessionId(Integer sessionId);
}

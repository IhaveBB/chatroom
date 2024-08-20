package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.MessageMapper;
import com.nicebao.chatroom.model.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @name: MessageService
 * @author: IhaveBB
 * @date: 2024-08-16 09:21
 **/
@Service
@Slf4j
public class MessageService {
	@Autowired
	private MessageMapper messageMapper;;
	
	/** 
	* @description: 获取会话的最后一条消息
	* @param: [java.lang.Integer]
	* @return: java.lang.String
	* @author: IhaveBB
	* @date: 2024/8/16 
	**/
	public String getLastMessageBySessionId(Integer sessionId){
		String lastMessage = messageMapper.selectLastMessageBySessionId(sessionId);
		return lastMessage;
	}

	public List<Message> getMessageBySessionId(Integer sessionId){
		List<Message> messages = messageMapper.getMessageBySessionId(sessionId);
		return messages;
	}

	public int addMessage(Message message){
		return messageMapper.addMessage(message);
	}
}

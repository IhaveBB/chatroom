package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.MessageMapper;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.nicebao.chatroom.model.Friend;
import com.sun.xml.internal.bind.v2.TODO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @name: MessageService
 * @author: IhaveBB
 * @date: 2024-08-15 22:46
 **/
@Service
@Slf4j
public class MessageService {
	@Autowired
	private MessageMapper messageMapper;
	@Autowired
	private UserService userService;
	/**
	* LJBTODO: 2024/8/16 0:07 IhaveBB
	 * 先根据UserId找出SessionId -> 根据用户ID，获取出用户的MessageSessionId
	 * 再根据SessionId找出每个会话中有哪些好友 ->用户点开消息后里面出现的好友
	 * 再根据SessionId 找出lastTime -> 最后一条消息时间
	 * 再根据SessionId找出lastMessage -> 最后一条消息内容
	*/

	/**
	* @description: 根据用户ID，获取出用户的MessageSessionId
	* @param: [int]
	* @return: java.util.List<java.lang.Integer>
	* @author: IhaveBB
	* @date: 2024/8/16
	**/
	public List<Integer> getMessageSessionIdListByUserId(Integer userId) {
		if(userService.isUserIdExist(userId) == false){
			throw new ServiceException(ResultCodeEnum.PARAM_IS_ERROR);
		}
		List<Integer> MessageSessionIdList = messageMapper.getMessageSessionListByUserId(userId);
		return MessageSessionIdList;
	}

	public List<Friend> getFriendBySessionId(Integer sessionId) {

	}
}

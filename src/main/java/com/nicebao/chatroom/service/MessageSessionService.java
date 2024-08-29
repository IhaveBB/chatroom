package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.MessageSessionMapper;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.nicebao.chatroom.model.AcceptMessageSessionId;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.MessageSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.LinkedList;
import java.util.List;

/**
 * @name: MessageService
 * @author: IhaveBB
 * @date: 2024-08-15 22:46
 **/
@Service
@Slf4j
public class MessageSessionService {
	@Autowired
	private MessageSessionMapper messageSessionMapper;
	@Autowired
	private MessageService messageService;
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
		List<Integer> MessageSessionIdList = messageSessionMapper.getMessageSessionListByUserId(userId);
		return MessageSessionIdList;
	}
	/** 
	* @description: 根据用户获取的SessionId，来获取到这个会话（sessionId）包含了哪些用户，排除最初的自己
	* @param: [java.lang.Integer, java.lang.Integer]
	* @return: java.util.List<com.nicebao.chatroom.model.Friend>
	* @author: IhaveBB
	* @date: 2024/8/16 
	**/
	public List<Friend> getMessageSessionFriendsBySessionId(Integer sessionId, Integer selfUserId) {
		List<Friend> friends = messageSessionMapper.getMessageSessionFriendsBySessionId(sessionId,selfUserId);
		return friends;
	}
	/**
	* @description: 构建最后需要返回的数据，左侧的数据显示
	* @param: [java.lang.Integer]
	* @return: java.util.List<com.nicebao.chatroom.model.MessageSession>
	* @author: IhaveBB
	* @date: 2024/8/16
	**/

	public List<MessageSession> getMessageSessionListByUserId(Integer userId) {
		if(userService.isUserIdExist(userId) == false){
			throw new ServiceException(ResultCodeEnum.PARAM_IS_ERROR);
		}
		List<MessageSession> messageSessionList = new LinkedList<>();
		List<Integer> sessionIdList = getMessageSessionIdListByUserId(userId);

		//构建需要返回的数据
		for(Integer sessionId : sessionIdList){
			MessageSession messageSession = new MessageSession();
			messageSession.setSessionId(sessionId);
			//获取会话中的好友
			List<Friend> friends = 	getMessageSessionFriendsBySessionId(sessionId,userId);
			messageSession.setFriends(friends);
			//获取最后一条消息
			String lastMessage = messageService.getLastMessageBySessionId(sessionId);
			if(lastMessage == null){
				messageSession.setLastMessage("");
			}else{
				messageSession.setLastMessage(lastMessage);
			}
			messageSessionList.add(messageSession);
		}
		return messageSessionList;
	}

	public Integer addMessageSession(Integer otherId,Integer userId){
		//先检查一下用户和创建对象ID的有效性再创建会话
		//防止有一个ID是空的，导致创建出来的会话里只有一个人
		if(userService.isUserIdExist(otherId) == false || userService.isUserIdExist(userId) == false){
			log.warn("ID缺失：otherId={},userId={}",otherId,userId);
			throw new ServiceException(ResultCodeEnum.PARAM_IS_ERROR);
		}
		//先插入数据到message_session表,创建出会话后获取到会话的Id
		//如果我们要获取数据库的自增值，需要创建一个对象来接受
		AcceptMessageSessionId acceptMessageSessionId = new AcceptMessageSessionId();
		int ret = messageSessionMapper.addMessageSession(acceptMessageSessionId);
		Integer messageSessionId = acceptMessageSessionId.getSessionId();
		if(messageSessionId == null || messageSessionId <= 0 || ret <= 0){
			log.warn("message_session表:创建会话失败，messageSessionId为{},数据库操作结果ret为{}",acceptMessageSessionId,ret);
			throw new ServiceException(ResultCodeEnum.SYSTEM_ERROR);
		}
		//给message_session_user表插入数据
		int ret2 = messageSessionMapper.addMessageSessionUser(messageSessionId,userId);
		int ret3 = messageSessionMapper.addMessageSessionUser(messageSessionId,otherId);
		if(ret2 <= 0 || ret3 <= 0){
			log.warn("message_session_user表:会话插入用户失败，影响行数为:{},{}",ret2,ret3);
			throw new ServiceException(ResultCodeEnum.SYSTEM_ERROR);
		}
		log.info("addMessageSession:会话创建成功,sessionId={},user1={},user2={}",messageSessionId,userId,otherId);
		return messageSessionId;
	}
}

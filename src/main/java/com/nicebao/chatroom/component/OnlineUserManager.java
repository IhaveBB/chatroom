package com.nicebao.chatroom.component;

import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.sun.org.apache.regexp.internal.RE;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @name: OnlineUserManager
 * @author: IhaveBB
 * @date: 2024-08-18 18:28
 **/
@Slf4j
@Component
public class OnlineUserManager {
	private ConcurrentHashMap<Integer, WebSocketSession> sessions = new ConcurrentHashMap<Integer,WebSocketSession>();

	public void addUser(int userId, WebSocketSession session) {
		if(sessions.get(userId) != null){
			log.info("UserID:{}，已登录，连接建立失败!",userId);
			//throw new ServiceException(ResultCodeEnum.USER_ALREADY_LOGGED_IN);
			return;
		}
		sessions.put(userId, session);
		log.info("UserID:{}，WebSocket连接建立",userId);
	}
	public void removeUser(int userId, WebSocketSession session) {
		//从之前的连接中获取到已经建立连接的Session
		WebSocketSession existSession = sessions.get(userId);
		if(existSession != session){
			log.info("传入session和已建立连接session不匹配");
			return;
			//throw new ServiceException(ResultCodeEnum.PARAM_SESSION_NOT_MATCHING);
		}
	}
	/** 
	* @description: 根据UserId返回Session
	* @param: [int]
	* @return: org.springframework.web.socket.WebSocketSession
	* @author: IhaveBB
	* @date: 2024/8/19 
	**/
	public WebSocketSession getSession(int userId) {
		return sessions.get(userId);
	}
}

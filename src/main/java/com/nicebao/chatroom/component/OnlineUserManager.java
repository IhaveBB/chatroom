package com.nicebao.chatroom.component;

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
	private final ConcurrentHashMap<Integer, WebSocketSession> sessions = new ConcurrentHashMap<>(); // 使用泛型钻石操作符

	public void addUser(int userId, WebSocketSession session) {
		if (sessions.containsKey(userId)) { // 使用containsKey方法判断
			log.warn("UserID:{} 已登录，连接建立失败!", userId); // 改为warn级别
			return;
		}
		sessions.put(userId, session);
		log.info("UserID:{} WebSocket连接建立", userId);
	}

	public void removeUser(int userId, WebSocketSession session) {
		WebSocketSession existSession = sessions.get(userId);
		if (existSession != null && existSession.equals(session)) { // 使用equals检查session匹配
			sessions.remove(userId);
			log.info("UserID:{} WebSocket连接已移除", userId);
		} else {
			log.warn("传入session和已建立连接session不匹配，UserID:{}", userId); // 改为warn级别
		}
	}

	/**
	 * @description: 根据UserId返回Session
	 * @param: userId
	 * @return: WebSocketSession
	 */
	public WebSocketSession getSession(int userId) {
		return sessions.get(userId);
	}
}

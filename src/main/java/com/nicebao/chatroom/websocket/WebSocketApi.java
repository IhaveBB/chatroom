package com.nicebao.chatroom.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicebao.chatroom.component.OnlineUserManager;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.Message;
import com.nicebao.chatroom.model.MessageRequest;
import com.nicebao.chatroom.model.MessageResponse;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.MessageService;
import com.nicebao.chatroom.service.MessageSessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;

/**
 * @name: WebSocketApi
 * @author: IhaveBB
 * @date: 2024-08-18 19:33
 **/
@Component
public class WebSocketApi extends TestWebSocketApi {
	private static final Logger log = LoggerFactory.getLogger(WebSocketApi.class);

	@Autowired
	private OnlineUserManager onlineUserManager;

	@Autowired
	private MessageSessionService messageSessionService;

	@Autowired
	private MessageService messageService;

	private final ObjectMapper objectMapper = new ObjectMapper(); // 使用final修饰，确保不可变

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("WebSocketApi:afterConnectionEstablished");
		User user = (User) session.getAttributes().get("user");
		if (user == null) {
			log.info("WebSocketApi:user is null");
			return;
		}
		onlineUserManager.addUser(user.getUserId(), session);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		log.error("[WebSocketAPI] 连接异常!", exception); // 使用error级别记录异常信息

		User user = (User) session.getAttributes().get("user");
		if (user != null) {
			onlineUserManager.removeUser(user.getUserId(), session);
		}
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("WebSocketApi:afterConnectionClosed");
		User user = (User) session.getAttributes().get("user");
		if (user != null) {
			onlineUserManager.removeUser(user.getUserId(), session);
		}
	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		User user = (User) session.getAttributes().get("user");
		if (user == null) {
			log.info("WebSocketApi:user is null");
			return;
		}

		try {
			MessageRequest req = objectMapper.readValue(message.getPayload(), MessageRequest.class);
			if ("message".equals(req.getType())) {
				handleMessages(user, req);
			} else {
				log.warn("WebSocketApi:handleTextMessage Error type!=message"); // 使用warn记录非期望情况
			}
		} catch (IOException e) {
			log.error("WebSocketApi:消息处理失败", e);
		}
	}

	private void handleMessages(User fromUser, MessageRequest req) throws IOException {
		MessageResponse response = new MessageResponse();
		response.setType("message");
		response.setSessionId(req.getSessionId());
		response.setFromId(fromUser.getUserId());
		response.setFromName(fromUser.getUsername());
		response.setContent(req.getContent());

		String responseJson = objectMapper.writeValueAsString(response);
		log.info("[handleMessages] {}", responseJson);

		List<Friend> friends = messageSessionService.getMessageSessionFriendsBySessionId(req.getSessionId(), fromUser.getUserId());

		Friend myself = new Friend();
		myself.setFriendName(fromUser.getUsername());
		myself.setFriendId(fromUser.getUserId());
		friends.add(myself);

		Message message = new Message();
		message.setFromId(fromUser.getUserId());
		message.setSessionId(req.getSessionId());
		message.setContent(req.getContent());
		int ret = messageService.addMessage(message);
		if (ret <= 0) {
			log.error("消息写入数据库失败"); // 改为error级别
		}

		for (Friend friend : friends) {
			WebSocketSession webSocketSession = onlineUserManager.getSession(friend.getFriendId());
			if (webSocketSession != null && webSocketSession.isOpen()) { // 检查WebSocket连接是否打开
				webSocketSession.sendMessage(new TextMessage(responseJson));
			} else {
				log.warn("UserId:{} 的WebSocket连接未打开或不存在", friend.getFriendId());
			}
		}
	}
}

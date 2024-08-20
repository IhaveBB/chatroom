package com.nicebao.chatroom.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nicebao.chatroom.component.OnlineUserManager;
import com.nicebao.chatroom.dao.MessageMapper;
import com.nicebao.chatroom.model.*;
import com.nicebao.chatroom.service.MessageService;
import com.nicebao.chatroom.service.MessageSessionService;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import sun.dc.pr.PRError;

import java.io.IOException;
import java.util.List;

/**
 * @name: WebSocketApi
 * @author: IhaveBB
 * @date: 2024-08-18 19:33
 **/
@Component
public class WebSocketApi extends TestWebSocketApi{
	private static final Logger log = LoggerFactory.getLogger(WebSocketApi.class);
	@Autowired
	private OnlineUserManager onlineUserManager;
	@Autowired
	private MessageSessionService messageSessionService;
	@Autowired
	private MessageService messageService;
	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void afterConnectionEstablished(WebSocketSession session) throws Exception {
		log.info("WebSocketApi:afterConnectionEstablished");
		User user = (User)session.getAttributes().get("user");
		if(user == null){
			log.info("WebSocketApi:user is null");
			return;
		}
		onlineUserManager.addUser(user.getUserId(),session);
	}

	@Override
	public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
		System.out.println("[WebSocketAPI] 连接异常!" + exception.toString());

		User user = (User) session.getAttributes().get("user");
		if (user == null) {
			return;
		}
		onlineUserManager.removeUser(user.getUserId(), session);
	}

	@Override
	public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
		log.info("WebSocketApi:afterConnectionClosed");
		User user = (User) session.getAttributes().get("user");
		if (user == null) {
			log.info("WebSocketApi:user is null");
		}
		onlineUserManager.removeUser(user.getUserId(), session);

	}

	@Override
	protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
		User user = (User) session.getAttributes().get("user");
		if (user == null) {
			log.info("WebSocketApi:user is null");
			return;
		}
		//jsong格式字符串转为java
		MessageRequest req = objectMapper.readValue(message.getPayload(), MessageRequest.class);
		if(req.getType().equals("message")){
			handleMessages(user,req);
		}else {
			log.info("WebSocketApi:handleTextMessage Error type!=message");
		}
	}

	private void handleMessages(User fromUser, MessageRequest req) throws IOException {
		MessageResponse response = new MessageResponse();
		response.setType("message");
		response.setSessionId(req.getSessionId());
		response.setFromId(fromUser.getUserId());
		response.setFromName(fromUser.getUsername());
		response.setContent(req.getContent());
		//java转成json对象
		String responseJson = objectMapper.writeValueAsString(response);
		log.info("[handleMessages]+{}",responseJson);

		//调用之前构建左侧消息列表时候的查询语句，根据会话ID查找到会话中有哪些好友
		List<Friend> friends = messageSessionService.getMessageSessionFriendsBySessionId(req.getSessionId(),fromUser.getUserId());
		//把自己也添加到这个会话中的人里
		Friend myself = new Friend();
		myself.setFriendName(fromUser.getUsername());
		myself.setFriendId(fromUser.getUserId());
		friends.add(myself);

		//先把消息存到数据库，下次打开会话历史消息会出现
		Message message = new Message();
		message.setFromId(fromUser.getUserId());
		message.setSessionId(req.getSessionId());
		message.setContent(req.getContent());
		int ret = messageService.addMessage(message);
		if(ret <= 0){
			log.info("消息写入数据库失败");
		}

		//循环遍历会话中的好友列表, 给列表中的每个用户都发一份响应消息
		for (Friend friend : friends) {
			WebSocketSession webSocketSession = onlineUserManager.getSession(friend.getFriendId());
			if(webSocketSession == null){
				continue;
			}
			webSocketSession.sendMessage(new TextMessage(responseJson));
		}
	}
}

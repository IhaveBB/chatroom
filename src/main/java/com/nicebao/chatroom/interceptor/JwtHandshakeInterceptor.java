package com.nicebao.chatroom.interceptor;

import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;
import java.util.Optional;

/**
 * websocket握手拦截器
 * @name: JwtHandshakeInterceptor
 * @author: IhaveBB
 * @date: 2024-09-04 16:54
 **/
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {
	@Autowired
	private UserService userService;

	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
		User user = userService.getUserInfo();

		if(user != null){
			attributes.put("user",user);
			return true;
		}
		return false;
	}

	@Override
	public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {

	}
}

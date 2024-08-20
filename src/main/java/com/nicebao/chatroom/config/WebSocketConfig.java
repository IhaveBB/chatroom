package com.nicebao.chatroom.config;

import com.nicebao.chatroom.websocket.TestWebSocketApi;
import com.nicebao.chatroom.websocket.WebSocketApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

/**
 * @name: WebSocketConfig
 * @author: IhaveBB
 * @date: 2024-08-16 23:12
 **/
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	@Autowired
	private WebSocketApi webSocketApi;
	@Override
	public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
		registry.addHandler(webSocketApi,"/chat")
				.addInterceptors(new HttpSessionHandshakeInterceptor())
				.setAllowedOrigins("*");
	}
}

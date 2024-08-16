package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.model.MessageSession;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.MessageSessionService;
import com.nicebao.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @name: MessageContorller
 * @author: IhaveBB
 * @date: 2024-08-15 22:34
 **/
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageSessionController {
	@Autowired
	private MessageSessionService messageService;
	@Autowired
	private MessageSessionService messageSessionService;
	@Autowired
	private UserService userService;

	@GetMapping("/getMessageSessionList")
	public List<MessageSession> getMessageSessionList(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		User user = userService.getUserFromSession(session);
		messageSessionService.getMessageSessionListByUserId(user.getUserId());

		return messageSessionService.getMessageSessionListByUserId(user.getUserId());
	}
}


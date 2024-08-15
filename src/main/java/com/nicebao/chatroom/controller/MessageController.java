package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.model.MessageSession;
import com.nicebao.chatroom.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @name: MessageContorller
 * @author: IhaveBB
 * @date: 2024-08-15 22:34
 **/
@RestController
@RequestMapping("/message")
@Slf4j
public class MessageController {
	@Autowired
	private MessageService messageService;

	public List<MessageSession> getSessionList() {

	}
}


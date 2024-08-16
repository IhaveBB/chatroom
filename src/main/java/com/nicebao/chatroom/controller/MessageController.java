package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.common.ResponseResult;
import com.nicebao.chatroom.model.Message;
import com.nicebao.chatroom.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @name: MessageController
 * @author: IhaveBB
 * @date: 2024-08-16 09:21
 **/
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {
	@Autowired
	private MessageService messageService;
	@GetMapping("/getMessage")
	public ResponseResult<List<Message>> getMessage(Integer sessionId){
		List<Message> messages = messageService.getMessageBySessionId(sessionId);
		return ResponseResult.success(messages);
	}

}

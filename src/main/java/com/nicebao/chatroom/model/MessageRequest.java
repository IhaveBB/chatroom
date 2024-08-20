package com.nicebao.chatroom.model;

import lombok.Data;

/**
 * @name: MessageRequest
 * @author: IhaveBB
 * @date: 2024-08-18 23:42
 **/
@Data
public class MessageRequest {
	private String type = "message";
	private String content;
	private Integer sessionId;
}

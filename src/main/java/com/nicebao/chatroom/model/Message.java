package com.nicebao.chatroom.model;

import lombok.Data;

/**
 * @name: Message
 * @author: IhaveBB
 * @date: 2024-08-16 09:21
 **/
@Data
public class Message {
	private Integer messageId;
	private Integer fromId;
	private String fromName;
	private Integer sessionId;
	private String content;
}

package com.nicebao.chatroom.model;

import lombok.Data;

/**
 * @name: MessageResponse
 * @author: IhaveBB
 * @date: 2024-08-18 23:42
 **/
@Data
public class MessageResponse  {
	private String type = "message";
	private int fromId;
	private String fromName;
	private int sessionId;
	private String content;
}

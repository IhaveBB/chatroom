package com.nicebao.chatroom.model;

import lombok.Data;

import java.util.List;

/**
 * @name: Session
 * @author: IhaveBB
 * @date: 2024-08-15 22:52
 **/
@Data
public class MessageSession {
	private int sessionId;
	private List<Friend> friends;
	private String lastMessage;
}

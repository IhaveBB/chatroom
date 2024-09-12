package com.nicebao.chatroom.model;
import lombok.Data;


/**
 * @name: VerificationMessage
 * @author: IhaveBB
 * @date: 2024-09-05 10:57
 **/
@Data
public class VerificationMessage {
	private int id;
	private int requestId;
	private int senderId;
	private String message;
	private Data createdAt;
}

package com.nicebao.chatroom.model;

import lombok.Data;

import javax.persistence.*;
import java.sql.Date;

/**
 * @name: friendRequest
 * @author: IhaveBB
 * @date: 2024-09-05 10:53
 **/
@Data
public class FriendRequest {
	private Integer id;
	private Integer senderId;
	private String senderName;
	private Integer receiverId;
	private String receiverName;
	private String status;
	private String message;
}

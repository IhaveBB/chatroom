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
	private int id;
	private Integer senderId;
	private Integer receiverId;
	private String status;
	private String message;
}

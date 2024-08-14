package com.nicebao.chatroom.model;

import lombok.Data;

/**
 * @name: User
 * @author: IhaveBB
 * @date: 2024-08-14 12:13
 **/
@Data
public class User {
	private int userId;
	private String username;
	private String password;
}
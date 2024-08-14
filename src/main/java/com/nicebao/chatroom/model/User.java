package com.nicebao.chatroom.model;

import lombok.Data;

import java.sql.Date;

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
	private String email;
	private Integer gender;
	private String phoneNumber;
	private Date registrationData;
	private Date lastLoginData;
	private Boolean isActive;
	private String avatarUrl;
	private Integer roleId;
}
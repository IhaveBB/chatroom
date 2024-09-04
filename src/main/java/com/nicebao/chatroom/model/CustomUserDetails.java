package com.nicebao.chatroom.model;

/**
 * 
 *
 * @name: CustomUserDetails
 * @author: IhaveBB
 * @date: 2024-09-02 22:57
**/
public class CustomUserDetails {
	private Integer userId;
	private String username;

	public CustomUserDetails(Integer userId, String username) {
		this.userId = userId;
		this.username = username;
	}

	public Integer getUserId() {
		return userId;
	}

	public String getUsername() {
		return username;
	}
}

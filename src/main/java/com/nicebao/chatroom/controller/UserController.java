package com.nicebao.chatroom.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name: UserController
 * @author: IhaveBB
 * @date: 2024-08-14 00:48
 * @description: 用户api
 **/
@RestController
@RequestMapping("/user")
public class UserController {
	@RequestMapping("/login")
	public String login() {
		return "login";
	}
	@RequestMapping("/register")
	public int hello() {
		int i = 9/0;
		return i;
	}
}

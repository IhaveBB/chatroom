package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.common.ResponseResult;
import com.nicebao.chatroom.dto.LoginRequest;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * @name: UserController
 * @author: IhaveBB
 * @date: 2024-08-14 00:48
 * @description: 用户api
 **/
@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@PostMapping("/login")
	public ResponseResult<User> login(@Valid @RequestBody LoginRequest request, HttpServletRequest req)  {
		User user = userService.login(request,req);
		return ResponseResult.success(user);
	}
}

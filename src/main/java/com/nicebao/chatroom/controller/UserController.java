package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.common.ResponseResult;
import com.nicebao.chatroom.dto.LoginRequest;
import com.nicebao.chatroom.dto.RegisterRequest;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Registration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.ws.http.HTTPBinding;

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

	/**
	* LJBTODO: 2024/8/14 17:58 IhaveBB JWT校验 密码加密等均未完成
	*/
	@PostMapping("/login")
	public ResponseResult<User> login(@Valid @RequestBody LoginRequest request, HttpServletRequest req)  {
		User user = userService.login(request,req);
		return ResponseResult.success(user);
	}
	@PostMapping("/register")
	public ResponseResult<Integer> register(@Valid @RequestBody RegisterRequest request){
		int ret = userService.register(request);
		return ResponseResult.success(ret);
	}
	@GetMapping("/userInfo")
	public ResponseResult<User> getUserInfo(HttpServletRequest req){
		HttpSession session = req.getSession(false);
		User user = userService.getUserFromSession(session);
		return ResponseResult.success(user);
	}
}

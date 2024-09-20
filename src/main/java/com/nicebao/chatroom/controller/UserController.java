package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.common.ResponseResult;
import com.nicebao.chatroom.dto.LoginRequest;
import com.nicebao.chatroom.dto.RegisterRequest;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.Registration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.xml.ws.http.HTTPBinding;
import java.util.LinkedList;
import java.util.List;

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
	public ResponseResult<User> login(@Valid @RequestBody LoginRequest request, HttpServletResponse response)  {
		return ResponseResult.success(userService.login(request,response));
	}
	@PostMapping("/register")
	public ResponseResult<Integer> register(@Valid @RequestBody RegisterRequest request){
		int ret = userService.register(request);
		return ResponseResult.success(ret);
	}
	@GetMapping("/userInfo")
	public ResponseResult<User> getUserInfo(){
		User user = userService.getUserInfo();
		return ResponseResult.success(user);
	}


}

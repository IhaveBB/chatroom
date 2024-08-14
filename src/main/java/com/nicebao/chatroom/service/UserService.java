package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.UserMapper;
import com.nicebao.chatroom.dto.LoginRequest;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.nicebao.chatroom.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @name: UserService
 * @author: IhaveBB
 * @date: 2024-08-14 00:49
 * @description: 用户层service
 **/
@Service
@Slf4j
public class UserService {
	@Autowired
	UserMapper userMapper;
	public User login(LoginRequest request, HttpServletRequest req){
		User user = userMapper.selectByName(request.getUsername());
		if(user == null || !user.getPassword().equals(request.getPassword())) {
			log.debug("username:{}, password:{}", request.getUsername(), request.getPassword());
			log.info("用户{}登录失败，用户名或者密码错误", request.getUsername());
			throw new ServiceException(ResultCodeEnum.USER_LOGIN_ERROR);
		}
		HttpSession session = req.getSession();
		session.setAttribute("user", user);
		user.setPassword("");
		return user;
	}
}

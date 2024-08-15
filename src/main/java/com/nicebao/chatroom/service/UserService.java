package com.nicebao.chatroom.service;

import com.nicebao.chatroom.common.ResponseResult;
import com.nicebao.chatroom.dao.UserMapper;
import com.nicebao.chatroom.dto.LoginRequest;
import com.nicebao.chatroom.dto.RegisterRequest;
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

	public Integer register(RegisterRequest request) {
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(request.getPassword());
		user.setEmail(request.getEmail());
		user.setGender(request.getGender());
		user.setIsActive(true);
		//普通用户1
		//管理员用户2
		user.setRoleId(1);
		int ret = userMapper.insertUser(user);
		if(ret > 0){
			return ret;
		}
		throw new ServiceException(ResultCodeEnum.REGISTER_ERROR);
	}

	public User getUserFromSession(HttpSession session) {
		if(session == null){
			log.debug("session is null");
			throw new ServiceException(ResultCodeEnum.USER_NOTLOGGED_IN);
		}
		User user = (User) session.getAttribute("user");
		if (user == null){
			log.debug("user is null");
			throw new ServiceException(ResultCodeEnum.USER_NOTLOGGED_IN);
		}
		user.setPassword("");
		return user;
	}
}

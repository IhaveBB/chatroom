package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.UserMapper;
import com.nicebao.chatroom.dto.LoginRequest;
import com.nicebao.chatroom.dto.RegisterRequest;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.nicebao.chatroom.model.CustomUserDetails;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.utils.JWTUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

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

	public String login(LoginRequest request, HttpServletResponse response){
		User user = userMapper.selectByName(request.getUsername());
		if(user == null || !user.getPassword().equals(request.getPassword())) {
			log.debug("username:{}, password:{}", request.getUsername(), request.getPassword());
			log.info("用户{}登录失败，用户名或者密码错误", request.getUsername());
			throw new ServiceException(ResultCodeEnum.USER_LOGIN_ERROR);
		}
//		HttpSession session = req.getSession();
//		session.setAttribute("user", user);

		Map<String,Object> map = new HashMap<>();
		map.put("userId",user.getUserId());
		map.put("username",request.getUsername());
		String token = JWTUtils.genJwt(map);

		Cookie cookie = new Cookie("token", token);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);
		return "登录成功";
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

	public User getUserInfo() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		User user = new User();
		user.setUserId(userDetails.getUserId());
		user.setUsername(userDetails.getUsername());
		return user;
	}

	/**
	* @description: 检测UserId是否存在
	* @param: [java.lang.Integer]
	* @return: boolean
	* @author: IhaveBB
	* @date: 2024/8/16
	**/
	public boolean isUserIdExist(Integer userId) {
		if(userId == null || userId <= 0){
			return false;
		}
		Integer count = userMapper.isUserIdExists(userId);
		if(count == 00 || count == null){
			return false;
		}
		return true;
	}
}

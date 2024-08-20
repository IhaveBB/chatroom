package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.common.ResponseResult;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.FriendService;
import com.nicebao.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @name: FriendController
 * @author: IhaveBB
 * @date: 2024-08-15 22:24
 **/
@RestController
@Slf4j
@RequestMapping("/friend")
public class FriendController {
	@Autowired
	private FriendService friendService;

	@GetMapping("/getFriendList")
	public ResponseResult<List<Friend>> getFriendList(HttpServletRequest req){
		HttpSession session = req.getSession(false);
		List<Friend> friends = friendService.getFriendList(session);
		log.info(friends.toString());
		return ResponseResult.success(friends);
	}
	@GetMapping("/isExistMessageSession")
	public ResponseResult<Integer> isExistMessageSession(HttpServletRequest req,Integer friendId){
		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("user");
		Integer ret = friendService.isExistMessageSession(user.getUserId(),friendId);
		return ResponseResult.success(ret);
	}

}

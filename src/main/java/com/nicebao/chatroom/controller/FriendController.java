package com.nicebao.chatroom.controller;

import com.nicebao.chatroom.common.ResponseResult;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.service.FriendService;
import com.nicebao.chatroom.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.ResourceBundle;

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
	@Autowired
	private UserService userService;
	@GetMapping("/getFriendList")
	public ResponseResult<List<Friend>> getFriendList(){
//		HttpSession session = req.getSession(false);
//		List<Friend> friends = friendService.getFriendList(session);
		List<Friend> friends = friendService.getFriendList();
		log.info(friends.toString());
		return ResponseResult.success(friends);
	}
	@GetMapping("/isExistMessageSession")
	public ResponseResult<Integer> isExistMessageSession(Integer friendId){
//		HttpSession session = req.getSession(false);
//		User user = (User) session.getAttribute("user");
		User user = userService.getUserInfo();
		Integer ret = friendService.isExistMessageSession(user.getUserId(),friendId);
		log.info(ret.toString());
		return ResponseResult.success(ret);
	}
	/** 
	* @description: 添加好友，根据用户名来进行搜索，返回链表，展示有哪些数据
	* @param: 
	* @return: 
	* @author: IhaveBB
	* @date: 2024/8/29 
	**/
	@GetMapping("/searchFriends")
	public ResponseResult<List<Friend>> searchFriends(String searchName){
		return ResponseResult.success(friendService.searchFriendsByFriendName(searchName));
	}

	/** 
	* @description: 添加好友，根据好友ID进行添加双向好友
	* @param: 
	* @return: 
	* @author: IhaveBB
	* @date: 2024/8/29 
	**/
	@GetMapping("/addFriend")
	public ResponseResult addFriend(Integer otherId){
		User user = userService.getUserInfo();
		friendService.addFriendByFriendId(otherId,user);
		return ResponseResult.success("");
	}

}

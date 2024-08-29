package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.FriendMapper;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @name: FriendService
 * @author: IhaveBB
 * @date: 2024-08-15 19:49
 **/
@Service
@Slf4j
public class FriendService {
	@Autowired
	private FriendMapper friendMapper;
	@Autowired
	private UserService userService;

	public List<Friend> getFriendList(HttpSession session) {
		if(session == null){
			log.debug("session is null");
			throw new ServiceException(ResultCodeEnum.USER_NOTLOGGED_IN);
		}
		User user = (User) session.getAttribute("user");
		if (user == null){
			log.debug("user is null");
			throw new ServiceException(ResultCodeEnum.USER_NOTLOGGED_IN);
		}
		List<Friend> friends = friendMapper.getFriendListById(user.getUserId());
		log.info(friends.toString());
		return friends;
	}

	public Integer isExistMessageSession(Integer userId, Integer friendId) {
		Integer ret = friendMapper.isExistMessageSession(userId,friendId);
		return ret;
	}

	public List<Friend> searchFriendsByFriendName(String searchName) {
		if(searchName == null){
			log.debug("searchName is null");
		}
		List<Friend> friendList = friendMapper.searchFriendsByFriendName(searchName);
		return friendList;
	}

	public void addFriendByFriendId(Integer otherId, User user) {
		Integer userId = user.getUserId();
		if(userService.isUserIdExist(otherId) == false || userService.isUserIdExist(userId) == false){
			throw new ServiceException(ResultCodeEnum.PARAM_IS_ERROR);
		}
		if (otherId == userId){
			throw new ServiceException(ResultCodeEnum.PARAM_IS_ERROR);
		}
		//在添加好友前，先检查一下表钟是否存在这一条记录
		int isExistFriendRelationship = friendMapper.isFriendExists(userId,otherId);
		if(isExistFriendRelationship > 0){
			log.info("friend is exist，不添加记录");
			throw new ServiceException(ResultCodeEnum.FRIEND_ALREADY_EXISTS);
		}
		//添加双向好友，A有B B有A
		int ret1 = friendMapper.addFriendByFriendId(otherId,userId);
		int ret2 = friendMapper.addFriendByFriendId(userId,otherId);
		if(ret1 <= 0 || ret2 <= 0){
			log.warn("friend表:会话插入用户失败，影响行数为:{},{}",ret1,ret2);
			throw new ServiceException(ResultCodeEnum.SYSTEM_ERROR);
		}
		log.info("friend添加成功,ID1={},ID2={}",userId,otherId);
	}
}

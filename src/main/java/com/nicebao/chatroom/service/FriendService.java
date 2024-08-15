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
}

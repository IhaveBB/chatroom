package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.FriendMapper;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.FriendRequest;
import com.nicebao.chatroom.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

	public List<Friend> getFriendList() {
//		if(session == null){
//			log.debug("session is null");
//			throw new ServiceException(ResultCodeEnum.USER_NOTLOGGED_IN);
//		}
//		User user = (User) session.getAttribute("user");
//		if (user == null){
//			log.debug("user is null");
//			throw new ServiceException(ResultCodeEnum.USER_NOTLOGGED_IN);
//		}
		User user = userService.getUserInfo();
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

//	public void addFriendByFriendId(Integer otherId, User user) {
//		Integer userId = user.getUserId();
//		if(userService.isUserIdExist(otherId) == false || userService.isUserIdExist(userId) == false){
//			throw new ServiceException(ResultCodeEnum.PARAM_IS_ERROR);
//		}
//		if (otherId == userId){
//			throw new ServiceException(ResultCodeEnum.PARAM_IS_ERROR);
//		}
//		//在添加好友前，先检查一下表钟是否存在这一条记录
//		int isExistFriendRelationship = friendMapper.isFriendExists(userId,otherId);
//
//		if(isExistFriendRelationship > 0){
//			log.info("friend is exist，不添加记录");
//			throw new ServiceException(ResultCodeEnum.FRIEND_ALREADY_EXISTS);
//		}
//		//添加双向好友，A有B B有A
//		int ret1 = friendMapper.addFriendByFriendId(otherId,userId);
//		int ret2 = friendMapper.addFriendByFriendId(userId,otherId);
//		if(ret1 <= 0 || ret2 <= 0){
//			log.warn("friend表:会话插入用户失败，影响行数为:{},{}",ret1,ret2);
//			throw new ServiceException(ResultCodeEnum.SYSTEM_ERROR);
//		}
//		log.info("friend添加成功,ID1={},ID2={}",userId,otherId);
//	}

	/** 
	* @description: 发送好友申请
	* @param: [java.lang.Integer, java.lang.String]
	* @return: java.lang.Integer
	* @author: IhaveBB
	* @date: 2024/9/6 
	**/
	public Integer sendFriendRequest(Integer receiverId, String message) {
		//当前登录用户就是发送者， 获取登录用户信息
		Optional<User> sender = Optional.ofNullable(userService.getUserInfo());
		Optional<User> receiver = Optional.ofNullable(userService.selectByUserId(receiverId));

		if (sender.isPresent() && receiver.isPresent()) {
			FriendRequest friendRequest = new FriendRequest();
			friendRequest.setSenderId(sender.get().getUserId());
			friendRequest.setReceiverId(receiver.get().getUserId());
			friendRequest.setStatus("PENDING");
			friendRequest.setMessage(message);
			return friendMapper.addFriendRequest(friendRequest);
		}
		return 1;
	}
	/** 
	* @description: 同意好友申请
	* @param: [java.lang.Integer]
	* @return: java.lang.String
	* @author: IhaveBB
	* @date: 2024/9/6 
	**/
	public String acceptFriendRequest(Integer requestId) {
		//根据先前的申请好友那条回话ID，来获取到双方的信息
		//这里需要鉴权，先获取一下登录用户信息，然后看登录用户是否是被申请好友的那一方，只有被申请好友的那一方才有权限同意
		Integer loginUserId = userService.getUserInfo().getUserId();

		FriendRequest friendRequest = friendMapper.getFriendRequestById(requestId);
		Integer receiverId = friendRequest.getReceiverId();
		Integer senderId =	friendRequest.getSenderId();
		if(loginUserId != receiverId){
			throw new ServiceException(ResultCodeEnum.USER_ILLEGAL_ACCESS);
		}
		//鉴权没问题后，添加双向好友，A有B B有A
		//在添加好友前，先检查一下表钟是否存在这一条记录
		int isExistFriendRelationship = friendMapper.isFriendExists(senderId,receiverId);

		if(isExistFriendRelationship > 0){
			log.info("friend is exist，不添加记录");
			throw new ServiceException(ResultCodeEnum.FRIEND_ALREADY_EXISTS);
		}
		//添加双向好友，A有B B有A
		int ret1 = friendMapper.addFriendByFriendId(receiverId,senderId);
		int ret2 = friendMapper.addFriendByFriendId(senderId,receiverId);
		if(ret1 <= 0 || ret2 <= 0){
			log.warn("friend表:会话插入用户失败，影响行数为:{},{}",ret1,ret2);
			throw new ServiceException(ResultCodeEnum.SYSTEM_ERROR);
		}
		log.info("friend添加成功,ID1={},ID2={}",senderId,receiverId);

		//然后修改申请信息的状态，修改为已同意
		friendRequest.setStatus("ACCEPTED");
		if(friendMapper.updateFriendRequestStatus(friendRequest) <= 0){
			log.info("friendService:acceptFriendRequest:申请好友信息会话状态修改失败");
			throw new ServiceException(ResultCodeEnum.CHANGE_FRIEND_REQUEST_STATUS_ERROR);
		}
		return "accept";
	}
	/** 
	* @description: 拒绝好友申请
	* @param: [java.lang.Integer]
	* @return: java.lang.String
	* @author: IhaveBB
	* @date: 2024/9/6 
	**/
	public String rejectFriendRequest(Integer requestId) {
		//根据先前的申请好友那条回话ID，来获取到双方的信息
		//这里需要鉴权，先获取一下登录用户信息，然后看登录用户是否是被申请好友的那一方，只有被申请好友的那一方才有权限同意
		Integer loginUserId = userService.getUserInfo().getUserId();

		FriendRequest friendRequest = friendMapper.getFriendRequestById(requestId);
		Integer receiverId = friendRequest.getReceiverId();
		Integer senderId =	friendRequest.getSenderId();
		if(loginUserId != receiverId){
			throw new ServiceException(ResultCodeEnum.USER_ILLEGAL_ACCESS);
		}
		//校验没问题，直接修改状态为拒绝，不修改好友表信息
		friendRequest.setStatus("REJECTED");
		if(friendMapper.updateFriendRequestStatus(friendRequest) <= 0){
			log.info("friendService:rejectFriendRequest:申请好友信息会话状态修改失败");
			throw new ServiceException(ResultCodeEnum.CHANGE_FRIEND_REQUEST_STATUS_ERROR);
		}
		return "reject";
	}

	/**
	* @description: 获取全部的和自己有关的申请，前段进行具体的校验
	 * 用户获取列表，其中包含senderid，receiveId，status
	 * padding
	 * 发送者：待审核
	 * 接受者：接受/拒绝
	 * rejected
	 * 发送者：拒绝
	 * 接受者：已拒绝
	 * 前段根据userId判断是发送者还是接受者
	* @param: []
	* @return: java.util.List<com.nicebao.chatroom.model.FriendRequest>
	* @author: IhaveBB
	* @date: 2024/9/6 
	**/
	public List<FriendRequest> getFriendRequestList() {
		User user = userService.getUserInfo();
		return friendMapper.getFriendRequestList(user.getUserId());
	}
	


}

package com.nicebao.chatroom.service;

import com.nicebao.chatroom.dao.FriendMapper;
import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import com.nicebao.chatroom.model.Friend;
import com.nicebao.chatroom.model.FriendRequest;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.model.VerificationMessage;
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
		
		//这里需要鉴权，先获取一下登录用户信息，然后看登录用户是否是被申请好友的那一方，只有被申请好友的那一方才有权限同意
		Integer loginUserId = userService.getUserInfo().getUserId();

		FriendRequest friendRequest = friendMapper.getFriendRequestById(requestId);
		Integer receiverId = friendRequest.getReceiverId();
		Integer senderId =	friendRequest.getSenderId();
		if(loginUserId != receiverId){
			throw new ServiceException(ResultCodeEnum.USER_ILLEGAL_ACCESS);
		}
		String ret = friendMapper.getFriendRequestStats(3);
		log.info("status{},receiverID:{}",friendRequest.getStatus(),receiverId);
		log.info(friendMapper.getFriendRequestStats(requestId)) ;
		//在更改状态前先检测一下，当前是否为PADDING，如果是可以修改
		//如果不是则证明已经处理过这个消息了，拒绝二次修改
		if(!"PENDING".equalsIgnoreCase(friendRequest.getStatus())){
			throw new ServiceException(ResultCodeEnum.USER_FRIEND_REQUEST_NOT_PENDING);
		}
		//没问题后，添加双向好友，A有B B有A
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
		if(loginUserId != receiverId){
			throw new ServiceException(ResultCodeEnum.USER_ILLEGAL_ACCESS);
		}
		//在更改状态前先检测一下，当前是否为PADDING，如果是可以修改
		//如果不是则证明已经处理过这个消息了，拒绝二次修改
		if(!"PENDING".equals(friendMapper.getFriendRequestStats(receiverId).toUpperCase())){
			throw new ServiceException(ResultCodeEnum.USER_FRIEND_REQUEST_NOT_PENDING);
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
	/**
	 * @description: 根据请求ID，可以获取到双方信息，和这个验证消息的状态，只有处于待审核时候才能发送消息。
	 * 此外，需要根据登录用户的ID，和会话信息里的双方ID进行匹配，查看是否是这个请求中的人 ！= 则为非法访问
	 * 信息发送者就是登录用户
	 * @param: [java.lang.Long, java.lang.Long, java.lang.String]
	 * @return: com.nicebao.chatroom.model.VerificationMessage
	 * @author: IhaveBB
	 * @date: 2024/9/12
	 **/
	public Integer sendVerificationMessage(int requestId, String message) {
		//鉴权，保证登录用户是好友申请的发送者或者是接受者
		Integer loginUserId = userService.getUserInfo().getUserId();
		FriendRequest friendRequest = friendMapper.getFriendRequestById(requestId);
		Integer RequestSenderId = friendRequest.getReceiverId();
		Integer ResponseSenderId = friendRequest.getSenderId();
		if(loginUserId != RequestSenderId && loginUserId != ResponseSenderId){
			throw new ServiceException(ResultCodeEnum.USER_ILLEGAL_ACCESS);
		}
		log.info(friendRequest.getStatus().toUpperCase());
		//验证会话的状态
		if(!"PENDING".equals(friendRequest.getStatus().toUpperCase())){
			throw new ServiceException(ResultCodeEnum.USER_FRIEND_REQUEST_CLOSED);
		}

		VerificationMessage verificationMessage = new VerificationMessage();
		verificationMessage.setMessage(message);
		verificationMessage.setRequestId(requestId);
		verificationMessage.setSenderId(loginUserId);


		return friendMapper.saveVerificationMessage(verificationMessage);

	}
	/**
	 * 判断用户之间的朋友关系是否成立
	 *
	 * @param userId 用户ID，用于判断朋友关系的用户
	 * @param friendId 友ID，用于判断朋友关系的另一方
	 * @return 如果朋友关系成立返回true，否则返回false
	 */
	public boolean isFriend(int userId, int friendId){
		return friendMapper.isFriendExists(userId, friendId) > 0;
	}

	public User getFriendInfoById(Integer friendId) {
		User user = userService.getUserInfo();
		log.info("friendId:{},userId:{}",friendId,user.getUserId());
		//首先检测是不是好友，只有好友才能查看详细资料
		if(!isFriend(user.getUserId(), friendId)){
			throw new ServiceException(ResultCodeEnum.USER_ILLEGAL_ACCESS);
		}
		/**
		 * LJBTODO: 2024/9/19 9:19 IhaveBB 后期，用户可以设置对外展示哪些资料
		 */
		User friendInfo = userService.selectByUserId(friendId);
		friendInfo.setPassword("");
		return friendInfo;
	}

}

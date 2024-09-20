package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.model.FriendRequest;
import com.nicebao.chatroom.model.User;
import com.nicebao.chatroom.model.VerificationMessage;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
/**
 * 
 *
 * @name: FriendMapperTest
 * @author: IhaveBB
 * @date: 2024-09-01 21:33
 * @
**/
@SpringBootTest
@Slf4j
class FriendMapperTest {
	@Autowired
	private FriendMapper friendMapper;
	@Test
	void getFriendListById() {
	}

	@Test
	void isExistMessageSession() {
		int ret = friendMapper.isExistMessageSession(1,2);
		log.info("ret={}",ret);

	}

	@Test
	void searchFriendsByFriendName() {
	}

	@Test
	void addFriendByFriendId() {
	}

	@Test
	void isFriendExists() {
	}

	@Test
	void getFriendRequestById() {
		FriendRequest friendRequest = friendMapper.getFriendRequestById(2);
		System.out.println(friendRequest.toString());
	}

	@Test
	void addFriendRequest() {
		FriendRequest friendRequest = new FriendRequest();
		friendRequest.setStatus("PENDING");
		friendRequest.setMessage("test");
		friendRequest.setSenderId(1);
		friendRequest.setReceiverId(2);

		friendMapper.addFriendRequest(friendRequest);

	}

	@Test
	void updateFriendRequestStatus() {
		FriendRequest friendRequest = new FriendRequest();
		friendRequest.setStatus("ACCEPTED");
		friendRequest.setMessage("test");
		friendRequest.setSenderId(1);
		friendRequest.setReceiverId(2);
		int ret = friendMapper.updateFriendRequestStatus(friendRequest);
		System.out.println(ret);
	}

	@Test
	void saveVerificationMessage() {
		VerificationMessage verificationMessage = new VerificationMessage();
		verificationMessage.setRequestId(222);
		verificationMessage.setMessage("test");
		verificationMessage.setSenderId(111);
		friendMapper.saveVerificationMessage(verificationMessage);
	}

	@Test
	void getFriendRequestStats() {
		String ret = friendMapper.getFriendRequestStats(4);
		System.out.println(ret);
	}

	@Test
	void testGetFriendRequestStats() {
		String ret = friendMapper.getFriendRequestStats(3);
		System.out.println(ret);
	}
}
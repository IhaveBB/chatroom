package com.nicebao.chatroom.dao;

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
}
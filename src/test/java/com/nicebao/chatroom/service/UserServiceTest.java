package com.nicebao.chatroom.service;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @name: UserServiceTest
 * @author: IhaveBB
 * @date: 2024-09-04 17:38
 **/
class UserServiceTest {

	@Test
	void register() {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String password = passwordEncoder.encode("Qwe123456");
		System.out.println(password);
	}
}
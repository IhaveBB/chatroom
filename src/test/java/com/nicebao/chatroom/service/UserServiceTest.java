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
		String password = passwordEncoder.encode("($Avd(OP!$u8msxxp1)FT&v7q!");
		System.out.println(password);
	}
}
package com.nicebao.chatroom.model;

import lombok.Data;

/**
 * 这个Friend不是单纯的表示数据库中的Friend表
 * 而是对应Friend和User表联合查询出来的用户名和对象
 *
 * @name: Friend
 * @author: IhaveBB
 * @date: 2024-08-14 12:14
 **/

@Data
public class Friend {
	private int friendId;
	private String friendName;
}

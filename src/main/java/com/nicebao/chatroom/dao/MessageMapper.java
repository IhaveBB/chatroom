package com.nicebao.chatroom.dao;

import com.nicebao.chatroom.model.Friend;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @name: MessageMapper
 * @author: IhaveBB
 * @date: 2024-08-15 22:46
 **/
@Mapper
public interface MessageMapper {
	//获取用户有多少个会话，也就是有多少个SessionId，然后通过最后的发送时间排序
	List<Integer> getMessageSessionListByUserId(Integer userId);
	//根据用户获取的SessionId，来获取到这个会话（sessionId）包含了哪些用户，排除最初的自己
	List<Friend> getMessageSessionFriendsBySessionId(Integer sessionId,Integer selfUserId);
}

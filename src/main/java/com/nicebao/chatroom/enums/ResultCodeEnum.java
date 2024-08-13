package com.nicebao.chatroom.enums;

/**
 * Created by IhaveBB on 2024/8/13 17:55
 * @author IhaveBB
 */
public enum ResultCodeEnum {
	Success(200,"success"),
	PARAM_IS_INVALID(1001, "参数无效"),
	PARAM_IS_BLANK(1002, "参数为空"),
	PARAM_TYPE_BIND_ERROR(1003, "参数类型错误"),
	PARAM_NOT_COMPLETE(1004, "参数缺失"),
	/* 用户错误 2001-2999*/
	USER_NOTLOGGED_IN(2001, "用户未登录"),
	USER_LOGIN_ERROR(2002, "账号不存在或密码错误"),
	SYSTEM_ERROR(10000, "系统异常，请稍后重试");
	;

	private int code;
	private String message;


	ResultCodeEnum(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}
}

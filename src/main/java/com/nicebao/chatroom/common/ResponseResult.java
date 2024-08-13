package com.nicebao.chatroom.common;

import com.nicebao.chatroom.enums.ResultCodeEnum;
import lombok.Builder;
import lombok.Data;

/**
 * 统一结果返回
 *
 * @name: ResponseResult
 * @author: IhaveBB
 * @date: 2024-08-13 18:10
 **/
@Data
@Builder
public class ResponseResult<T> {
	private Integer code;
	private String msg;
	private long timestamp;
	private T data;

	public static <T> ResponseResult<T> success(T data) {
		return ResponseResult.<T>builder()
				.code(ResultCodeEnum.Success.getCode())
				.msg(ResultCodeEnum.Success.getMessage())
				.timestamp(System.currentTimeMillis())
				.data(data)
				.build();
	}
	public static <T> ResponseResult<T> fail(Integer code, String message) {
		return ResponseResult.<T>builder()
				.code(code)
				.msg(message)
				.timestamp(System.currentTimeMillis())
				.data(null)
				.build();
	}



}

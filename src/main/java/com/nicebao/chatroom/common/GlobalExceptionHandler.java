package com.nicebao.chatroom.common;

import com.nicebao.chatroom.enums.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理
 *
 * @name: GlobalExceptionHandler
 * @author: IhaveBB
 * @date: 2024-08-13 21:43
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseResult<String> handleException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);
		return ResponseResult.fail(ResultCodeEnum.SYSTEM_ERROR.getCode(), e.getMessage());
	}
}
/*
@RestControllerAdvice，RestController的增强类，可用于实现全局异常处理器
@ExceptionHandler,统一处理某一类异常，从而减少代码重复率和复杂度，比如要获取自定义异常可以@ExceptionHandler(BusinessException.class)
@ResponseStatus指定客户端收到的http状态码
 */
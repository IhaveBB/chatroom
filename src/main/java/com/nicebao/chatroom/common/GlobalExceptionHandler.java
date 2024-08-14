package com.nicebao.chatroom.common;

import com.nicebao.chatroom.enums.ResultCodeEnum;
import com.nicebao.chatroom.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
	@ResponseStatus(HttpStatus.OK)
	public ResponseResult<String> handleException(Exception ex) {
		log.error("全局异常信息 ex={}", ex.getMessage(), ex);
		return ResponseResult.fail(ResultCodeEnum.SYSTEM_ERROR.getCode(), ex.getMessage());
	}
	/** 
	* @description: 捕获vaild参数校验错误
	* @param: [org.springframework.web.bind.MethodArgumentNotValidException]
	* @return: com.nicebao.chatroom.common.ResponseResult<java.lang.String>
	* @author: IhaveBB
	* @date: 2024/8/14
	**/
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseResult<String> HandleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
		StringBuilder errorMessage = new StringBuilder();
		log.info("valid捕获错误信息 ex={}", ex.getMessage(), ex);
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMsg = error.getDefaultMessage();
			errorMessage.append(fieldName).append(": ").append(errorMsg).append("; ");
		});
		return ResponseResult.fail(ResultCodeEnum.PARAM_IS_ERROR.getCode(),errorMessage.toString());
	}
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(ServiceException.class)
	public  ResponseResult<String> HandleInvalidLoginException(ServiceException ex){
		return ResponseResult.fail(ex.getErrorCode(), ex.getMessage());
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(DuplicateKeyException.class)
	public ResponseResult<String> handleDuplicateKeyException(DuplicateKeyException ex){
		return ResponseResult.fail(ResultCodeEnum.USER_REGISTER_EXISTS.getCode(), ResultCodeEnum.USER_REGISTER_EXISTS.getMessage());
	}
}
/*
@RestControllerAdvice，RestController的增强类，可用于实现全局异常处理器
@ExceptionHandler,统一处理某一类异常，从而减少代码重复率和复杂度，比如要获取自定义异常可以@ExceptionHandler(BusinessException.class)
@ResponseStatus指定客户端收到的http状态码
 */
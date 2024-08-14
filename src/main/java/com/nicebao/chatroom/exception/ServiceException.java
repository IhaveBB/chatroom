package com.nicebao.chatroom.exception;

import com.nicebao.chatroom.enums.BaseErrorInfoInterface;
import lombok.Data;

/**
 * @name: ServiceException
 * @author: IhaveBB
 * @date: 2024-08-14 13:31
 **/
@Data
public class ServiceException extends RuntimeException{
	protected int errorCode;
	protected String errorMsg;
	/** 
	* @description: 自定义的异常方法
	* @param: [com.nicebao.chatroom.enums.BaseErrorInfoInterface]
	* @return:
	* @author: IhaveBB
	* @date: 2024/8/14 
	**/
	public ServiceException(BaseErrorInfoInterface errorInfoInterface) {
		super(errorInfoInterface.getMessage());
		this.errorMsg = errorInfoInterface.getMessage();
		this.errorCode = errorInfoInterface.getCode();
	}

	public ServiceException(BaseErrorInfoInterface errorInfoInterface,Throwable cause) {
		super(errorInfoInterface.getMessage(),cause);
		this.errorMsg = errorInfoInterface.getMessage();
		this.errorCode = errorInfoInterface.getCode();
	}
	

	public ServiceException() {
		super();
	}

	public ServiceException(String message) {
		super(message);
		this.errorMsg = message;
	}

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(Throwable cause) {
		super(cause);
	}
}

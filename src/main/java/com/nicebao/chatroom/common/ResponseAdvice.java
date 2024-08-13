package com.nicebao.chatroom.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 全局统一结果返回
 *
 * @name: ResponseAdvice
 * @author: IhaveBB
 * @date: 2024-08-13 22:39
 **/
@RestControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object o, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
		if(o instanceof String){
			try {
				return objectMapper.writeValueAsString(ResponseResult.success(o));
			} catch (JsonProcessingException e) {
				throw new RuntimeException(e);
			}
		}
		if(o instanceof ResponseResult){
			return o;
		}
		return ResponseResult.success(o);
	}
}

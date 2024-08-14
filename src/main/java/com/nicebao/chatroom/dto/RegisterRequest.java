package com.nicebao.chatroom.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * @name: registerRequest
 * @author: IhaveBB
 * @date: 2024-08-14 17:54
 **/
@Data
public class RegisterRequest {
	@NotBlank(message = "用户名不可为空")
	@Size(min = 5, max = 20, message = "用户名长度必须在5到20个字符之间")
	@Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
	private String username;

	@NotBlank(message = "密码不可为空")
	@Size(min = 8, max = 30, message = "密码长度必须在8到30个字符之间")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=!.]).+$",
			message = "密码必须包含至少一个小写字母、一个数字和一个特殊字符")
	private String password;
	private String email;
	private Integer gender;
}

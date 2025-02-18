package com.nicebao.chatroom.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserDTO {
	private String username;
	private String email; // 脱敏处理
	private Integer gender;
	private String phoneNumber; // 脱敏处理
	private String avatarUrl;
	private String personalizedSignature;
	private String region;
	private boolean isEmailVisible;
	private boolean isPhoneVisible;

	public UserDTO(User user, boolean isSelf) {
		this.username = user.getUsername();
		if (isSelf) {
			//自己始终可以看到完整的邮箱和电话
			this.email = user.getEmail();
			this.phoneNumber = user.getPhoneNumber();
		} else {
			//否则其他人能否看见的信息由用户设置决定
			this.email = user.getIsEmailVisible() ? maskEmail(user.getEmail()) : "Private";
			this.phoneNumber = user.getIsPhoneVisible() ? maskPhoneNumber(user.getPhoneNumber()) : "Private";
		}
		this.gender = user.getGender();
		this.avatarUrl = user.getAvatarUrl();
		this.personalizedSignature = user.getPersonalizedSignature();
		this.region = user.getRegion();
		this.isEmailVisible = user.getIsEmailVisible();
		this.isPhoneVisible = user.getIsPhoneVisible();
	}

	private String maskEmail(String email) {
		String[] parts = email.split("@");
		String maskedLocalPart = parts[0].charAt(0) + "****" + parts[0].charAt(parts[0].length() - 1);
		return maskedLocalPart + "@" + parts[1];
	}

	private String maskPhoneNumber(String phone) {
		return phone.replaceAll("(?<=\\d{3})\\d(?=\\d{4})", "*");
	}

}

package com.jmd.shopnet.entity;

public class SignInUser {

	private String nickName;
	private String email;
	private String userString;
	private Boolean signed; 

	public String getNickName() {
		return this.nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUserString() {
		return this.userString;
	}

	public void setUserString(String userString) {
		this.userString = userString;
	}

	public Boolean getSigned() {
		return this.signed;
	}

	public void setSigned(Boolean signed) {
		this.signed = signed;
	}
}

package com.jmd.shopnet.entity;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Embed
public class Friend {	
	@Id
    private Long id;
	@Index
	private String name;
	@Index
	private String fbId;
	@Index
    private String email;
	private String fbPictureURL;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFbId() {
		return fbId;
	}
	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFbPictureURL() {
		return fbPictureURL;
	}
	public void setFbPictureURL(String fbPictureURL) {
		this.fbPictureURL = fbPictureURL;
	}
	
}

package com.jmd.shopnet.entity;

import java.util.Date;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

public class CustomerComments {
	
	@Parent
	private Key<Customer> customer;
	
	@Id
    private Long id;
	
	private String comment;
	
	private Date createdate;
	private Date updatedate;
	
	public Key<Customer> getCustomer() {
		return customer;
	}
	public void setCustomer(Key<Customer> customer) {
		this.customer = customer;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public Date getUpdatedate() {
		return updatedate;
	}
	public void setUpdatedate(Date updatedate) {
		this.updatedate = updatedate;
	}
	
}

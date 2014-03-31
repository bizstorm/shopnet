package com.jmd.shopnet.entity;

import java.util.Date;

import lombok.Data;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Data
public class OfferComments {
	
	@Parent
	private Key<ProductOffer> productOffer;
	
	@Id
    private Long id;
	
	private Key<Customer> customer;
	
	private String comment;
	
	private Date createdate;
	private Date updatedate;
	
}

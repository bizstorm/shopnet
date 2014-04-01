package com.jmd.shopnet.entity;

import java.util.Date;

import lombok.Data;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Parent;

@Data
public class OfferComments {
	
	@Parent
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<ProductOffer> productOffer;
	
	@Id
    private Long id;
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Customer> customer;
	
	private String comment;
	
	private Date createdate;
	private Date updatedate;
	
}

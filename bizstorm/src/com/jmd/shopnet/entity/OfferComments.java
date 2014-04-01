package com.jmd.shopnet.entity;

import java.util.Date;

import lombok.Data;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
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
	private String commentedBy;
	
	@Index
	private Date createdOn;
	private Date updatedOn;
	
	public Long getCustomerId(){
		Long id = null;
		if(customer != null) id = customer.getId();
		return id;
	}
	
	public void setCustomerId(Long id){		
		if(id != null) customer = Key.create(Customer.class, id);
	}
	
	public Long getProductOfferId(){
		Long id = null;
		if(productOffer != null) id = productOffer.getId();
		return id;
	}
	
	public void setProductOfferId(Long id){		
		if(id != null) productOffer = Key.create(ProductOffer.class, id);
	}
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Key<OfferComments> geyKey() {
		return Key.create(productOffer, OfferComments.class, id);
	}
}

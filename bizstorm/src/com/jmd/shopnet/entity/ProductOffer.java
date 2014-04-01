package com.jmd.shopnet.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.jmd.shopnet.utils.Enumerators.OFFERTYPE;

@Data
@Entity
public class ProductOffer {

	@Parent
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Product> product;
	
	public Long getProduct(){
		return product.getId();
	}

	@Id
	private Long id;
	
	@Index
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Business> business;

	@Index
	private float price;

	private int quantity;

	@Index
	private OFFERTYPE offerType;

	private String message;

	private int customerRatingAvg;
	
	private int blacklistedCount;
	
	@Index
	private int access;
	
	@Index
	private Date createdDate;
	
	private Date modifiedDate;
	
	private List<OfferComments> comments;
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Key<ProductOffer> geyKey() {
		return Key.create(product, ProductOffer.class, id);
	}

}

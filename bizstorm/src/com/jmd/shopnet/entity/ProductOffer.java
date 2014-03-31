package com.jmd.shopnet.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Data
@Entity
public class ProductOffer {

	@Parent
	private Key<Product> product;

	@Id
	private Long id;
	
	@Index
	private Key<Business> business;

	@Index
	private float price;

	private int quantity;

	@Index
	private String offerType;

	private String message;

	private int customerRatingAvg;
	
	private int blacklistedCount;
	
	@Index
	private int access;
	
	@Index
	private Date createdDate;
	
	private Date modifiedDate;
	
	private List<OfferComments> comments;
	
	public Key<ProductOffer> geyKey() {
		return Key.create(product, ProductOffer.class, id);
	}

}

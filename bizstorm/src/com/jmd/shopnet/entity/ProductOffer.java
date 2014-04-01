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
	
	@Id
	private Long id;
	
	@Index
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Business> business;

	@Index
	private Float price;

	private Integer quantity;

	@Index
	private OFFERTYPE offerType;

	private String message;

	private Integer customerRatingAvg;
	
	private Integer blacklistedCount;
	
	@Index
	private Integer access;
	
	@Index
	private Date createdOn;
	
	private Date modifiedOn;
	
	private Integer status=1;
	
	private List<OfferComments> comments;
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Key<ProductOffer> geyKey() {
		return Key.create(product, ProductOffer.class, id);
	}
	
	public Long getProductId(){
		Long id = null;
		if(product != null) id = product.getId();
		return id;
	}
	
	public void setProductId(Long id){		
		if(id != null) product = Key.create(Product.class, id);
	}
	
	public Long getBusinessId(){
		Long id = null;
		if(business != null) id = business.getId();
		return id;
	}
	
	public void setBusiness(Long id){		
		if(id != null) business = Key.create(Business.class, id);
	}

}

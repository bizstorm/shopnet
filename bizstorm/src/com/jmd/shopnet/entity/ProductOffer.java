package com.jmd.shopnet.entity;

import java.util.List;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class ProductOffer {

	@Parent
	private Key<Business> businessKey;

	@Id
	private Long id;

	@Index
	private Key<Product> productKey;

	@Index
	private float price;

	private int quantity;

	@Index
	private String offerType;

	private String retailerMessage;

	private int customerRatingAvg;
	
	private int blacklistedCount;
	
	private List<CustomerComments> customerComments;

	
	public Key<Business> getBusinessKey() {
		return businessKey;
	}

	public void setBusinessKey(Key<Business> retailerKey) {
		this.businessKey = retailerKey;
	}

	public Key<Product> getProductKey() {
		return productKey;
	}

	public void setProductKey(Key<Product> productKey) {
		this.productKey = productKey;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getCurrentStock() {
		return this.quantity;
	}

	public void setCurrentStock(int currentStock) {
		this.quantity = currentStock;
	}

	public String getRetailerMessage() {
		return this.retailerMessage;
	}

	public void setRetailerMessage(String retailerMessage) {
		this.retailerMessage = retailerMessage;
	}

	public int getCustomerRatingAvg() {
		return this.customerRatingAvg;
	}

	public void setCustomerRatingAvg(int customerRatingAvg) {
		this.customerRatingAvg = customerRatingAvg;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOfferType() {
		return offerType;
	}

	public void setOfferType(String offerType) {
		this.offerType = offerType;
	}
	public int getBlacklistedCount() {
		return blacklistedCount;
	}

	public void setBlacklistedCount(int blacklistedCount) {
		this.blacklistedCount = blacklistedCount;
	}
	public List<CustomerComments> getCustomerComments() {
		return customerComments;
	}

	public void setCustomerComments(List<CustomerComments> customerComments) {
		this.customerComments = customerComments;
	}
}

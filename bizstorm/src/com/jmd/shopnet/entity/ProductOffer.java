package com.jmd.shopnet.entity;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class ProductOffer {

	@Parent
    private Key<Retailer> retailerKey;

	@Id
    private Long id;

	@Index
    private Key<Product> productKey;

    @Index
    private float price;

    private int currentStock;

    private String retailerMessage;

    private int customerRatingAvg;

	public Key<Retailer> getRetailerKey() {
		return retailerKey;
	}

	public void setRetailerKey(Key<Retailer> retailerKey) {
		this.retailerKey = retailerKey;
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
        return this.currentStock;
    }

	public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
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

}

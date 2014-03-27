package com.jmd.shopnet.entity;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Parent;
import com.googlecode.objectify.annotation.Load;

@Entity
public class Bookmark {

	@Id
    private Long id;
	
	@Parent @Load
	private Ref<Customer> customer;

	
	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }


	
	public void setCustomer(Customer customer) {
		if(customer != null) {
			this.customer = Ref.create(customer);
		} else {
			this.customer = null;
		}
	}
	
	public Customer getCustomer() {
		return this.customer.get();
	}
	
	@Override
	public String toString() {
		return "Bookmark [id=" + id + ", customer=" + customer  + "]";
	}
}

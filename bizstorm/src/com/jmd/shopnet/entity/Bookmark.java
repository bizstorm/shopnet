package com.jmd.shopnet.entity;

import lombok.Data;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Data
@Entity
public class Bookmark {

	@Id
    private Long id;
	
	@Parent @Load
	private Ref<Customer> customer;

	
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
	
	public Key<Bookmark> geyKey() {
		return Key.create(customer.getKey(), Bookmark.class, id);
	}

}

package com.jmd.shopnet.entity;

import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.Parent;

@Entity
public class CustomerQuery {

	@Id
    private Long id;
	
	@Parent @Load 
	private Ref<Customer> customer;
	
	private String customerQuery;
	
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
	
	public String getCustomerQuery() {
        return this.customerQuery;
    }

	public void setCustomerQuery(String customerQuery) {
        this.customerQuery = customerQuery;
    }

	@Override
	public String toString() {
		return "CustomerQuery [id=" + id + ", customer=" + customer + ", customerQuery=" + customerQuery + "]";
	}
}
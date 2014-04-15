package com.jmd.shopnet.service;

import java.util.List;

import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.dao.CustomerDAO;
import com.jmd.shopnet.entity.Customer;

public class CustomerService {
private CustomerDAO customerDAO;
	
	public List<Customer> getCustomers(User user) {
		List<Customer> customerList = customerDAO.getByUser(user);
		return customerList;
	}
	
	
	
	
	public Customer createCustomer(Customer customer) {
		if(customer != null){			
				try{
					Key<Customer> customerKey = customerDAO.saveEntity(customer);
					if(customerKey != null){
						customer = customerDAO.getEntity(customerKey.getId());
					}
				}catch(Exception e){
					e.printStackTrace();
				}
		}
		return customer;
	}

	public boolean updateCustomer(Customer customer) {
		Boolean updated = false;
		if(customer != null){
			if(customer.getId() != null){
				Key<Customer> customerKey = customerDAO.saveEntity(customer);
				if(customerKey != null){
					updated = true;
				}
			}
		}
		return updated;
	}

	public boolean deleteCustomer(Long customerId) {
		if(customerId != null){
			customerDAO.deleteEntity(customerId);
		}
		return true;
	}
	

	@Inject
	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

}

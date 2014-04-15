package com.jmd.shopnet.api;

import java.util.List;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.service.CustomerService;
import com.jmd.shopnet.utils.Ids;

@Api(name = "customer"
,version = "v1"
,clientIds = {Ids.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
,scopes = {Ids.SCOPES}
)
public class CustomerAPI {
private CustomerService customerService;
	
	

	@ApiMethod(name="getCustomer", httpMethod = "GET" , path="get")
	public Customer getCustomer(User user
				) throws OAuthRequestException {
		if(user == null){
			throw new OAuthRequestException("User must login ");
		}
		Customer cust = null;
		List<Customer> customers = customerService.getCustomers(user);
		if(customers != null && customers.size() > 0){
			cust = customers.get(0);
		}
		return cust;
	}
	
	@ApiMethod(name="createCustomer", httpMethod = "POST" , path="new")
	public Customer createCustomer(				
			User user, Customer customer) throws Exception {
		if(user == null){
			throw new OAuthRequestException("User must login ");
		}
		customer.setUser(user);
		customer.setEmail(user.getEmail());
		List<Customer> customers = customerService.getCustomers(user);
		if(customers.size() > 0){
			throw new Exception("Customer is registered");
		}else{
			
		}
		Customer cust = customerService.createCustomer(customer);
		
		return cust;
	}
	
	@ApiMethod(name="updateCustomer", httpMethod = "POST" , path="update")
	public void updateCustomer(				
				User user, Customer customer) throws OAuthRequestException {
		if(user == null){
			throw new OAuthRequestException("User must login ");
		}
		customer.setEmail(user.getEmail());
		customer.setUser(user);
		customerService.updateCustomer(customer);
	}
	
	@ApiMethod(name="deleteCustomer", httpMethod = "POST" , path="delete/{id}")
	public void deleteCustomer(				
			User user ,@Named("id") Long customerId ) throws OAuthRequestException {
		if(user == null){
			throw new OAuthRequestException("User must login ");
		}
		 customerService.deleteCustomer(customerId);
	}
			
	
	@Inject
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}
}

package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.jmd.shopnet.entity.Customer;
import com.google.appengine.api.users.User;

public class CustomerOfy {
	protected Logger log = Logger.getLogger(CustomerOfy.class.getName());
	public Key<Customer> saveEntity(Customer customer) {
	    return ofy().save().entity(customer).now();
	}
	public Map<Key<Customer>, Customer> saveEntitiesNow(List<Customer> customer) {
	    return ofy().save().entities(customer).now();
	}
	public void saveEntityAsync(Customer customer) {
	    ofy().save().entity(customer);
	}
	public void saveEntitiesAsync(List<Customer> customers) {
	    ofy().save().entities(customers);
	}
	public List<Customer> fetchCustomers(Integer limit) {
		List<Customer> customers = ofy().load().type(Customer.class)
				.limit(limit).list();
		return customers;
	}
	public Customer fetchCustomer(Long id) {
		Customer customer = ofy().load().type(Customer.class).id(id).now();
		return customer;
	}
	public Customer fetchCustomerSafe(Long id) {
		Customer customer = ofy().load().type(Customer.class).id(id).safe();
		return customer;
	}
	public List<Customer> fetchByName(String name) {
	    return ofy().load().type(Customer.class).filter("name", name).list();
	}
	public List<Customer> fetchByEmail(String email) {
	    return ofy().load().type(Customer.class).filter("email", email).list();
	}
	public List<Customer> fetchByPhone(String phone) {
	    return ofy().load().type(Customer.class).filter("phone", phone).list();
	}
	public List<Customer> fetchByFbId(String fbId) {
	    return ofy().load().type(Customer.class).filter("fbId", fbId).list();
	}
	public List<Customer> fetchByDOB(String dob) {
	    return ofy().load().type(Customer.class).filter("dob", dob).list();
	}
	public List<Customer> fetchByUser(User user) {
	    return ofy().load().type(Customer.class).filter("user", user).list();
	}
	public void deleteEntity(Customer customer) {
	    ofy().delete().entity(customer).now();
	}
	public void deleteEntity(Long id) {
	    ofy().delete().type(Customer.class).id(id).now();
	}
	public void deleteEntities(List<Customer> customers) {
	    log.info("Deleting customers: " + customers.size());
	    ofy().delete().entities(customers);
	}
	public void deleteEntities(Integer limit) {
	    List<Customer> customersToDelete = fetchCustomers(limit);
	    log.info("Deleting all customers: " + customersToDelete.size());
		ofy().delete().entities(customersToDelete);
	}
}

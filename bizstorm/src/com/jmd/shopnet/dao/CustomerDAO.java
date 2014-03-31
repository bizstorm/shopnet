package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.entity.Customer;

public class CustomerDAO {
	protected Logger log = Logger.getLogger(CustomerDAO.class.getName());
	public Key<Customer> saveEntity(Customer entity) {
	    return ofy().save().entity(entity).now();
	}
	
	public Map<Key<Customer>, Customer> saveEntities(List<Customer> entities) {
	    return ofy().save().entities(entities).now();
	}
	
	public void saveEntityAsync(Customer entity) {
	    ofy().save().entity(entity);
	}
	
	public void saveEntitiesAsync(List<Customer> entities) {
	    ofy().save().entities(entities);
	}
	
	public List<Customer> getAllEntities(Integer offset, Integer limit) {
		List<Customer> entities = ofy().load().type(Customer.class)				
				.offset(offset).limit(limit).list();
		return entities;
	}
	
	
	public Customer getEntity(Long id) {
		Customer entity = ofy().load().type(Customer.class).id(id).now();
		return entity;
	}
	
	public Map<Long, Customer> getEntities(List<Long> ids) {
		Map<Long, Customer> entities = ofy().load().type(Customer.class).ids(ids);
		return entities;
	}
	
	public Customer getEntitySafe(Long id) {
		Customer entity = ofy().load().type(Customer.class).id(id).safe();
		return entity;
	}
	
	
	public void deleteEntity(Customer entity) {
	    ofy().delete().entity(entity).now();
	}
	
	public void deleteEntity(Long id) {
	    ofy().delete().type(Customer.class).id(id).now();
	}
	
	public void deleteEntities(List<Customer> entities) {
	    log.info("Deleting entities: " + entities.size());
	    ofy().delete().entities(entities);
	}
	
	public List<Customer> deleteEntities(Key<Customer> parent, Integer offset, Integer limit) {
	    List<Customer> entitiesToDelete = getAllEntities(offset, limit);
	    log.info("Deleting all entities: " + entitiesToDelete.size());
		ofy().delete().entities(entitiesToDelete);
		return entitiesToDelete;
	}
	
	public List<Customer> getByName(String name) {
	    return ofy().load().type(Customer.class).filter("name", name).list();
	}
	
	public List<Customer> getByEmail(String email) {
	    return ofy().load().type(Customer.class).filter("email", email).list();
	}
	
	public List<Customer> getByPhone(String phone) {
	    return ofy().load().type(Customer.class).filter("phone", phone).list();
	}
	
	public List<Customer> getByFbId(String fbId) {
	    return ofy().load().type(Customer.class).filter("fbId", fbId).list();
	}
	
	public List<Customer> getByUser(User user) {
	    return ofy().load().type(Customer.class).filter("user", user).list();
	}
}

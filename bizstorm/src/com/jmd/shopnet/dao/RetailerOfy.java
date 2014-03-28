package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Category;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.entity.Business;

public class RetailerOfy {
	protected Logger log = Logger.getLogger(RetailerOfy.class.getName());
	public Key<Business> saveEntity(Business retailer) {
	    return ofy().save().entity(retailer).now();
	}
	public Map<Key<Business>, Business> saveEntitiesNow(List<Business> retailers) {
	    return ofy().save().entities(retailers).now();
	}
	public void saveEntityAsync(Business retailer) {
	    ofy().save().entity(retailer);
	}
	public void saveEntitiesAsync(List<Business> retailers) {
	    ofy().save().entities(retailers);
	}
	public List<Business> fetchRetailers(Integer limit) {
		// User user = UserServiceFactory.getUserService().getCurrentUser();
		List<Business> retailers = ofy().load().type(Business.class)
				.limit(limit).list();// TODO geospatial query on search API
		return retailers;
	}
	public Business fetchRetailer(Long id) {
		Business retailer = ofy().load().type(Business.class).id(id).now();
		return retailer;
	}
	public Business fetchRetailerSafe(Long id) {
		Business retailer = ofy().load().type(Business.class).id(id).safe();
		return retailer;
	}
	public List<Business> fetchBySmsNumbers(String smsNumber) {
	    return ofy().load().type(Business.class).filter("smsNumbers", smsNumber).list();
	}
	public List<Business> fetchByPhoneNumbers(String smsNumber) {
	    return ofy().load().type(Business.class).filter("phoneNumbers", smsNumber).list();
	}
	public List<Business> fetchByCategory(Category category) {
	    return ofy().load().type(Business.class).filter("categories", category).list();
	}
	public List<Business> fetchByCategories(List<Category> categories) {
	    return ofy().load().type(Business.class).filter("categories in: ", categories).list();
	}
	public List<Business> fetchByUserEmail(String addedByEmail) {
	    return ofy().load().type(Business.class).filter("addedByEmail", addedByEmail).list();
	}
	public void deleteEntity(Business retailer) {
	    ofy().delete().entity(retailer).now();
	}
	public void deleteEntity(Long id) {
	    ofy().delete().type(Business.class).id(id).now();
	}
	public void deleteEntities(List<Business> retailers) {
	    log.info("Deleting retailers: " + retailers.size());
	    ofy().delete().entities(retailers);
	}
	public List<Business> deleteEntities(Integer limit) {
	    List<Business> retailersToDelete = fetchRetailers(limit);
	    log.info("Deleting all retailers: " + retailersToDelete.size());
		ofy().delete().entities(retailersToDelete);
		return retailersToDelete;
	}
}

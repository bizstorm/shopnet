package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Category;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.entity.Retailer;

public class RetailerOfy {
	protected Logger log = Logger.getLogger(RetailerOfy.class.getName());
	public Key<Retailer> saveEntity(Retailer retailer) {
	    return ofy().save().entity(retailer).now();
	}
	public Map<Key<Retailer>, Retailer> saveEntitiesNow(List<Retailer> retailers) {
	    return ofy().save().entities(retailers).now();
	}
	public void saveEntityAsync(Retailer retailer) {
	    ofy().save().entity(retailer);
	}
	public void saveEntitiesAsync(List<Retailer> retailers) {
	    ofy().save().entities(retailers);
	}
	public List<Retailer> fetchRetailers(Integer limit) {
		// User user = UserServiceFactory.getUserService().getCurrentUser();
		List<Retailer> retailers = ofy().load().type(Retailer.class)
				.limit(limit).list();// TODO geospatial query on search API
		return retailers;
	}
	public Retailer fetchRetailer(Long id) {
		Retailer retailer = ofy().load().type(Retailer.class).id(id).now();
		return retailer;
	}
	public Retailer fetchRetailerSafe(Long id) {
		Retailer retailer = ofy().load().type(Retailer.class).id(id).safe();
		return retailer;
	}
	public List<Retailer> fetchBySmsNumbers(String smsNumber) {
	    return ofy().load().type(Retailer.class).filter("smsNumbers", smsNumber).list();
	}
	public List<Retailer> fetchByPhoneNumbers(String smsNumber) {
	    return ofy().load().type(Retailer.class).filter("phoneNumbers", smsNumber).list();
	}
	public List<Retailer> fetchByCategory(Category category) {
	    return ofy().load().type(Retailer.class).filter("categories", category).list();
	}
	public List<Retailer> fetchByCategories(List<Category> categories) {
	    return ofy().load().type(Retailer.class).filter("categories in: ", categories).list();
	}
	public List<Retailer> fetchByUserEmail(String addedByEmail) {
	    return ofy().load().type(Retailer.class).filter("addedByEmail", addedByEmail).list();
	}
	public void deleteEntity(Retailer retailer) {
	    ofy().delete().entity(retailer).now();
	}
	public void deleteEntity(Long id) {
	    ofy().delete().type(Retailer.class).id(id).now();
	}
	public void deleteEntities(List<Retailer> retailers) {
	    log.info("Deleting retailers: " + retailers.size());
	    ofy().delete().entities(retailers);
	}
	public List<Retailer> deleteEntities(Integer limit) {
	    List<Retailer> retailersToDelete = fetchRetailers(limit);
	    log.info("Deleting all retailers: " + retailersToDelete.size());
		ofy().delete().entities(retailersToDelete);
		return retailersToDelete;
	}
}

package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Category;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.LoadType;
import com.googlecode.objectify.cmd.Loader;
import com.googlecode.objectify.cmd.Query;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.utils.StringConversions;
import com.jmd.shopnet.vo.BusinessParams;

public class BusinessDAO {
	protected Logger log = Logger.getLogger(BusinessDAO.class.getName());
	
	
	public Key<Business> saveEntity(Business entity) {
	    return ofy().save().entity(entity).now();
	}
	
	public Map<Key<Business>, Business> saveEntities(List<Business> entities) {
	    return ofy().save().entities(entities).now();
	}
	
	public void saveEntityAsync(Business entity) {
	    ofy().save().entity(entity);
	}
	
	public void saveEntitiesAsync(List<Business> entities) {
	    ofy().save().entities(entities);
	}
	
	public List<Business> getAllEntities(Key<Customer> parent, Integer offset, Integer limit) {
		List<Business> entities = ofy().load().type(Business.class)
				.ancestor(parent)
				.offset(offset).limit(limit).order("businessName").list();
		return entities;
	}
	
	public Business getEntity(Long id) {
		Business entity = ofy().load().type(Business.class).id(id).now();
		return entity;
	}
	
	public Map<Long, Business> getEntities(List<Long> ids) {
		Map<Long, Business> entities = ofy().load().type(Business.class).ids(ids);
		return entities;
	}
	
	public Business getEntitySafe(Long id) {
		Business entity = ofy().load().type(Business.class).id(id).safe();
		return entity;
	}
	
	public List<Business> getBySmsNumbers(String smsNumber) {
	    return ofy().load().type(Business.class).filter("smsNumbers", smsNumber).list();
	}
	
	public List<Business> getByPhoneNumbers(String smsNumber) {
	    return ofy().load().type(Business.class).filter("phoneNumbers", smsNumber).list();
	}
	
	public List<Business> getByCategory(Category category) {
	    return ofy().load().type(Business.class).filter("categories", category).list();
	}
	
	public List<Business> getByCategories(List<Category> categories) {
	    return ofy().load().type(Business.class).filter("categories in: ", categories).list();
	}
	
	public List<Business> getMyEntitiesByCategory(Key<Customer> parent,Category category) {
		List<Business> entities = ofy().load().type(Business.class)
				.ancestor(parent)
				.filter("categories", category)
				.list();
		return entities;
	}
	
	public void deleteEntity(Business entity) {
	    ofy().delete().entity(entity).now();
	}
	
	public void deleteEntity(Long id) {
	    ofy().delete().type(Business.class).id(id).now();
	}
	
	public void deleteEntities(List<Business> entities) {
	    log.info("Deleting entities: " + entities.size());
	    ofy().delete().entities(entities);
	}
	
	public List<Business> deleteEntities(Key<Customer> parent, Integer offset, Integer limit) {
	    List<Business> entitiesToDelete = getAllEntities(parent, offset, limit);
	    log.info("Deleting all entities: " + entitiesToDelete.size());
		ofy().delete().entities(entitiesToDelete);
		return entitiesToDelete;
	}
	
	public List<Business> getMyBusinessByType(Key<Customer> parent, String businessType) {
		List<Business> entities = ofy().load().type(Business.class)
				.ancestor(parent)
				.filter("businessType", businessType)
				.list();
		return entities;
	}
	
	public List<Business> getMyBusiness(Key<Customer> parent) {
		List<Business> entities = ofy().load().type(Business.class)
				.ancestor(parent)
				.list();
		return entities;
	}
	
	public List<Business> getEntityByType(String businessType) {
		List<Business> entities = ofy().load().type(Business.class)
				.filter("businessType", businessType)
				.list();
		return entities;
	}
	
	public List<Business> getBusinessListByParams(BusinessParams params) {
		List<Business> entities = null;
		Query<Business> q = getBusinessQueryByParams(params);
		entities = q.list();
		return entities;
	}
	
		
	public List<Key<Business>> getBusinessKeysByParams(BusinessParams params) {
		List<Key<Business>> entities = null;
		Query<Business> q = getBusinessQueryByParams(params);
		entities = q.keys().list();
		return entities;
	}
	
	public Map<Key<Business>, Business> loadKeysByParams(List<Key<Business>> keys, BusinessParams bParams) {
		Map<Key<Business>, Business> entities  = null;;
		Loader q = ofy().load();
		if(bParams.getKeys() != null && bParams.getKeys().size() > 0){
			q.filterKey("<>", bParams.getKeys());
		}
		if(bParams.getOffset() != null){
			q.offset(bParams.getOffset());
		}
		if(bParams.getLimit() != null){
			q.limit(bParams.getLimit());
		}
		entities  = q.keys(keys);
		return entities;
	}
	
	
	public Query<Business> getBusinessQueryByParams(BusinessParams bParams) {
		Query<Business> q = ofy().load().type(Business.class);
		if(bParams.getKeys() != null && bParams.getKeys().size() > 0){
			q.filterKey("in", bParams.getKeys());
		}	
		
		if(bParams.getBusinessName() != null && bParams.getBusinessName().length() > 0){
			q.filter("businessName", bParams.getBusinessName());
		}
		if(bParams.getBusinessTypes() != null && bParams.getBusinessTypes().size() > 0){
			q.filter("businessType in", StringConversions.extractCategoriesFromList(bParams.getBusinessTypes()));
		}
		if(bParams.getIndustries() != null && bParams.getIndustries().size() > 0){
			q.filter("industries in", StringConversions.extractCategoriesFromList(bParams.getIndustries()));
		}
		if(bParams.getCategoryTags() != null && bParams.getCategoryTags().size() > 0){
			q.filter("categories in", StringConversions.extractCategoriesFromList(bParams.getCategoryTags()));
		}
		if(bParams.getBrandTags() != null && bParams.getBrandTags().size() > 0){
			q.filter("brands in", StringConversions.extractCategoriesFromList(bParams.getBrandTags()));
		}
		if(bParams.getProductTags() != null && bParams.getProductTags().size() > 0){
			q.filter("products in", StringConversions.extractCategoriesFromList(bParams.getProductTags()));
		}
		if(bParams.getPhoneNumber() != null && bParams.getPhoneNumber().length() > 0){
			q.filter("smsNumbers in", StringConversions.extractPhonesFromCsv(bParams.getPhoneNumber()));
		}
		if(bParams.getEmailId() != null && bParams.getEmailId().length() > 0){
			q.filter("emailids in", StringConversions.extractEmailsFromCsv(bParams.getEmailId()));
		}
		if(bParams.getCity() != null && bParams.getCity().length() > 0){
			q.filter("city", bParams.getCity());
		}
		if(bParams.getCountry() != null && bParams.getCountry().length() > 0){
			q.filter("country", bParams.getCountry());
		}
		if(bParams.getAccess() != null && bParams.getAccess().size() > 0){
			q.filter("access in", bParams.getAccess());
		}		
		if(bParams.getOrderby() != null && bParams.getOrderby().length() > 0){
			q.order(bParams.getOrderby());
		}
		if(bParams.getOffset() != null){
			q.offset(bParams.getOffset());
		}
		if(bParams.getLimit() != null){
			q.limit(bParams.getLimit());
		}
		return q;
	}
	
	
}

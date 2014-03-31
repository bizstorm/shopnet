package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Loader;
import com.googlecode.objectify.cmd.Query;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.entity.ProductOffer;
import com.jmd.shopnet.vo.OfferParams;

public class ProductOfferDAO {
	protected Logger log = Logger.getLogger(ProductOfferDAO.class.getName());
	
	
	public Key<ProductOffer> saveEntity(ProductOffer entity) {
	    return ofy().save().entity(entity).now();
	}
	
	public Map<Key<ProductOffer>, ProductOffer> saveEntities(List<ProductOffer> entities) {
	    return ofy().save().entities(entities).now();
	}
	
	public void saveEntityAsync(ProductOffer entity) {
	    ofy().save().entity(entity);
	}
	
	public void saveEntitiesAsync(List<ProductOffer> entities) {
	    ofy().save().entities(entities);
	}
	
	public List<ProductOffer> getAllEntities(Key<Product> parent, String offerType,  Integer offset, Integer limit) {
		List<ProductOffer> entities = ofy().load().type(ProductOffer.class)
				.ancestor(parent)
				.filter("offerType", offerType)
				.order("price")
				.offset(offset).limit(limit).list();
		return entities;
	}
	
	public ProductOffer getEntity(Long id) {
		ProductOffer entity = ofy().load().type(ProductOffer.class).id(id).now();
		return entity;
	}
	
	public Map<Long, ProductOffer> getEntities(List<Long> ids) {
		Map<Long, ProductOffer> entities = ofy().load().type(ProductOffer.class).ids(ids);
		return entities;
	}
	
	public ProductOffer getEntitySafe(Long id) {
		ProductOffer entity = ofy().load().type(ProductOffer.class).id(id).safe();
		return entity;
	}
	
	
	public void deleteEntity(ProductOffer entity) {
	    ofy().delete().entity(entity).now();
	}
	
	public void deleteEntity(Long id) {
	    ofy().delete().type(ProductOffer.class).id(id).now();
	}
	
	public void deleteEntities(List<ProductOffer> entities) {
	    log.info("Deleting entities: " + entities.size());
	    ofy().delete().entities(entities);
	}
	
	public List<ProductOffer> deleteEntities(Key<Product> parent, String offerType, Integer offset, Integer limit) {
	    List<ProductOffer> entitiesToDelete = getAllEntities(parent,offerType, offset, limit);
	    log.info("Deleting all entities: " + entitiesToDelete.size());
		ofy().delete().entities(entitiesToDelete);
		return entitiesToDelete;
	}
	

	public List<ProductOffer> getOfferListByParams(Set<Key<Business>> businessKeys, Set<Key<Product>> productKeys,
			OfferParams oParams) {
		List<ProductOffer> entities = null;
		Query<ProductOffer> q = getOfferQueryByParams(businessKeys, productKeys, oParams);
		entities = q.list();
		return entities;
	}
	
		
	public List<Key<ProductOffer>> getOfferKeysByParams(Set<Key<Business>> businessKeys, Set<Key<Product>> productKeys,
			OfferParams oParams) {
		List<Key<ProductOffer>> entities = null;
		Query<ProductOffer> q = getOfferQueryByParams(businessKeys, productKeys, oParams);
		entities = q.keys().list();
		return entities;
	}
	
	public Map<Key<ProductOffer>, ProductOffer> loadKeysByParams(List<Key<ProductOffer>> keys, OfferParams oParams) {
		Map<Key<ProductOffer>, ProductOffer> entities  = null;;
		Loader q = ofy().load();
	
		if(oParams.getOffset() != null){
			q.offset(oParams.getOffset());
		}
		if(oParams.getLimit() != null){
			q.limit(oParams.getLimit());
		}
		entities  = q.keys(keys);
		return entities;
	}
	
	
	public Query<ProductOffer> getOfferQueryByParams(Set<Key<Business>> businessKeys, Set<Key<Product>> productKeys,
			OfferParams oParams) {
		Query<ProductOffer> q = ofy().load().type(ProductOffer.class);
		
		if(businessKeys != null && businessKeys.size() > 0){
			q.filterKey("business in",businessKeys);
		}
		if(productKeys != null && productKeys.size() > 0){
			q.filterKey("product in",productKeys);
		}
		
		if(oParams.getOfferType()!= null && oParams.getOfferType().length() > 0){
			q.filter("offerType", oParams.getOfferType());
		}
		if(oParams.getPriceRange()!= null){
			if(oParams.getPriceRange().getMin() != null){
				q.filter("price >=", oParams.getPriceRange().getMin());
			}
			if(oParams.getPriceRange().getMax() != null){
				q.filter("price <", oParams.getPriceRange().getMax());
			}
		}
		
		if(oParams.getCreatedDateRange()!= null){
			if(oParams.getCreatedDateRange().getMin() != null){
				q.filter("createdDate >=", oParams.getCreatedDateRange().getMin());
			}
			if(oParams.getCreatedDateRange().getMax() != null){
				q.filter("createdDate <", oParams.getCreatedDateRange().getMax());
			}
		}
		
		if(oParams.getModifiedDateRange()!= null){
			if(oParams.getModifiedDateRange().getMin() != null){
				q.filter("modifiedDate >=", oParams.getModifiedDateRange().getMin());
			}
			if(oParams.getModifiedDateRange().getMax() != null){
				q.filter("modifiedDate <", oParams.getModifiedDateRange().getMax());
			}
		}
		
		
		if(oParams.getOrderby() != null && oParams.getOrderby().length() > 0){
			q.order(oParams.getOrderby());
		}
		if(oParams.getOffset() != null){
			q.offset(oParams.getOffset());
		}
		if(oParams.getLimit() != null){
			q.limit(oParams.getLimit());
		}
		return q;
	}
	
		
}

package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
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
	
	public List<ProductOffer> getAllOffers(List<Key<Business>> members, List<Key<Product>> products,  String offerType, String orderBy,  Integer offset, Integer limit ) {
		List<ProductOffer> entities = ofy().load().type(ProductOffer.class)
				.filterKey("business in:", members)
				.filterKey("product in:", products)
				.filter("offerType", offerType)
				.order(orderBy)
				.offset(offset).limit(limit).list();
		return entities;
	}

	public List<ProductOffer> getOfferByParams(
			Set<Key<Business>> businessKeys, Set<Key<Product>> productKeys,
			OfferParams oParams) {
		// TODO Auto-generated method stub
		return null;
	}
	
		
}

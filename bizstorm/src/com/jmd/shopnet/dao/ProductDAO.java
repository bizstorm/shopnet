package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Category;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Loader;
import com.googlecode.objectify.cmd.Query;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.utils.StringConversions;
import com.jmd.shopnet.vo.ProductParams;

public class ProductDAO {
	protected Logger log = Logger.getLogger(ProductDAO.class.getName());
	
	public Key<Product> saveEntity(Product entity) {
	    return ofy().save().entity(entity).now();
	}
	
	public Map<Key<Product>, Product> saveEntities(List<Product> entities) {
	    return ofy().save().entities(entities).now();
	}
	
	public void saveEntityAsync(Product entity) {
	    ofy().save().entity(entity);
	}
	
	public void saveEntitiesAsync(List<Product> entities) {
	    ofy().save().entities(entities);
	}
	
	public List<Product> getAllEntities(Key<Customer> parent, Integer offset, Integer limit) {
		List<Product> entities = ofy().load().type(Product.class)
				.ancestor(parent)
				.offset(offset).limit(limit).order("name").list();
		return entities;
	}
	
	public Product getEntity(Long id) {
		Product entity = ofy().load().type(Product.class).id(id).now();
		return entity;
	}
	
	public Map<Long, Product> getEntities(List<Long> ids) {
		Map<Long, Product> entities = ofy().load().type(Product.class).ids(ids);
		return entities;
	}
	
	public Product getEntitySafe(Long id) {
		Product entity = ofy().load().type(Product.class).id(id).safe();
		return entity;
	}
	
	public List<Product> getByIndustry(Category insdutry) {
	    return ofy().load().type(Product.class).filter("industry", insdutry).list();
	}
	
	public List<Product> getByBrand(Category brand) {
	    return ofy().load().type(Product.class).filter("brand", brand).list();
	}	
	
	public List<Product> getByCategory(Category category) {
	    return ofy().load().type(Product.class).filter("categories", category).list();
	}
	
	public List<Product> getByCategories(List<Category> categories) {
	    return ofy().load().type(Product.class).filter("categories in: ", categories).list();
	}
	
	public List<Product> getMyEntitiesByCategory(Key<Customer> parent,Category category) {
		List<Product> entities = ofy().load().type(Product.class)
				.ancestor(parent)
				.filter("categories", category)
				.list();
		return entities;
	}
	
	public void deleteEntity(Product entity) {
	    ofy().delete().entity(entity).now();
	}
	
	public void deleteEntity(Long id) {
	    ofy().delete().type(Product.class).id(id).now();
	}
	
	public void deleteEntities(List<Product> entities) {
	    log.info("Deleting entities: " + entities.size());
	    ofy().delete().entities(entities);
	}
	
	public List<Product> deleteEntities(Key<Customer> parent, Integer offset, Integer limit) {
	    List<Product> entitiesToDelete = getAllEntities(parent, offset, limit);
	    log.info("Deleting all entities: " + entitiesToDelete.size());
		ofy().delete().entities(entitiesToDelete);
		return entitiesToDelete;
	}
	
	public List<Product> getProductListByParams(ProductParams params) {
		List<Product> entities = null;
		Query<Product> q = getProductQueryByParams(params);
		entities = q.list();
		return entities;
	}
	
		
	public List<Key<Product>> getProductKeysByParams(ProductParams params) {
		List<Key<Product>> entities = null;
		Query<Product> q = getProductQueryByParams(params);
		entities = q.keys().list();
		return entities;
	}
	
	public Map<Key<Product>, Product> loadKeysByParams(List<Key<Product>> keys, ProductParams bParams) {
		Map<Key<Product>, Product> entities  = null;;
		Loader q = ofy().load();
		if(bParams != null){
			if(bParams.getOffset() != null){
				q.offset(bParams.getOffset());
			}
			if(bParams.getLimit() != null){
				q.limit(bParams.getLimit());
			}
		}
		
		entities  = q.keys(keys);
		return entities;
	}
	
	public List<Product> getListByKeys(List<Key<Product>> keys) {
		Map<Key<Product>, Product> result =  loadKeysByParams(keys,null);
		if(result != null){
			return (List) result.values();
		}else{
			return null;
		}
	}

	public Query<Product> getProductQueryByParams(ProductParams params) {
		Query<Product> q = ofy().load().type(Product.class);
		
		if(params.getIndustries() != null && params.getIndustries().size() > 0){
			q.filter("industry",StringConversions.extractCategoriesFromList( params.getIndustries()));
		}
		if(params.getCategoryTags() != null && params.getCategoryTags().size() > 0){
			q.filter("category in", StringConversions.extractCategoriesFromList(params.getCategoryTags()));
		}
		if(params.getBrandTags() != null && params.getBrandTags().size() > 0){
			q.filter("brand in", StringConversions.extractCategoriesFromList(params.getBrandTags()));
		}
		if(params.getNameTags() != null && params.getNameTags().size() > 0){
			q.filter("name in", StringConversions.extractCategoriesFromList(params.getNameTags()));
		}
		if(params.getAttributeTags() != null && params.getAttributeTags().size() > 0){
			q.filter("keywords in", StringConversions.extractCategoriesFromList(params.getAttributeTags()));
		}
		
		if(params.getOrderby() != null && params.getOrderby().length() > 0){
			q.order(params.getOrderby());
		}
		if(params.getOffset() != null){
			q.offset(params.getOffset());
		}
		if(params.getLimit() != null){
			q.limit(params.getLimit());
		}
		return q;
	}	

}

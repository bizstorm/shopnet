package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.jmd.shopnet.entity.Bookmark;
import com.jmd.shopnet.entity.Customer;


public class BookmarkDAO {
	protected Logger log = Logger.getLogger(BookmarkDAO.class.getName());

	public Key<Bookmark> saveEntity(Bookmark entity) {
	    return ofy().save().entity(entity).now();
	}
	
	public Map<Key<Bookmark>, Bookmark> saveEntities(List<Bookmark> entities) {
	    return ofy().save().entities(entities).now();
	}
	
	public void saveEntityAsync(Bookmark entity) {
	    ofy().save().entity(entity);
	}
	
	public void saveEntitiesAsync(List<Bookmark> entities) {
	    ofy().save().entities(entities);
	}
	
	public List<Bookmark> getAllEntities(Key<Customer> parent, Integer offset, Integer limit) {
		List<Bookmark> entities = ofy().load().type(Bookmark.class)
				.ancestor(parent)
				.offset(offset).limit(limit).list();
		return entities;
	}
	
	public Bookmark getEntity(Long id) {
		Bookmark entity = ofy().load().type(Bookmark.class).id(id).now();
		return entity;
	}
	
	public Map<Long, Bookmark> getEntities(List<Long> ids) {
		Map<Long, Bookmark> entities = ofy().load().type(Bookmark.class).ids(ids);
		return entities;
	}
	
	public Bookmark getEntitySafe(Long id) {
		Bookmark entity = ofy().load().type(Bookmark.class).id(id).safe();
		return entity;
	}
	
	
	public void deleteEntity(Bookmark entity) {
	    ofy().delete().entity(entity).now();
	}
	
	public void deleteEntity(Long id) {
	    ofy().delete().type(Bookmark.class).id(id).now();
	}
	
	public void deleteEntities(List<Bookmark> entities) {
	    log.info("Deleting entities: " + entities.size());
	    ofy().delete().entities(entities);
	}
	
	public List<Bookmark> deleteEntities(Key<Customer> parent, Integer offset, Integer limit) {
	    List<Bookmark> entitiesToDelete = getAllEntities(parent, offset, limit);
	    log.info("Deleting all entities: " + entitiesToDelete.size());
		ofy().delete().entities(entitiesToDelete);
		return entitiesToDelete;
	}
}

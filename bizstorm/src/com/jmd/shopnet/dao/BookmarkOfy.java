package com.jmd.shopnet.dao;

import static com.jmd.shopnet.dao.OfyService.ofy;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.googlecode.objectify.Key;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.entity.Bookmark;


public class BookmarkOfy {
	protected Logger log = Logger.getLogger(BookmarkOfy.class.getName());

	public Key<Bookmark> saveEntity(Bookmark bookmark) {
	    return ofy().save().entity(bookmark).now();
	}
	
	public Map<Key<Bookmark>, Bookmark> saveEntitiesNow(List<Bookmark> bookmark) {
	    return ofy().save().entities(bookmark).now();
	}
	
	public void saveEntityAsync(Bookmark bookmark) {
	    ofy().save().entity(bookmark);
	}
	
	public void saveEntitiesAsync(List<Bookmark> bookmarks) {
	    ofy().save().entities(bookmarks);
	}
	
	public List<Bookmark> fetchBookmarks(Integer limit) {
		List<Bookmark> bookmarks = ofy().load().type(Bookmark.class)
				.limit(limit).list();
		return bookmarks;
	}

	public Bookmark fetchBookmark(Long bookmarkId, Long customerId) {
		Key<Customer> customerKey = Key.create(Customer.class, customerId);
		return fetchBookmark(bookmarkId, customerKey);
	}
	public Bookmark fetchBookmark(Long bookmarkId, Key<Customer> customerKey) {
		Key<Bookmark> bookmarkKey = Key.create(customerKey, Bookmark.class, bookmarkId);
		return fetchBookmark(bookmarkKey);
	}
	public Bookmark fetchBookmark(Key<Bookmark> bookmarkKey) {
		Bookmark bookmark = ofy().load().key(bookmarkKey).now();
		return bookmark;
	}

	public Bookmark fetchBookmarkSafe(Long bookmarkId, Long customerId) {
		Key<Customer> customerKey = Key.create(Customer.class, customerId);
		return fetchBookmarkSafe(bookmarkId, customerKey);
	}
	public Bookmark fetchBookmarkSafe(Long bookmarkId, Key<Customer> customerKey) {
		Key<Bookmark> bookmarkKey = Key.create(customerKey, Bookmark.class, bookmarkId);
		return fetchBookmarkSafe(bookmarkKey);
	}
	public Bookmark fetchBookmarkSafe(Key<Bookmark> bookmarkKey) {
		Bookmark bookmark = ofy().load().key(bookmarkKey).safe();
		return bookmark;
	}

	public List<Bookmark> fetchByCustomer(Customer customer) {
	    return ofy().load().type(Bookmark.class).ancestor(customer).list();
	}
	
	public void deleteEntity(Bookmark bookmark) {
	    ofy().delete().entity(bookmark).now();
	}
	
	public void deleteEntity(Long id) {
	    ofy().delete().type(Bookmark.class).id(id).now();
	}
	
	public void deleteEntities(List<Bookmark> bookmarks) {
	    log.info("Deleting bookmarks: " + bookmarks.size());
	    ofy().delete().entities(bookmarks);
	}
	
	public void deleteEntities(Integer limit) {
	    List<Bookmark> bookmarksToDelete = fetchBookmarks(limit);
	    log.info("Deleting all bookmarks: " + bookmarksToDelete.size());
		ofy().delete().entities(bookmarksToDelete);
	}
}

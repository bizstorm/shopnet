package com.jmd.shopnet.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Document.Builder;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.GeoPoint;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.SearchServiceFactory;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.BusinessOwner;

public class RetailerIndexer {
	protected Logger log = Logger.getLogger(RetailerIndexer.class.getName());

	public static final Index RETAILER_INDEX = SearchServiceFactory.getSearchService().getIndex(
			IndexSpec.newBuilder().setName("RETAILER_INDEX"));
	public static final String RETAILER_GEO_POINT = "geo";
	public static final String RETAILER_NAME = "name";
	public static final String RETAILER_DETAIL = "detail";
	public static final String RETAILER_CATEGORIES = "cat";
	public static final String RETAILER_BUSINESSTYPE = "bizType";
	public static final String RETAILER_BRANDS = "brand";
	public static final String RETAILER_WEBSITE = "site";

	public static final float DEFAULT_RADIUS = 10000f;
	public static final float MAX_RADIUS = 100000f;
	public static final int DEFAULT_LIMIT = 20;
	public static final int MAX_LIMIT = 200;

	/**
	 * Pushing collection in single async call
	 * More efficient
	 */
	public boolean pushToIndex(Collection<BusinessOwner> businesses) {
		List<Document> docs = new ArrayList<Document>();
		for (BusinessOwner retailer : businesses) {
			Document doc = builder(retailer).build();
			log.info("Retailers search doc to add:\n" + doc);
			docs.add(doc);
		}
		RETAILER_INDEX.putAsync(docs);
		return true;
	}
	public String pushToIndex(BusinessOwner business) {
		Document doc = builder(business).build();
		log.info("Retailer search doc to add:\n" + doc);
		try {
			RETAILER_INDEX.putAsync(doc);
			return "Document added";
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "Failed to add " + doc, e);
			//TODO retry
			return "Docs not added to seach due to an error: " + e.getMessage();
		}
	}
	public void deleteFromIndex(Business retailer) {
		log.info("Retailer to delete:\n" + retailer);
		try {
			RETAILER_INDEX.deleteAsync(retailer.getId().toString());
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "Failed to delete " + retailer, e);
		}
	}
	public void deleteFromIndex(List<Business> retailers) {
		log.info("Retailers sise to delete:\n" + retailers.size());
		List<String> documentIds = new ArrayList<>(retailers.size());
		for (Business retailer : retailers) {
			documentIds.add(retailer.getId().toString());
		}
		try {
			RETAILER_INDEX.deleteAsync(documentIds);
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "Failed to delete " + documentIds, e);
		}
	}
	@SuppressWarnings("deprecation")
	public void deleteSchema() {
		log.info("deleteSchema");
		try {
			RETAILER_INDEX.deleteSchema();
		} catch (RuntimeException e) {
			log.log(Level.SEVERE, "Failed to deleteSchema. ", e);
		}
	}

	/*
	 * Just for varargs sugar
	 *
	public String pushToIndex(Retailer retailers) {
		return pushToIndex(Arrays.asList(retailers));
	}*/

	protected Builder builder(BusinessOwner business) {
		Builder docBuilder = Document.newBuilder().setId(business.getId().toString())
				.addField(Field.newBuilder().setName(RETAILER_NAME).setText(business.getBusinessName()))
				.addField(Field.newBuilder().setName(RETAILER_DETAIL).setText(business.getDetails()))
				.addField(Field.newBuilder().setName(RETAILER_GEO_POINT).setGeoPoint(new GeoPoint(business.getLat(), business.getLng())));
		business.getCategories();
		if(business.getCategories() != null && !business.getCategories().isEmpty())
			for (Category c : business.getCategories()) {
				docBuilder.addField(Field.newBuilder().setName(RETAILER_CATEGORIES).setAtom(c.getCategory()));
			}
		if(business.getBusinessType() != null ) {
				docBuilder.addField(Field.newBuilder().setName(RETAILER_BUSINESSTYPE).setAtom(business.getBusinessType().getCategory()));
		}
		if(business.getBrands() != null && !business.getBrands().isEmpty())
			for (Category b : business.getBrands()) {
				docBuilder.addField(Field.newBuilder().setName(RETAILER_BRANDS).setAtom(b.getCategory()));
			}
		return docBuilder;
	}
}

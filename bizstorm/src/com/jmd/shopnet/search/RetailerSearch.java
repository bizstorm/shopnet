package com.jmd.shopnet.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.QueryOptions;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SortExpression;
import com.google.appengine.api.search.SortOptions;
import com.google.appengine.api.search.SortOptions.Builder;
import com.google.inject.Inject;
import com.googlecode.objectify.NotFoundException;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.dao.BusinessDAO;

public class RetailerSearch {
	private static final int DUPLICATE_CHECK_LIMIT = 10;
	private static final String DUPLICATE_CHECK_RADIUS_COMPARISON = "<5000";
	Logger log = Logger.getLogger(RetailerSearch.class.getName());
	private BusinessDAO retailerOfy;

	public List<Business> searchRetailers(double lat, double lng, Float radius, List<Category> categories, String sortKey, Integer limit, Integer offset) {
		if (log.isLoggable(Level.FINE))
			log.fine("called searchRetailers with lat: " + lat  + " lng: " + lng
				+ " radius: " + radius + " categories: " + categories
				+ " sortKey: " + sortKey + " limit: " + limit + " offset: " + offset);
		StringBuilder distanceExpression = distanceExpression(lat, lng);
		com.google.appengine.api.search.QueryOptions.Builder queryOptionsBuilder = QueryOptions.newBuilder().setLimit(limit)
				.setFieldsToReturn(RetailerIndexer.RETAILER_GEO_POINT, RetailerIndexer.RETAILER_NAME)
				.setSortOptions(buildSortOptions(sortKey, distanceExpression));
		if(offset != null)
			queryOptionsBuilder.setOffset(offset);
		QueryOptions options = queryOptionsBuilder.build();
		// GeoPoint userLocation = new GeoPoint(latitude, longitude);

		// Query string
		String q = buildCategoryConditions(distanceExpression, radius, categories).toString();
		log.info(q);

		// Build the Query and run the search
		Query query = Query.newBuilder().setOptions(options).build(q);
		Collection<ScoredDocument> results = RetailerIndexer.RETAILER_INDEX.search(query).getResults();
		if (log.isLoggable(Level.FINE))
			log.fine("Search results size: " + results.size());
		if (results != null && results.size() > 0) {
			List<Business> retailers = new ArrayList<Business>();
			for (ScoredDocument sd : results) {
				if (log.isLoggable(Level.FINER))
					log.finer("Search result ID: " + sd.getId());
				try {
					Business r = retailerOfy.getEntitySafe(Long.parseLong(sd.getId()));
					retailers.add(r);
				} catch (NotFoundException e) {
					log.logp(Level.WARNING, RetailerSearch.class.getName(), "searchRetailers", "Bad retailer ID from search results. Ignoring. ", e);
				}
			}
			if (log.isLoggable(Level.INFO))
				log.info("Converted retailers size: " + retailers.size());
			return retailers;
/*		} else {
			log.info("NO Search results found. Checking in database");
			List<Retailer> retailers = retailerOfy.fetchByCategories(categories);
			if (log.isLoggable(Level.INFO))
				log.info("Datastore retailers size: " + retailers.size());
			if (retailers != null && retailers.size() > 0)
				return retailers;*/
		}
		log.warning("No Retailers found. Returning empty list.");
		return Collections.emptyList();
	}

	public Business getExactDuplicate(Business r) {
		Collection<ScoredDocument> results = getMatchingRetailerDocs(r.getLat(), r.getLng(), r.getName(), DUPLICATE_CHECK_LIMIT);
		if (log.isLoggable(Level.FINE))
			log.fine("Search results size: " + results.size());
		if (results != null && results.size() > 0) {
			for (ScoredDocument sd : results) {
				if (log.isLoggable(Level.FINER))
					log.finer("Duplicate Search result ID: " + sd.getId());
				try {
					Business d = retailerOfy.getEntitySafe(Long.parseLong(sd.getId()));
					/*if(d.getName().equalsIgnoreCase(r.getName())
							&& d.getPostalAddress().equalsIgnoreCase(r.getPostalAddress())
							) {
						return d;
					}*/
						
				} catch (NotFoundException e) {
					log.logp(Level.WARNING, RetailerSearch.class.getName(), "searchRetailers", "Bad retailer ID from search results. Ignoring. ", e);
				}
			}
		}
		return null;
	}

	/**
	 * @return null for no matching results
	 */
	public List<Business> getMatchingRetailersByName(double lat, double lng, String name, Integer limit) {
		Collection<ScoredDocument> results = getMatchingRetailerDocs(lat, lng, name, limit);
		if (log.isLoggable(Level.FINE))
			log.fine("Search results size: " + results.size());
		List<Business> list = null;//null for no duplicates
		if(results != null && !results.isEmpty()) {
			list = new ArrayList<Business>();
			if (results != null && results.size() > 0) {
				for (ScoredDocument sd : results) {
					if (log.isLoggable(Level.FINER))
						log.finer("Duplicate Search result ID: " + sd.getId());
					try {
						Business d = retailerOfy.getEntitySafe(Long.parseLong(sd.getId()));
						list .add(d);
					} catch (NotFoundException e) {
						log.logp(Level.WARNING, RetailerSearch.class.getName(), "searchRetailers", "Bad retailer ID from search results. Ignoring. ", e);
					}
				}
			}
		}
		return list;
	}

	public Collection<ScoredDocument> getMatchingRetailerDocs(double lat, double lng, String name, Integer limit) {
		if(limit == null)
			limit = DUPLICATE_CHECK_LIMIT;
		StringBuilder expr = distanceExpression(lat, lng);
		QueryOptions options = QueryOptions.newBuilder().setLimit(limit)
				.setFieldsToReturn(RetailerIndexer.RETAILER_NAME)
				.setSortOptions(buildSortOptions(RetailerIndexer.RETAILER_NAME, expr)).build();
		// Query string
		expr.append(DUPLICATE_CHECK_RADIUS_COMPARISON);
		if(name != null)
			expr.append(" AND ").append(RetailerIndexer.RETAILER_NAME).append(":\"").append(name).append("\"");
		String q = expr.toString();
		log.info(q);
		// Build the Query and run the search
		Query query = Query.newBuilder().setOptions(options).build(q);
		Collection<ScoredDocument> results = RetailerIndexer.RETAILER_INDEX.search(query).getResults();
		return results;
	}

	protected SortOptions buildSortOptions(String sortKey, StringBuilder distanceExpression) {
		Builder sortOptions = SortOptions.newBuilder();
		if(sortKey != null && !sortKey.isEmpty()) {
			switch (sortKey) {
			case RetailerIndexer.RETAILER_NAME:
				sortOptions.addSortExpression(SortExpression.newBuilder().setExpression(RetailerIndexer.RETAILER_NAME)
						.setDirection(SortExpression.SortDirection.ASCENDING).setDefaultValue("ZZ").build());
				break;
			default:
				if (log.isLoggable(Level.FINE))
					log.fine("No sort found except 'geo'");
				break;
			}
		}
		sortOptions.addSortExpression(buildDistanceExpression(distanceExpression));
		return sortOptions.build();
	}

	protected SortExpression buildDistanceExpression(StringBuilder distanceExpression) {
		SortExpression distanceSortExpr = SortExpression.newBuilder().setExpression(distanceExpression.toString())
				.setDirection(SortExpression.SortDirection.ASCENDING).setDefaultValueNumeric(100_000d).build();
		return distanceSortExpr;
	}

	protected StringBuilder buildCategoryConditions(StringBuilder query, Float radius, List<Category> categories) {
		query.append("<").append(radius.toString());
		if (categories == null || categories.isEmpty())
			return query;
		if (categories.size() == 1)
			return query.append(" ").append(RetailerIndexer.RETAILER_CATEGORIES).append(":").append(categories.get(0).getCategory());
		else {
			query.append(" (").append(RetailerIndexer.RETAILER_CATEGORIES).append(":").append(categories.get(0).getCategory());
			for (int i = 1; i < categories.size(); i++) {
				query.append(" OR ").append(RetailerIndexer.RETAILER_CATEGORIES).append(":").append(categories.get(i).getCategory());
			}
			query.append(")");
			return query;
		}
	}

	protected StringBuilder distanceExpression(double lat, double lng) {
		StringBuilder distanceExpression = new StringBuilder();
		distanceExpression.append("distance(").append(RetailerIndexer.RETAILER_GEO_POINT).append(", geopoint(").append(Double.toString(lat))
				.append(", ").append(Double.toString(lng)).append("))");
		if(log.isLoggable(Level.FINE))
			log.fine("distanceExpression: " + distanceExpression);
		return distanceExpression;
	}

	public BusinessDAO getRetailerOfy() {
		return retailerOfy;
	}

	@Inject
	public void setRetailerOfy(BusinessDAO retailerOfy) {
		this.retailerOfy = retailerOfy;
	}
}

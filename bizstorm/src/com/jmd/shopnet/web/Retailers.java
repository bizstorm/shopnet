package com.jmd.shopnet.web;

import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.search.RetailerIndexer;
import com.jmd.shopnet.search.RetailerSearch;

@Path("/shops")
public class Retailers
{
	private final static Logger log = Logger.getLogger(Retailers.class.getName());
	private RetailerSearch retailerSearch;

	/** How many (max) retailers to fetch in a single call */
	public static final int RETAILERS_TO_FETCH = 100;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Business> getRetailers(
			@QueryParam("lat") Double lat,
			@QueryParam("lng") Double lng,
			@QueryParam("radius") Float radius,
			@QueryParam("radius") Integer limit)
	{
		if (log.isLoggable(Level.FINE))
			log.fine("getRetailers(" + lat + ", " + lng + ", " + radius + ")");
		if(limit == null)
			limit = RetailerIndexer.DEFAULT_LIMIT;
		if(radius == null)
			radius = RetailerIndexer.DEFAULT_RADIUS;
		List<Business> retailers;
		try {
			retailers = retailerSearch.searchRetailers(lat, lng, radius, null, null, limit, null);
			return retailers;
		} catch (Exception e) {
			log.logp(Level.SEVERE, Retailers.class.getName(), "getRetailers", "Shop search failed. " + e.getMessage(), e);
			return Collections.<Business>emptyList();
		}
	}

	public RetailerSearch getRetailerSearch() {
		return retailerSearch;
	}
	@Inject
	public void setRetailerSearch(RetailerSearch retailerSearch) {
		this.retailerSearch = retailerSearch;
	}
}
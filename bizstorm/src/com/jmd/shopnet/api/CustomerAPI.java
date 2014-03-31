package com.jmd.shopnet.api;

import java.util.logging.Logger;

import com.google.api.server.spi.config.Api;
import com.jmd.shopnet.utils.Ids;
/**
 * Defines v1 of a Search Interface as part of the endpoints API, which
 * provides clients the ability to query for geospatial search for items of
 * interest.
 */
@Api(name = "sts"
		,version = "v1"
		,clientIds = {Ids.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
		,scopes = {Ids.SCOPES}
		)
public class CustomerAPI {
	public static final int MAX_API_LIMIT = 200;
	Logger log = Logger.getLogger(CustomerAPI.class.getName());
	

}
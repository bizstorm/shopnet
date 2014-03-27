package com.jmd.shopnet.web;

import javax.inject.Singleton;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Give us some finer control over Jackson's behavior
 */
@Singleton
@Provider
public class ObjectMapperProvider implements ContextResolver<ObjectMapper> {

	private final ObjectMapper jacksonObjectMapper;

	public ObjectMapperProvider() {
		jacksonObjectMapper = new ObjectMapper();

		jacksonObjectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

		//jacksonObjectMapper.registerModule(new JodaModule());

	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return jacksonObjectMapper;
	}
}
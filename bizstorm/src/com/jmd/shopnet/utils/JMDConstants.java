package com.jmd.shopnet.utils;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.jmd.shopnet.entity.Country;

public class JMDConstants {
	public static final int LOG_LEVEL_PROCESS_ALERT = 4000;
	public static final int LOG_LEVEL_BUSINESS_ALERT = 5000;
	public static final Map<Country, String> COUNTRY_CODES = ImmutableMap.of(
			Country.India, "91", 
			Country.Singapore, "65",
			Country.USA, "1",
			Country.UK, "44");
	public static final Map<Country, Integer> MOBILE_NUMBER_LENGTHS = ImmutableMap.of(
			Country.India, 10,
			Country.Singapore, 8, 
			Country.USA, 10, 
			Country.UK, 10);
	
	public static final Integer PAGE_LIMIT=10;
	public static final Integer PAGE_MAX_LIMIT=100000;
	
	
}

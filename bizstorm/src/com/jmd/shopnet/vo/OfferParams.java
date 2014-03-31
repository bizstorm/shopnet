package com.jmd.shopnet.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.google.appengine.api.users.User;

public class OfferParams {
	@Setter @Getter private String offerType;
	@Setter @Getter private DateRange createdDate;
	@Setter @Getter private Integer page;
	
	//order
	@Setter @Getter private String orderby = "createdDate";
	
	// Customer params
	@Setter @Getter private User user;
	@Setter @Getter private Boolean membersOnly;
	
	//  business params
	@Setter @Getter private String businessName;
	@Setter @Getter private List<String> businessTypes;
	@Setter @Getter private Integer lat;
	@Setter @Getter private Integer lng;
	@Setter @Getter private Integer distance;
	@Setter @Getter private String country;
	@Setter @Getter private String city;
	
	//  product params
	@Setter @Getter private List<String> industry;
	@Setter @Getter private List<String> categoryTags;
	@Setter @Getter private List<String> brandTags;
	@Setter @Getter private List<String> productTags;
	@Setter @Getter private List<String> keywordTags;
	@Setter @Getter private NumericRange price;
	
}

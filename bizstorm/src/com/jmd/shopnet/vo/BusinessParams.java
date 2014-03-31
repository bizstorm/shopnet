package com.jmd.shopnet.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.google.appengine.api.users.User;

public class BusinessParams {
	
	// mandatory
	//order (category, activity, distance)
	@Setter @Getter private String orderby = "category";
	
	@Setter @Getter private String industry;
	@Setter @Getter private List<String> businessTypes;
	
	//optional
	@Setter @Getter private List<String> categoryTags;
	@Setter @Getter private List<String> brandTags;
	@Setter @Getter private List<String> productTags;
	@Setter @Getter private List<String> keywordTags;
	
	@Setter @Getter private String businessName;
	@Setter @Getter private String country;
	@Setter @Getter private String city;
	
	@Setter @Getter private Integer lat;
	@Setter @Getter private Integer lng;
	@Setter @Getter private Integer distance;
	
	@Setter @Getter private User user;
	@Setter @Getter private Boolean membersOnly;
}

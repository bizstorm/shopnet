package com.jmd.shopnet.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.utils.Enumerators.ACCESS;
import com.jmd.shopnet.utils.Enumerators.SCOPE;

public class BusinessParams {
	
	// mandatory
	//order (category, activity, distance)
	@Setter @Getter private String orderby = "category";
	@Setter @Getter private Integer limit;
	@Setter @Getter private Integer offset;
	
	@Setter @Getter private List<String> industries;
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
	@Setter @Getter private List<Ref<Business>> keys;
	@Setter @Getter private List<ACCESS> access;
	@Setter @Getter private SCOPE scope;
	
	@Setter @Getter private String phoneNumber;
	@Setter @Getter private String emailId;
}

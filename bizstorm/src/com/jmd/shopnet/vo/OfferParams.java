package com.jmd.shopnet.vo;

import java.util.List;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import com.google.appengine.api.users.User;
import com.jmd.shopnet.utils.Enumerators.SCOPE;

@Data
public class OfferParams {
	
	@Setter @Getter private Integer offset;
	@Setter @Getter private Integer limit;
	
	//order
	@Setter @Getter private String orderby = "createdDate";
	
	// offer params
	@Setter @Getter private String offerType;
	@Setter @Getter private DateRange createdDateRange;
	@Setter @Getter private DateRange modifiedDateRange;
	@Setter @Getter private NumericRange priceRange;
	
	
	// Customer params
	@Setter @Getter private User user;
	@Setter @Getter private SCOPE scope;
	
	//  business params
	@Setter @Getter private String businessName;
	@Setter @Getter private List<String> businessTypes;
	@Setter @Getter private Integer lat;
	@Setter @Getter private Integer lng;
	@Setter @Getter private Integer distance;
	@Setter @Getter private String country;
	@Setter @Getter private String city;
	
	
	//  product params
	@Setter @Getter private List<String> industries;
	@Setter @Getter private List<String> categoryTags;
	@Setter @Getter private List<String> brandTags;
	@Setter @Getter private List<String> productTags;
	@Setter @Getter private List<String> keywordTags;
	
	
}

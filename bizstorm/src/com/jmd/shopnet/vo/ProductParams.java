package com.jmd.shopnet.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ProductParams {
	
	//order
	@Setter @Getter private String orderby = "name";
	@Setter @Getter private Integer limit;
	@Setter @Getter private Integer offset;
	
	//  product params
	@Setter @Getter private List<String> industries;
	@Setter @Getter private List<String> categoryTags;
	@Setter @Getter private List<String> brandTags;
	@Setter @Getter private List<String> nameTags;
	
	@Setter @Getter private List<String> attributeTags;

}

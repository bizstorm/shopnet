package com.jmd.shopnet.vo;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class ProductParams {
	@Setter @Getter private Integer page;
	
	//order
	@Setter @Getter private String orderby = "name";
	
	//  product params
	@Setter @Getter private List<String> industry;
	@Setter @Getter private List<String> categoryTags;
	@Setter @Getter private List<String> brandTags;
	@Setter @Getter private List<String> nameTags;
	
	@Setter @Getter private List<String> attributeTags;

}

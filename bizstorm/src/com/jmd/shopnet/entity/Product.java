package com.jmd.shopnet.entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Data;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.Category;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.EmbedMap;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Data
@Entity
@Cache
public class Product {

	@Id
    private Long id;
    
    @Index
    private Category industry;
    @Index
    private Category category;
    @Index
    private Category brand;
    @Index
    private Category name;
    @Index
    private List<Category> keywords;
    
    private String defaultPictureUrl;
    
    private Integer status=1;

    @EmbedMap
    private Map<String, String> attributes = new HashMap<String, String>();

    @ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
    public Key<Product> getKey() {
		return Key.create(Product.class, id);
	}
	
}

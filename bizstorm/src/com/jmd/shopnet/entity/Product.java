package com.jmd.shopnet.entity;
import java.util.HashMap;
import java.util.Map;

import lombok.Data;

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
    
    private String defaultPictureUrl;

    @EmbedMap
    private Map<String, String> attributes = new HashMap<String, String>();

    public Key<Product> geyKey() {
		return Key.create(Product.class, id);
	}
	
}

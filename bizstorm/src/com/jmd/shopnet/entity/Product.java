package com.jmd.shopnet.entity;
import java.util.HashMap;
import java.util.Map;

import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.EmbedMap;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
@Cache
public class Product {

	@Id
    private Long id;

    @Index
    private String name;

    @Index
    private String category;
    
    @Index
    private String business;

    @EmbedMap
    private Map<String, String> attributes = new HashMap<String, String>();

    private String defaultPictureUrl;

	public String getName() {
        return this.name;
    }

	public void setName(String name) {
        this.name = name;
    }

	public String getCategoryTags() {
        return this.category;
    }

	public void setCategoryTags(String category) {
        this.category = category;
    }

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, String> attributes) {
		this.attributes = attributes;
	}

	public String getDefaultPictureUrl() {
        return this.defaultPictureUrl;
    }

	public void setDefaultPictureUrl(String defaultPictureUrl) {
        this.defaultPictureUrl = defaultPictureUrl;
    }

	public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }
}

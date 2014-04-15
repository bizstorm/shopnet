package com.jmd.shopnet.entity;

import lombok.Data;

import com.googlecode.objectify.annotation.Embed;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Data
@Embed
@Entity
public class Address {
	
	@Id
    private Long id;
	private String addressline1;
	private String addressline2;
	@Index
	private String city;
	private Integer zip;
	@Index
	private boolean primary=false;
}

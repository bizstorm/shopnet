package com.jmd.shopnet.service;

import java.util.List;
import java.util.Map;

import lombok.Getter;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.dao.ProductDAO;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.vo.ProductParams;

public class ProductService {
	
	@Getter private ProductDAO productDAO;

	public List<Product> getOrderedProducts(ProductParams pParams) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Key<Product>, Product> getProducts(ProductParams pParams) {
		// TODO Auto-generated method stub
		return null;
	}

	@Inject
	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

}

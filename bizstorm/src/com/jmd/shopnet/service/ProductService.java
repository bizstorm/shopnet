package com.jmd.shopnet.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.dao.ProductDAO;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.vo.ProductParams;

public class ProductService {
	
	private ProductDAO productDAO;
	
	public List<Product> getOrderedProducts(ProductParams params) {
		List<Product> productOfferList = productDAO.getProductListByParams(params);
		return productOfferList;
	}
	
	public Map<Key<Product>, Product> getProducts(ProductParams params) {
		Map<Key<Product>, Product> productOffers = new LinkedHashMap<>();
		List<Product> productList = getOrderedProducts(params);
		for (Product product : productList) {
			productOffers.put(product.geyKey(), product);
		}
		return productOffers;
	}


	@Inject
	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

}

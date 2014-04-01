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
		List<Product> productList = productDAO.getProductListByParams(params);
		return productList;
	}
	
	public List<Key<Product>> getOrderedProductkeys(ProductParams params) {
		List<Key<Product>> productKeys = productDAO.getProductKeysByParams(params);
		return productKeys;
	}
	
	public List<Product> getOrderedProductFromkeys(List<Key<Product>> keys) {
		List<Product> productKeys = productDAO.getListByKeys(keys);
		return productKeys;
	}
	
	public Map<Long, Product> getProductsMap(ProductParams params) {
		Map<Long, Product> productOffers = new LinkedHashMap<>();
		List<Product> productList = getOrderedProducts(params);
		for (Product product : productList) {
			productOffers.put(product.getId(), product);
		}
		return productOffers;
	}
	


	@Inject
	public void setProductDAO(ProductDAO productDAO) {
		this.productDAO = productDAO;
	}

}

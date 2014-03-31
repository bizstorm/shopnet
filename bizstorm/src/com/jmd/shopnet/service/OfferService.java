package com.jmd.shopnet.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.dao.ProductOfferDAO;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.entity.ProductOffer;
import com.jmd.shopnet.vo.BusinessParams;
import com.jmd.shopnet.vo.OfferParams;
import com.jmd.shopnet.vo.ProductParams;


public class OfferService {
	
	private ProductOfferDAO offerDAO;
	
	public List<ProductOffer> getOrderedProductOffers(Set<Key<Business>> businessKeys, Set<Key<Product>> productKeys, OfferParams oParams) {
		List<ProductOffer> productOfferList = offerDAO.getOfferByParams(businessKeys, productKeys, oParams);
		return productOfferList;
	}
	
	public Map<Key<ProductOffer>, ProductOffer> getProductOffers(Set<Key<Business>> businessKeys, Set<Key<Product>> productKeys, OfferParams oParams) {
		Map<Key<ProductOffer>, ProductOffer> productOffers = new HashMap<>();
		List<ProductOffer> productOfferList = getOrderedProductOffers(businessKeys, productKeys, oParams);
		for (ProductOffer productOffer : productOfferList) {
			productOffers.put(productOffer.geyKey(), productOffer);
		}
		return productOffers;
	}

	

	public ProductParams getProductParams(OfferParams oParams) {
		ProductParams pParams = new ProductParams();
		pParams.setBrandTags(oParams.getBrandTags());
		pParams.setIndustry(oParams.getIndustry());
		pParams.setCategoryTags(oParams.getCategoryTags());
		pParams.setAttributeTags(oParams.getKeywordTags());
		pParams.setNameTags(oParams.getProductTags());
		return pParams;
	}

	public BusinessParams getBusinessParams(OfferParams oParams) {
		BusinessParams bParams = new BusinessParams();
		bParams.setUser(oParams.getUser());
		//bParams.setBrandTags(oParams.getBrandTags());
		bParams.setBusinessName(oParams.getBusinessName());
		bParams.setBusinessTypes(oParams.getBusinessTypes());
		//bParams.setCategoryTags(oParams.getCategoryTags());
		bParams.setCity(oParams.getCity());
		bParams.setCountry(oParams.getCountry());
		//bParams.setKeywordTags(oParams.getKeywordTags());
		//bParams.setIndustry(oParams.getIndustry());
		//bParams.setProductTags(oParams.getProductTags());
		bParams.setLat(oParams.getLat());
		bParams.setLng(oParams.getLng());
		bParams.setDistance(oParams.getDistance());
		bParams.setMembersOnly(oParams.getMembersOnly());
		return bParams;
	}
	
	@Inject
	public void setOfferDAO(ProductOfferDAO offerDAO) {
		this.offerDAO = offerDAO;
	}
	
}

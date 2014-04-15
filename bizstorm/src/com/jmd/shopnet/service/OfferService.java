package com.jmd.shopnet.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

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
	
	public List<ProductOffer> getOrderedProductOffersFromList(Collection<Business> businessList, Collection<Product> productList, OfferParams oParams) {
		List<Key<Business>> businessKeys = new ArrayList<>(businessList.size());
		for (Business business : businessList) {
			businessKeys.add(business.getKey());
		}
		List<Key<Product>> productKeys = new ArrayList<>(productList.size());
		for (Product product : productList) {
			productKeys.add(product.getKey());
		}
		List<ProductOffer> productOfferList = offerDAO.getOfferListByParams(businessKeys, productKeys, oParams);
		return productOfferList;
	}
	
	public List<ProductOffer> getOrderedProductOffers(List<Key<Business>> businessKeys, List<Key<Product>> productKeys, OfferParams oParams) {
		List<ProductOffer> productOfferList = offerDAO.getOfferListByParams(businessKeys, productKeys, oParams);
		return productOfferList;
	}
	
	public Map<Long, ProductOffer> getProductOffers(List<Key<Business>> businessKeys, List<Key<Product>> productKeys, OfferParams oParams) {
		Map<Long, ProductOffer> productOffers = new HashMap<>();
		List<ProductOffer> productOfferList = getOrderedProductOffers(businessKeys, productKeys, oParams);
		for (ProductOffer productOffer : productOfferList) {
			productOffers.put(productOffer.getId(), productOffer);
		}
		return productOffers;
	}

	public ProductParams getProductParams(OfferParams oParams) {
		ProductParams pParams = new ProductParams();
		pParams.setBrandTags(oParams.getBrandTags());
		pParams.setIndustries(oParams.getIndustries());
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
		bParams.setScope(oParams.getScope());
		return bParams;
	}
	
	@Inject
	public void setOfferDAO(ProductOfferDAO offerDAO) {
		this.offerDAO = offerDAO;
	}

	public ProductOffer createOffer(ProductOffer productOffer) {
		ProductOffer offer = null;
		if(productOffer != null){
				Key<ProductOffer> offerKey = offerDAO.saveEntity(productOffer);
				if(offerKey != null){
					offer = offerDAO.getEntity(offerKey.getId());
				}
		}
		return offer;
	}

	public boolean updateOffer(ProductOffer productOffer) {
		Boolean updated = false;
		if(productOffer != null){
			if(productOffer.getId() != null){
				Key<ProductOffer> offerKey = offerDAO.saveEntity(productOffer);
				if(offerKey != null){
					updated = true;
				}
			}
		}
		return updated;
	}

	public boolean deleteOffer(Long offerId) {
		if(offerId != null){
			offerDAO.deleteEntity(offerId);
		}
		return true;
	}
	
}

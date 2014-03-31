package com.jmd.shopnet.api;

import java.util.List;
import java.util.Map;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.Named;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.entity.ProductOffer;
import com.jmd.shopnet.service.BusinessService;
import com.jmd.shopnet.service.OfferService;
import com.jmd.shopnet.service.ProductService;
import com.jmd.shopnet.utils.Ids;
import com.jmd.shopnet.vo.BusinessParams;
import com.jmd.shopnet.vo.OfferParams;
import com.jmd.shopnet.vo.ProductParams;

@Api(name = "offers"
,version = "v1"
,clientIds = {Ids.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
,scopes = {Ids.SCOPES}
)
public class OfferAPI {

	private OfferService offerService;
	private BusinessService businessService;
	private ProductService productService;
	
	@ApiMethod(name="offer", httpMethod = "POST" , path="offer/{ot}/{p}")
	public Map<String, Object> getBusinessOffers(
				@Named("ot") String offerType, 
				//@Named("biz") String businessType,				
				@Named("p") Integer page, 				
				OfferParams oParams) {
		Map<String, Object> resultMap = new HashMap<>();
		oParams.setOfferType(offerType);
		//oParams.setBusinessTypes(businessType);
		oParams.setPage(page);
		try{
			BusinessParams bParams = offerService.getBusinessParams(oParams);		
			ProductParams pParams = offerService.getProductParams(oParams);		
			Map<Key<Business>, Business> businessDetails = businessService.getBaseBusinesses(bParams);
			Map<Key<Product>, Product> productDetails = productService.getProducts(pParams);
			List<ProductOffer> productOffers = offerService.getOrderedProductOffers(businessDetails.keySet(), productDetails.keySet(), oParams);
			resultMap.put("offers", productOffers);
			resultMap.put("productDetails", productDetails);
			resultMap.put("businessDetails", businessDetails);
		}catch (Exception e){
			throw e;
		}
		return resultMap;
	}
			
	public OfferService getOfferService() {
		return offerService;
	}

	@Inject
	public void setOfferService(OfferService offerService) {
		this.offerService = offerService;
	}
	@Inject
	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}

	@Inject
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}
}

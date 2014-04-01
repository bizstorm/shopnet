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
import com.jmd.shopnet.utils.JMDConstants;
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
	
	@ApiMethod(name="getoffers", httpMethod = "POST" , path="{ot}/{p}")
	public Map<String, Object> getBusinessOffers(
				@Named("ot") String offerType, 
				@Named("p") Integer page, 				
				OfferParams oParams) {
		Map<String, Object> resultMap = new HashMap<>();
		oParams.setOfferType(offerType);
		//oParams.setBusinessTypes(businessType);
		oParams.setOffset(JMDConstants.PAGE_LIMIT*(page-1));
		oParams.setLimit(JMDConstants.PAGE_LIMIT);
		try{
			BusinessParams bParams = offerService.getBusinessParams(oParams);		
			ProductParams pParams = offerService.getProductParams(oParams);		
			Map<Long, Business>  businessMap = businessService.getBusinessMap(bParams);
			Map<Long, Product> productMap = productService.getProductsMap(pParams);
			
			
			List<ProductOffer> productOffers = offerService.getOrderedProductOffersFromList(businessMap.values(), productMap.values(), oParams);
			resultMap.put("offers", productOffers);
			
			resultMap.put("productDetails", productMap);
			resultMap.put("businessDetails", businessMap);
		}catch (Exception e){
			throw e;
		}
		return resultMap;
	}
	
	@ApiMethod(name="createoffer", httpMethod = "POST" , path="{ot}/new")
	public ProductOffer createBusinessOffer(
				@Named("ot") String offerType, 				
				ProductOffer productOffer) {
		
		return offerService.createOffer(productOffer);
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

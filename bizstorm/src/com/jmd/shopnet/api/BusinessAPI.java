package com.jmd.shopnet.api;

import java.util.List;
import java.util.Map;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.api.server.spi.config.Named;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.gwt.dev.util.collect.HashMap;
import com.google.inject.Inject;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.service.BusinessService;
import com.jmd.shopnet.service.CustomerService;
import com.jmd.shopnet.service.ProductService;
import com.jmd.shopnet.utils.Ids;
import com.jmd.shopnet.utils.JMDConstants;
import com.jmd.shopnet.vo.BusinessParams;

@Api(name = "business"
,version = "v1"
,clientIds = {Ids.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
,scopes = {Ids.SCOPES}
)
public class BusinessAPI {
	private BusinessService businessService;
	private ProductService productService;
	private CustomerService customerService;
	
	

	@ApiMethod(name="getBusinesses", httpMethod = "POST" , path="{p}")
	public Map<String, Object> getBusinesses(User user,
				@Named("p") Integer page, 				
				BusinessParams params) {
		Map<String, Object> resultMap = new HashMap<>();
		//oParams.setBusinessTypes(businessType);
		params.setOffset(JMDConstants.PAGE_LIMIT*(page-1));
		params.setLimit(JMDConstants.PAGE_LIMIT);
		try{
			//ProductParams pParams = businessService.getProductParams(params);		
			//Map<Long, Product> productMap = productService.getProductsMap(pParams);
			if(user != null){
				params.setUser(user);
			}
			
			List<Business> businesss = businessService.getOrderedBusinessList(params);
			resultMap.put("business", businesss);
			

		}catch (Exception e){
			throw e;
		}
		return resultMap;
	}
	
	@ApiMethod(name="createbusiness", httpMethod = "POST" , path="new")
	public Business createBusiness(				
			User user, Business business) throws OAuthRequestException  {
		if(user == null){
			throw new OAuthRequestException("User must login ");
		}
		List<Customer> customers = customerService.getCustomers(user);
		Customer customer = null;
		if(customers != null && customers.size() > 0){
			customer = customers.get(0);
			business.setCustomer(customer.geyKey());			
		}
		business = businessService.createBusiness(business);
		if(customer != null){
			customer.addBusiness(business);
			customerService.updateCustomer(customer);
		}		
		return business;
	}
	
	@ApiMethod(name="updatebusiness", httpMethod = "POST" , path="update")
	public void updateBusinessBusiness(				
				User user, Business business) throws OAuthRequestException {
		if(user == null){
			throw new OAuthRequestException("User must login ");
		}
		List<Customer> customers = customerService.getCustomers(user);
		if(customers != null && customers.size() > 0){
			business.setCustomer(customers.get(0).geyKey());
		}
		businessService.updateBusiness(business);
	}
	
	@ApiMethod(name="deletebusiness", httpMethod = "POST" , path="delete/{id}")
	public void deleteBusinessBusiness(				
			User user ,@Named("id") Long businessId ) {
		
		 businessService.deleteBusiness(businessId);
	}
			

	
	
	@Inject
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public void setProductService(ProductService productService) {
		this.productService = productService;
	}

	@Inject
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public void setBusinessService(BusinessService businessService) {
		this.businessService = businessService;
	}
	
	@Inject
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public void setCustomerService(CustomerService customerService) {
		this.customerService = customerService;
	}

}

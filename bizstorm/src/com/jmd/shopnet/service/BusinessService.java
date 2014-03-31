package com.jmd.shopnet.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.jmd.shopnet.dao.BusinessDAO;
import com.jmd.shopnet.dao.CustomerDAO;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.BusinessOwner;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.utils.JMDConstants;
import com.jmd.shopnet.vo.BusinessParams;


public class BusinessService {
	

	private CustomerDAO customerDAO;
	private BusinessDAO businessDAO;

	public Map<Key<Business>, Business>  getBaseBusinesses(BusinessParams bParams) {
		List<Ref<Business>> businessList = null;
		Map<Key<Business>, Business> result = new LinkedHashMap<>();
		if(bParams.getUser() != null){ // logged in user
			if(bParams.getMembersOnly()){
				List<Customer> customers = customerDAO.getByUser(bParams.getUser());
				Customer customer = customers.get(0);
				List<Business> myBusinessList = businessDAO.getMyBusiness(customer.geyKey() );
				
				businessList = new ArrayList<>();
				if(bParams.getMembersOnly()){
					for (Business business : myBusinessList) {
						BusinessOwner owner = (BusinessOwner) business;
						businessList.addAll(owner.getMembers());
					}
				}
			}
		}
		List<Business> bList = businessDAO.getBusinessByParams(businessList, bParams);
		for (Business business : bList) {
			result.put(business.geyKey(), business);
		}
		return result;
	}

	

	@Inject
	public void setCustomerDAO(CustomerDAO customerDAO) {
		this.customerDAO = customerDAO;
	}

	@Inject
	public void setBusinessDAO(BusinessDAO businessDAO) {
		this.businessDAO = businessDAO;
	}

}

package com.jmd.shopnet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.gwt.dev.util.collect.HashMap;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.jmd.shopnet.dao.BusinessDAO;
import com.jmd.shopnet.dao.CustomerDAO;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.BusinessOwner;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.utils.Enumerators.ACCESS;
import com.jmd.shopnet.utils.Enumerators.SCOPE;
import com.jmd.shopnet.vo.BusinessParams;
import com.jmd.shopnet.vo.ProductParams;

/**
 * @author subodhk
 *
 */
public class BusinessService {

	private CustomerDAO customerDAO;
	private BusinessDAO businessDAO;

	/**
	 * @param bParams
	 * @return Map<Key<Business>, Business>
	 */
	public Map<Long, Business> getBusinessMap(BusinessParams params) {
		Map<Long, Business>  result = new HashMap<>();
		List<Business>  list = getOrderedBusinessList(params) ;
		for (Business business : list) {
			result.put(business.getId(), business);
		}		
		return result;
	}
	public List<Key<Business>> getOrderedBusinesskeys(BusinessParams params) {
		List<Key<Business>> keys = new ArrayList<>();
		List<Business>  list = getOrderedBusinessList(params) ;
		for (Business business : list) {
			keys.add(business.getKey());
		}
		return keys;
	}
	
	public List<Business> getOrderedBusinessList(BusinessParams params) {
		List<Business> myBusinessList = null;
		List<Business>  result = new ArrayList<>();
		
		List<ACCESS> access = new ArrayList<>();		
		access.add(ACCESS.PUBLIC);
		if (params.getUser() != null) { // logged in user
			//TODO correct group access query
			access.add(ACCESS.GROUP);
			List<Customer> customers = customerDAO.getByUser(params.getUser());
			Customer customer = customers.get(0);
			myBusinessList = customer.getBusiness();
			if (myBusinessList != null && SCOPE.GROUP.equals(params.getScope())) {
				List<Ref<Business>> businessFilter = new ArrayList<>();
				for (Business business : myBusinessList) {
					BusinessOwner owner = (BusinessOwner) business;
					businessFilter.addAll(owner.getMembers());
				}
				params.setKeys(businessFilter);
			}else if(myBusinessList != null && SCOPE.MINE.equals(params.getScope())){
				List<Ref<Business>> businessFilter = new ArrayList<>();
				for (Business business : myBusinessList) {
					businessFilter.add(business.getRef());
				}
				params.setKeys(businessFilter);
			}
		}
		
		// GET All Business following the criteria
		List<Business> businessList= businessDAO.getBusinessListByParams(params);

		
		//TODO FIX group access related logic
		// here we are removing business which have group access but not in currrent user's group
		if(!SCOPE.MINE.equals(params.getScope())){
			for (Business business: businessList) {
				// Remove business which has group access and current customer is
				// not in group
				if (myBusinessList != null && myBusinessList.size() > 0) {

					for (Business temp : myBusinessList) {
						if (business.getKey().equals(temp.getKey())) { // remove my business
							continue;
						}
						if (ACCESS.GROUP.equals(business.getAccess())) {
							BusinessOwner owner = (BusinessOwner) temp;
							BusinessOwner current = (BusinessOwner) business;
							if ((owner.getMembers().contains(current) && current
									.getMembers().contains(owner))) {
								result.add(business);
							}
						}else{
							result.add(business);
						}

					}
				}
			}
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

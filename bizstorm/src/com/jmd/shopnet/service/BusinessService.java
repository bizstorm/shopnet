package com.jmd.shopnet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.jmd.shopnet.dao.BusinessDAO;
import com.jmd.shopnet.dao.CustomerDAO;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.BusinessOwner;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.utils.Enumerators.ACCESS;
import com.jmd.shopnet.utils.Enumerators.SCOPE;
import com.jmd.shopnet.vo.BusinessParams;

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
	public Map<Key<Business>, Business> getBaseBusinesses(BusinessParams bParams) {
		List<Business> myBusinessList = null;
		List<ACCESS> access = new ArrayList<>();
		access.add(ACCESS.PUBLIC);
		if (bParams.getUser() != null) { // logged in user
			//TODO correct group access query
			access.add(ACCESS.GROUP);
			List<Customer> customers = customerDAO.getByUser(bParams.getUser());
			Customer customer = customers.get(0);
			myBusinessList = businessDAO.getMyBusiness(customer.geyKey());
			if (myBusinessList != null && SCOPE.GROUP.equals(bParams.getScope())) {
				List<Ref<Business>> businessFilter = new ArrayList<>();
				for (Business business : myBusinessList) {
					BusinessOwner owner = (BusinessOwner) business;
					businessFilter.addAll(owner.getMembers());
				}
				bParams.setKeys(businessFilter);
			}else if(myBusinessList != null && SCOPE.MINE.equals(bParams.getScope())){
				List<Ref<Business>> businessFilter = new ArrayList<>();
				for (Business business : myBusinessList) {
					businessFilter.add(business.getRef());
				}
				bParams.setKeys(businessFilter);
			}

		}
		
		// GET All Business following the criteria
		List<Key<Business>> businesskeys = businessDAO.getBusinessKeysByParams(bParams);

		Map<Key<Business>, Business> result = businessDAO.loadKeysByParams(businesskeys, bParams);
		
		//TODO FIX group access related logic
		// here we are removing business which have group access but not in currrent user's group
		if(!SCOPE.MINE.equals(bParams.getScope())){
			for (Map.Entry<Key<Business>, Business> businessEntry : result
					.entrySet()) {
				// Remove business which has group access and current customer is
				// not in group
				Business business = businessEntry.getValue();
				if (myBusinessList != null && myBusinessList.size() > 0) {

					for (Business temp : myBusinessList) {
						if (business.getKey().equals(temp.getKey())) { // remove my business
							result.remove(businessEntry.getKey());
							continue;
						}
						if (ACCESS.GROUP.equals(business.getAccess())) {
							BusinessOwner owner = (BusinessOwner) temp;
							BusinessOwner current = (BusinessOwner) business;
							if (!(owner.getMembers().contains(current) || current
									.getMembers().contains(owner))) {
								result.remove(businessEntry.getKey());
							}
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

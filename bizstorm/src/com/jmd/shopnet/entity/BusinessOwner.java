package com.jmd.shopnet.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.EntitySubclass;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.condition.IfTrue;
import com.jmd.shopnet.utils.StringConversions;

@Data
@EqualsAndHashCode(callSuper=true)
@Cache
@EntitySubclass
public class BusinessOwner extends Business {
	private List<Category> categories;
	private List<Category> brands;	
	private List<Category> products;
	private List<Category> keywords;	
	private List<Product> catalogs;	
	private List<Category> serviceTypes;
	private List<PaymentMode> paymentModes;
	private List<Ref<Business>>  members;
	@Index
	@Load
	private List<Ref<ProductOffer>>  offers;

	private String specialOfferings;
	
	private String hoursOfOperation;//TODO text to show when available. else show based on below 3 fields
	
	@Index(IfTrue.class)
	private Boolean active = true;
	private boolean approved = false;
	private User reviwedBy;
	private User addedBy;
	
	public List<ProductOffer> getAllOffers() {
		List<ProductOffer> tempoffers = new ArrayList<>();
		if(offers != null){
			for (Ref<ProductOffer> offer : offers) {
				tempoffers.add(offer.get());
			}
		}
		return tempoffers;
	}
	
	public void addOffer(ProductOffer offer) {
		if(offer != null && offers != null){
			offers.add(Ref.create(offer));
		}
	}
	
	public void removeOffer(ProductOffer offer) {
		if(offer != null && offers != null){
			offers.remove(Ref.create(offer));
		}
	}

	
	
	public List<Business> getAllGroupMembers() {
		List<Business> rmembers = new ArrayList<>();
		if(members != null){
			for (Ref<Business> member : members) {
				rmembers.add(member.get());
			}
		}
		return rmembers;
	}
	
	public void addGroupMember(Business customer) {
		if(customer != null && members != null){
			members.add(Ref.create(customer));
		}
	}
	
	public void removeGroupMember(Business customer) {
		if(customer != null && members != null){
			members.remove(Ref.create(customer));
		}
	}
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getCategoryTags() {
		return StringConversions.formCsvFromCategories(this.categories);
	}

	public void setCategoryTags(String tags) {
		this.categories = StringConversions.extractCategoriesFromCsv(tags);
	}


	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getBrandTags() {
		return StringConversions.formCsvFromCategories(this.brands);
	}

	public void setBrandTags(String tags) {
		this.brands = StringConversions.extractCategoriesFromCsv(tags);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getKeywordTags() {
		return StringConversions.formCsvFromCategories(this.keywords);
	}

	public void setKeywordTags(String tags) {
		this.keywords = StringConversions.extractCategoriesFromCsv(tags);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getServiceTypeTags() {
		return StringConversions.formCsvFromCategories(this.serviceTypes);
	}

	public void setServiceTypeTags(String tags) {
		this.serviceTypes = StringConversions.extractCategoriesFromCsv(tags);
	}

	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getPaymentModeCsv() {
		return StringConversions.formCsvFromPaymentModes(this.paymentModes);
	}

	public void setPaymentModeCsv(String paymentModeCsv) {
		this.paymentModes = StringConversions.extractPaymentModesFromCsv(paymentModeCsv);
	}

}

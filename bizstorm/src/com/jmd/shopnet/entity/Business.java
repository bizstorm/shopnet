package com.jmd.shopnet.entity;

import java.util.Date;
import java.util.List;

import lombok.Data;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.annotation.Parent;
import com.jmd.shopnet.utils.Enumerators.ACCESS;
import com.jmd.shopnet.utils.StringConversions;

@Data
@Entity
@Cache
public class Business {
	@Parent
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	private Key<Customer> customer;
	
	@Id
	private Long id;	
	private String details;
	private String owner;
	private ACCESS access;
	
	@Index
	private String businessName;
	
	@Index
	private Category industry;	
	
	@Index
	private Category businessType;	
		
	private Integer status=1;
	/**
	 * lat - The latitude. Must be between -90 and 90 (inclusive). null
	 * indicates no value set
	 */
	@IgnoreSave
	private Float lat;
	/**
	 * lng - The longitude. Must be between -180 and 180 (inclusive). null
	 * indicates no value set
	 */
	@IgnoreSave
	private Float lng;
	
	private GeoPt geoPt;
	
	private Address postalAddress;
	private Country country;


	private Link coverPicture;

	@Index
	private List<PhoneNumber> smsNumbers;
	private List<PhoneNumber> contactNumbers;
	@Index
	private List<Email> emailids;
	private Link website;
	private String contactName;

	private Date joiningDate;
	@Index
	private Date lastActivityDate;
	
	
	private Float editorRating;
	private Float avgUserRating;
	
	
	
	/** 
	 * Set country before setting sms numbers
	 */
	public void setSmsCsv(String csv) {
		this.smsNumbers = StringConversions.extractNumericPhonesFromCsv(csv, this.country);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getContactPhoneCsv() {
		return StringConversions.formCsvFromPhones(this.contactNumbers);
	}

	public void setContactPhoneCsv(String phone) {
		this.contactNumbers = StringConversions.extractPhonesFromCsv(phone);
	}

	
	public Float getLat() {
		return this.lat;
	}
	/**
	 * @deprecated Use setLatLng instead
	 * Currently used by OpenCsv
	 */
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public void setLat(Float lat) {
		this.lat = lat;
		repopulateGeoPt();
	}
	public Float getLng() {
		return this.lng;
	}
	/**
	 * @deprecated Use setLatLng instead
	 * Currently used by OpenCsv
	 */
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public void setLng(Float lng) {
		this.lng = lng;
		repopulateGeoPt();
	}

	public void setLatLng(Float lat, Float lng) {
		this.lat = lat;
		this.lng = lng;
		repopulateGeoPt();
	}

	public GeoPt getGeoPt() {
		if (this.geoPt == null)
			repopulateGeoPt(this.lat, this.lng);
		return this.geoPt;
	}

	public void setGeoPt(GeoPt geoPt) {
		this.geoPt = geoPt;
		repopulateLatLng(geoPt);
	}

	@OnLoad
	public void repopulateLatLng() {
		repopulateLatLng(this.geoPt);
	}

	private void repopulateLatLng(GeoPt geoPt) {
		if (geoPt != null) {
			this.lat = geoPt.getLatitude();
			this.lng = geoPt.getLongitude();
		} else {
			this.lat = null;
			this.lng = null;
		}
	}

	@OnSave
	public void repopulateGeoPt() {
		repopulateGeoPt(this.lat, this.lng);
	}

	private void repopulateGeoPt(Float lat, Float lng) {
		if (lat != null && lng != null) {
			this.geoPt = new GeoPt(lat, lng);
		} else {
			this.geoPt = null;
		}
	}
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Key<Business> getKey() {
		return Key.create(customer, Business.class, id);
	}
	
	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public Ref<Business> getRef() {
		return Ref.create(getKey());
	}
	
	public Long getCustomerId(){
		Long id = null;
		if(customer != null) id = customer.getId();
		return id;
	}
	
	public void setCustomerId(Long id){		
		if(id != null) customer = Key.create(Customer.class, id);
	}
}

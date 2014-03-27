package com.jmd.shopnet.entity;

import java.util.Date;
import java.util.List;

import com.google.api.server.spi.config.AnnotationBoolean;
import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Cache;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;
import com.googlecode.objectify.condition.IfTrue;
import com.jmd.shopnet.utils.StringConversions;

@Entity
@Cache
public class Retailer {
	@Id
	private Long id;

	@Index
	private String name;
	private String details;
	private String owner;
	private String specialOfferings;

	@Index
	private List<Category> categories;
	@Index
	private List<Category> subCategories;
	@Index
	private List<Category> brands;
	@Index
	private List<Category> keywords;
	@Index
	private List<Category> products;
	@Index
	private List<Category> serviceTypes;


	private String hoursOfOperation;//TODO text to show when available. else show based on below 3 fields
	//TODO aa33230 Break down Hours of Operation to indexable filelds by days of week

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
	private PostalAddress postalAddress;
	private String city;
	private Country country;
	private String zip;
	private String locality;
	private List<Email> emails;

	private Link coverPicture;
	private List<Link> retailerPictures;
	private List<Link> customerPictures;
	@Index
	private List<PhoneNumber> smsNumbers;
	private List<PhoneNumber> phoneNumbers;
	private Link website;
	private String contactName;
	private List<PhoneNumber> contactPhones;

	private boolean crowdSourced;
	@Index
	private User partnerLogin;
	private Date joiningDate;
	@Index(IfTrue.class)
	private Boolean active = true;
	private Boolean partner = true;
	private boolean approved = false;
	@Index
    private String addedByEmail;
	private User reviwedBy;
	private User addedBy;
	private Float editorRating;
	private Float avgUserRating;
	private List<PaymentMode> paymentModes;

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getPhoneCsv() {
		return StringConversions.formCsvFromPhones(this.phoneNumbers);
	}

	public void setPhoneCsv(String csv) {
		this.phoneNumbers = StringConversions.extractPhonesFromCsv(csv);
	}

	/** 
	 * Set country before setting sms numbers
	 */
	public void setSmsCsv(String csv) {
		this.smsNumbers = StringConversions.extractNumericPhonesFromCsv(csv, this.country);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getContactPhoneCsv() {
		return StringConversions.formCsvFromPhones(this.contactPhones);
	}

	public void setContactPhoneCsv(String phone) {
		this.contactPhones = StringConversions.extractPhonesFromCsv(phone);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getCategoryTags() {
		return StringConversions.formCsvFromCategories(this.categories);
	}

	public void setCategoryTags(String tags) {
		this.categories = StringConversions.extractCategoriesFromCsv(tags);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getSubCategoryTags() {
		return StringConversions.formCsvFromCategories(this.subCategories);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public void setSubCategoryTags(String tags) {
		this.subCategories = StringConversions.extractCategoriesFromCsv(tags);
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
	public String getProductTags() {
		return StringConversions.formCsvFromCategories(this.products);
	}

	public void setProductTags(String tags) {
		this.products = StringConversions.extractCategoriesFromCsv(tags);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getServiceTypeTags() {
		return StringConversions.formCsvFromCategories(this.serviceTypes);
	}

	public void setServiceTypeTags(String tags) {
		this.serviceTypes = StringConversions.extractCategoriesFromCsv(tags);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getEmailCsv() {
		return StringConversions.formCsvFromEmails(this.emails);
	}

	public void setEmailCsv(String emailCsv) {
		this.emails = StringConversions.extractEmailsFromCsv(emailCsv);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getRetailerPicturesCsv() {
		return StringConversions.formCsvFromLinks(this.retailerPictures);
	}

	public void setRetailerPicturesCsv(String pictureUrlCsv) {
		this.retailerPictures = StringConversions.extractLinksFromCsv(pictureUrlCsv);
	}

	@ApiResourceProperty(ignored = AnnotationBoolean.TRUE)
	public String getPaymentModeCsv() {
		return StringConversions.formCsvFromPaymentModes(this.paymentModes);
	}

	public void setPaymentModeCsv(String paymentModeCsv) {
		this.paymentModes = StringConversions.extractPaymentModesFromCsv(paymentModeCsv);
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

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}

	public PostalAddress getPostalAddress() {
		return this.postalAddress;
	}

	public void setPostalAddress(PostalAddress address) {
		this.postalAddress = address;
	}

	public String getAddress() {
		if (this.postalAddress == null)
			return null;
		return this.postalAddress.getAddress();
	}

	public void setAddress(String address) {
		this.postalAddress = new PostalAddress(address);
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZip() {
		return this.zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public Country getCountry() {
		return this.country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public String getLocality() {
		return this.locality;
	}

	public void setLocality(String locality) {
		this.locality = locality;
	}

	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getSpecialOfferings() {
		return this.specialOfferings;
	}

	public void setSpecialOfferings(String specialOfferings) {
		this.specialOfferings = specialOfferings;
	}

	public List<Category> getCategories() {
		return categories;
	}

	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}

	public List<Category> getSubCategories() {
		return subCategories;
	}

	public void setSubCategories(List<Category> subCategories) {
		this.subCategories = subCategories;
	}

	public List<Category> getBrands() {
		return brands;
	}

	public void setBrands(List<Category> brands) {
		this.brands = brands;
	}

	public List<Category> getKeywords() {
		return keywords;
	}

	public void setKeywords(List<Category> keywords) {
		this.keywords = keywords;
	}

	public List<Category> getProducts() {
		return products;
	}

	public void setProducts(List<Category> products) {
		this.products = products;
	}

	public List<Category> getServiceTypes() {
		return serviceTypes;
	}

	public void setServiceTypes(List<Category> serviceTypes) {
		this.serviceTypes = serviceTypes;
	}

	public Boolean getActive() {
		return this.active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getJoiningDate() {
		return this.joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public List<PhoneNumber> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<PhoneNumber> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public List<PhoneNumber> getSmsNumbers() {
		return smsNumbers;
	}

	public void setSmsNumbers(List<PhoneNumber> smsNumbers) {
		this.smsNumbers = smsNumbers;
	}

	public List<String> getDerivedSmsNumbers() {
		return StringConversions.deriveComparableSmsNumbers(phoneNumbers, this.country);
	}

	public String getWebsiteUrl() {
		if (this.website == null)
			return null;
		return this.website.getValue();
	}

	public void setWebsiteUrl(String website) {
		this.website = StringConversions.formLink(website);
	}

	public Link getWebsite() {
		return website;
	}

	public void setWebsite(Link website) {
		this.website = website;
	}

	public String getContactName() {
		return contactName;
	}

	public void setContactName(String contactName) {
		this.contactName = contactName;
	}

	public List<PhoneNumber> getContactPhones() {
		return contactPhones;
	}

	public void setContactPhones(List<PhoneNumber> contactPhones) {
		this.contactPhones = contactPhones;
	}

	public List<Email> getEmails() {
		return emails;
	}

	public void setEmails(List<Email> emails) {
		this.emails = emails;
	}

	public List<Link> getRetailerPictures() {
		return retailerPictures;
	}

	public void setRetailerPictures(List<Link> retailerPictures) {
		this.retailerPictures = retailerPictures;
	}

	public Link getCoverPicture() {
		return coverPicture;
	}

	public void setCoverPicture(Link coverPicture) {
		this.coverPicture = coverPicture;
	}

	public List<Link> getCustomerPictures() {
		return customerPictures;
	}

	public void setCustomerPictures(List<Link> customerPictures) {
		this.customerPictures = customerPictures;
	}

	public boolean isCrowdSourced() {
		return crowdSourced;
	}

	public void setCrowdSourced(boolean crowdSourced) {
		this.crowdSourced = crowdSourced;
	}

	public User getPartnerLogin() {
		return partnerLogin;
	}

	public void setPartnerLogin(User partnerLogin) {
		this.partnerLogin = partnerLogin;
	}

	public boolean isApproved() {
		return approved;
	}

	public void setApproved(boolean approved) {
		this.approved = approved;
	}

	public User getReviwedBy() {
		return reviwedBy;
	}

	public void setReviwedBy(User reviwedBy) {
		this.reviwedBy = reviwedBy;
	}

	public Float getEditorRating() {
		return editorRating;
	}

	public void setEditorRating(Float editorRating) {
		this.editorRating = editorRating;
	}

	public Float getAvgUserRating() {
		return avgUserRating;
	}

	public void setAvgUserRating(Float avgUserRating) {
		this.avgUserRating = avgUserRating;
	}

	public List<PaymentMode> getPaymentModes() {
		return paymentModes;
	}

	public void setPaymentModes(List<PaymentMode> paymentModes) {
		this.paymentModes = paymentModes;
	}

	public User getAddedBy() {
		return addedBy;
	}

	public void setAddedBy(User addedBy) {
		this.addedBy = addedBy;
	}

	public String getAddedByEmail() {
		return addedByEmail;
	}
	public void setAddedByEmail(String addedByEmail) {
		this.addedByEmail = addedByEmail;
	}

	public String getHoursOfOperation() {
		return hoursOfOperation;
	}

	public void setHoursOfOperation(String hoursOfOperation) {
		this.hoursOfOperation = hoursOfOperation;
	}

	public Boolean getPartner() {
		return partner;
	}

	public void setPartner(Boolean partner) {
		this.partner = partner;
	}

	@Override
	public String toString() {
		return "Retailer [id=" + id + ", name=" + name + ", details=" + details + ", owner=" + owner + ", categories=" + categories
				+ ", subCategories=" + subCategories + ", brands=" + brands + ", lat=" + lat + ", lng=" + lng + ", geoPt=" + geoPt
				+ ", postalAddress=" + postalAddress + ", city=" + city + ", country=" + country + ", zip=" + zip + ", emails=" + emails
				+ ", coverPicture=" + coverPicture + ", retailerPictures=" + retailerPictures + ", customererPictures=" + customerPictures
				+ ", smsNumbers=" + smsNumbers + ", phoneNumbers=" + phoneNumbers + ", website=" + website + ", contactName=" + contactName
				+ ", contactPhones=" + contactPhones + ", crowdSourced=" + crowdSourced + ", partnerLogin=" + partnerLogin 
				+ ", joiningDate=" + joiningDate + ", active=" + active + ", partner=" + partner + ", approved=" + approved + ", reviwedBy=" + reviwedBy + ", addedBy=" + addedBy 
				+ ", editorRating=" + editorRating + ", avgUserRating=" + avgUserRating + ", paymentModes=" + paymentModes + "]";
	}
}

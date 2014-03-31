package com.jmd.shopnet.entity;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.Ref;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.Load;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;

@Data
@Entity
public class Customer {

	@Id
    private Long id;
	
	/********** User Identity *********/
	@Index
    private User user;
	
	/********** My Profile *********/

	@Index
	private String name;
	@Index
    private String email;	
	@Index
    private String phoneCode;
	@Index
    private String phoneNumber;

	
	/********** FB Profile *********/
	@Index
	private String fbId;
	private String fbEmail;	
	private String fbPictureURL;	
		
	private String country;
	private Long socialCoin;
	private String pictureLogin;
	private String pictureUpload;
	
	private List<Address> addresses = new ArrayList<>();		
		
	@Load
	private List<Ref<Business>> business;
	
	
	
	/********** My Location *********/

    @IgnoreSave
    private Float lat;
    @IgnoreSave
    private Float lng;
    
    private GeoPt geoPt;

	
	public void setLat(Float lat) {
        this.lat = lat;
        invalidateGeoPt();
    }

	public Float getLng() {
        return this.lng;
    }

	public void setLng(Float lng) {
        this.lng = lng;
        invalidateGeoPt();
    }

	public GeoPt getGeoPt() {
		if(this.geoPt == null)
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
		if(geoPt != null) {
			this.lat = geoPt.getLatitude();
			this.lng = geoPt.getLongitude();
		} else {
			this.lat = null;
			this.lng = null;
		}
	}

	private void invalidateGeoPt() {
		this.geoPt = null;
	}

	@OnSave
	public void repopulateGeoPt() {
		repopulateGeoPt(this.lat, this.lng);
	}

	private void repopulateGeoPt(Float lat, Float lng) {
		if(lat != null && lng != null) {
			this.geoPt = new GeoPt(lat, lng);
		}
	}

	public void setLatLng(Float lat, Float lng) {
		this.lat = lat;
		this.lng = lng;
		repopulateGeoPt();
	}	
	
	public Key<Customer> geyKey() {
		return Key.create(Customer.class, id);
	}
}

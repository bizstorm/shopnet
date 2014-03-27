package com.jmd.shopnet.entity;
import java.util.ArrayList;
import java.util.List;

import com.google.appengine.api.datastore.GeoPt;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.IgnoreSave;
import com.googlecode.objectify.annotation.Index;
import com.googlecode.objectify.annotation.OnLoad;
import com.googlecode.objectify.annotation.OnSave;

@Entity
public class Customer {

	public Customer() {
		this.setSocialCoin(new Long(0));
	}
	@Id
    private Long id;
	/********** My Profile *********/

	@Index
	private String name;
	@Index
	private String fbId;
	@Index
    private String email;
	@Index
    private String phoneCode;
	@Index
    private String phoneNumber;
	@Index
	private String dob;	
	private String fbEmail;	
	private String fbPictureURL;	
		
	private String country;
	private Long socialCoin;
	private String pictureLogin;
	private String pictureUpload;
	private List<Friend> friends = new ArrayList<>();
	private List<Address> addresses = new ArrayList<>();	
	private List<Customer> group;	
	
	/********** My Location *********/

    @IgnoreSave
    private Float lat;
    @IgnoreSave
    private Float lng;
    private GeoPt geoPt;

	/********** My Offers *********/

	//List<Long> retailEventIdList;

	/********** My Shops *********/

	//List<Long> retailerIdList;

	/********** My Bookmarks *********/

	/********** My Reviews *********/

	/********** My Queries *********/

	/********** My Donations *********/

	/********** User Identity *********/
	@Index
    private User user;
	private String federationId;
	private String federationProvider;

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

	public String getEmail() {
        return this.email;
    }

	public void setEmail(String email) {
        this.email = email;
    }

	public String getPhoneCode() {
        return this.phoneCode;
    }

	public void setPhoneCode(String phoneCode) {
        this.phoneCode = phoneCode;
    }

	public String getPhoneNumber() {
        return this.phoneNumber;
    }

	public void setPhone(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

	public String getDOB() {
        return this.dob;
    }

	public void setDOB(String dob) {
        this.dob = dob;
    }

	


	public String getCountry() {
        return this.country;
    }

	public void setCountry(String country) {
        this.country = country;
    }


	public Long getSocialCoin() {
		return this.socialCoin;
	}

	public void setSocialCoin(Long socialCoin) {
		this.socialCoin = socialCoin;
	}

	public String getPictureLogin() {
        return this.pictureLogin;
    }

	public void setPictureLogin(String pictureLogin) {
        this.pictureLogin = pictureLogin;
	}

	public String getPictureUpload() {
        return this.pictureUpload;
    }

	public void setPictureUpload(String pictureUpload) {
        this.pictureUpload = pictureUpload;
	}
	
	public Float getLat() {
        return this.lat;
    }

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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFederationId() {
		return federationId;
	}

	public void setFederationId(String federationId) {
		this.federationId = federationId;
	}

	public String getFederationProvider() {
		return federationProvider;
	}

	public void setFederationProvider(String federationProvider) {
		this.federationProvider = federationProvider;
	}
	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}
	
	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}
	
	public String getFbEmail() {
		return fbEmail;
	}

	public void setFbEmail(String fbEmail) {
		this.fbEmail = fbEmail;
	}

	public String getFbPictureURL() {
		return fbPictureURL;
	}

	public void setFbPictureURL(String fbPictureURL) {
		this.fbPictureURL = fbPictureURL;
	}
}

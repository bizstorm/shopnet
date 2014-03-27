package com.jmd.shopnet.utils;

import java.util.logging.Level;
import java.util.logging.Logger;


import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;

public class SignIn {
	protected static Logger log = Logger.getLogger(SignIn.class.getName());

	
	private Boolean signed;
	private String name;
	private String logoutURL;
	private User user;


	
	private Map<String, String> loginAttributes;
	
	public SignIn(){
		this.user = null;
	}

	public SignIn(User user){
		this.user = user;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getSigned() {
		return this.signed;
	}

	public void setSigned(Boolean signed) {
		this.signed = signed;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogoutURL() {
		return this.logoutURL;
	}

	public void setLogoutURL(String logoutURL) {
		this.logoutURL = logoutURL;
	}

	public Map<String, String> getLoginAttributes() {
		return this.loginAttributes;
	}

	public void setLoginAttributes(Map<String, String> loginAttributes) {
		this.loginAttributes = loginAttributes;
	}

	public void sign(String destinationURL){
		if(log.isLoggable(Level.FINE))
    	  log.fine("RetailerSignIn sign destinationURL : " + destinationURL);

		UserService userService = UserServiceFactory.getUserService();
        //User user = userService.getCurrentUser(); // or req.getUserPrincipal()

		User user = getUser();

		if(user == null)
		{
			if(log.isLoggable(Level.FINE))
    			log.fine("RetailerSignIn sign before user : " + user);
			user = userService.getCurrentUser();

			if(log.isLoggable(Level.FINE))
    			log.fine("RetailerSignIn sign after user : " + user);
		}
        
		Map<String, String> authDomainMap = new HashMap<String, String>();
		authDomainMap.put("Google", "google.com");
		authDomainMap.put("Yahoo", "yahoo.com");
        
        if (user != null) {
        	String nickName = user.getNickname();
        	String authDomain = authDomainMap.get("Google");
			String logoutURL = userService.createLogoutURL(destinationURL, authDomain);

			setSigned(new Boolean(true));
			setName(nickName);
			setLogoutURL(logoutURL);
        }
		else
        {
			final Map<String, String> openIdProviders;
			Set<String> attributesRequest = new HashSet<String>();
			Map<String, String> loginAttributes = new HashMap<String, String>();

			String return_to = "openid.return_to=" + destinationURL;
			
            openIdProviders = new HashMap<String, String>();
            openIdProviders.put("Google", "https://www.google.com/accounts/o8/id");
            openIdProviders.put("Yahoo", "http://open.login.yahooapis.com/openid20/www.yahoo.com/xrds");

			attributesRequest.add("openid.mode=checkid_immediate");
			attributesRequest.add("openid.ns=http://specs.openid.net/auth/2.0");
			attributesRequest.add(return_to);

			setSigned(new Boolean(false));
        	
        	for (String providerName : openIdProviders.keySet()) {
                String providerUrl = openIdProviders.get(providerName);
                String authDomain = authDomainMap.get(providerName);
                String loginUrl = userService.createLoginURL(destinationURL, authDomain, providerUrl, attributesRequest);
				loginAttributes.put(providerName, loginUrl);
            }

			setLoginAttributes(loginAttributes);
        }
	}


	@Override
	public String toString() {
		return "Retailer SignIn [signed=" + signed + ", name=" + name + ", logoutURL=" + logoutURL + ", openIdProviders=" + loginAttributes + "]";
	}
}

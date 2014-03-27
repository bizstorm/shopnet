package com.jmd.shopnet.entity;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.dao.RetailerOfy;
import com.jmd.shopnet.utils.SignIn;
import com.jmd.shopnet.utils.SignInUser;

//@Api(name = "retailscanner", namespace = @ApiNamespace(ownerDomain = "jmd.shopnet.com", ownerName = "jmd.shopnet.com", packagePath = "entity"))
//@Api(name = "retailscanner",version = "v1")
		//,clientIds = {Ids.WEB_CLIENT_ID}
		//,scopes = {Ids.SCOPES})
/**
 * @deprecated - use PartnerDataServiceV1 instead
 */
@Deprecated
public class RetailerDataServiceV1 {
	protected static Logger log = Logger.getLogger(RetailerDataServiceV1.class.getName());
	
	private RetailerOfy retailerOfy;
	
	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	//@ApiMethod(name = "retailers.fetch")
	public Retailer getRetailer(@Named("id") Long id) {
		return this.retailerOfy.fetchRetailer(id);
	}

	//@ApiMethod(name = "retailer.createByProps")
	public void createRetailer(
			@Named("name") String name
			, @Named("latitude") double lat
			, @Named("longitude") double lng
			, @Nullable @Named("details") String details
			, @Nullable @Named("categories") String categoryCsv
			, @Nullable @Named("subCategories") String subCategoryCsv
			, @Nullable @Named("brands") String brandCsv
			, @Nullable @Named("phones") String phoneCsv
			, @Nullable @Named("smsNumbers") String smsNumberCsv
			, @Nullable @Named("emails") String emailCsv
			, @Nullable @Named("website") String website
			, @Nullable @Named("address") String address
			, @Nullable @Named("country") String country
			, @Nullable @Named("zip") String zip
			, @Nullable @Named("joiningDate") Date joiningDate
			, @Nullable @Named("contactName") String contactName
			, @Nullable @Named("contactPhones") String contactPhoneCsv
			, @Nullable @Named("rating") Float rating
			, @Nullable @Named("owner") String owner
			) {
		Retailer r = new Retailer();
		r.setName(name);
		r.setLatLng((float)lat, (float)lng);
		r.setDetails(details);
		r.setPhoneCsv(phoneCsv);
		r.setEmailCsv(emailCsv);
		r.setAddress(address);
		r.setCategoryTags(categoryCsv);
		r.setSubCategoryTags(subCategoryCsv);
		r.setBrandTags(brandCsv);
		//r.setCity(City.valueOf(city));
		r.setContactName(contactName);
		r.setContactPhoneCsv(contactPhoneCsv);
		r.setCountry(Country.valueOf(country));//TODO handle parse exception
		r.setJoiningDate(joiningDate);//TODO handle parse exception
		r.setOwner(owner);
		r.setEditorRating(rating);
		r.setSmsCsv(smsNumberCsv);
		r.setWebsiteUrl(website);
		r.setZip(zip);
		log.info("creating retailers: " + r);
		Key<Retailer> key = this.retailerOfy.saveEntity(r);
		r.setId(key.getId());
	}


/*    @ApiMethod(
		name = "retailers.signGoogleId"
		,httpMethod = "POST"
		,clientIds = {Ids.WEB_CLIENT_ID}
		,scopes = {Ids.SCOPES}
	)*/
	public SignIn signInRetailerGoogle(@Named("destinationURL") String destinationURL, User user)
			throws OAuthRequestException, IOException
	{		
		if(log.isLoggable(Level.FINE))
			log.fine("signInRetailers called with destimationURL: " + destinationURL);

		/*if(log.isLoggable(Level.FINE) && req == null)
			log.fine("signInRetailers called with req null " );
		else
		{
			log.fine("signInRetailers called with req exist ");
			log.fine("signInRetailers called with req getRemoteUser() " + req.getRemoteUser() + " getRequestURL() " + req.getRequestURL().toString() );

			HttpSession httpsess = req.getSession();
			if(httpsess != null)
				log.fine(" sess present " + httpsess.getAttribute("user"));
			else
				log.fine("sess absent");

			Cookie authCookie = getAuthCookie(req.getCookies());
			if(authCookie != null){
				log.fine("Cookie present");
				Session sess = ofy.load().key(Key.create(Session.class,authCookie.getValue())).now();
				if(!sess.isExpired()){
					log.fine("session present" + sess.getAttribute("user"));
				}
				else
					log.fine("session absent");
			}  
			else
				log.fine("Cookie absent");
		}*/

		if(log.isLoggable(Level.FINE) && user == null)
			log.fine("signInRetailers called with user null " );
		else
			log.fine("signInRetailers called with user exist " );

		SignIn signIn = new SignIn(user);

		signIn.sign(destinationURL);	
		
		if(log.isLoggable(Level.FINE))
			log.fine("signInRetailers signed? : " + signIn.getSigned());

		return signIn;
	}

/*@ApiMethod(
		name = "retailers.signUserValue"
		,httpMethod = "POST"
		,clientIds = {Ids.WEB_CLIENT_ID}
		,scopes = {Ids.SCOPES}	)*/
	public SignInUser signUserValue(User user)
     throws OAuthRequestException, IOException
	{	
		SignInUser signinuser = new SignInUser();
		if(log.isLoggable(Level.FINE) && user == null)
		{
			log.fine("signInRetailers called with user null " );
			signinuser.setSigned(new Boolean(false));
			signinuser.setNickName("Invalid");
			signinuser.setEmail("Invalid");
			signinuser.setUserString("Invalid");
			return signinuser;
		}
		else
		{
			log.fine("signInRetailers called with user exist : " + user.toString());
			signinuser.setSigned(new Boolean(true));
			signinuser.setNickName(user.getNickname());
			signinuser.setEmail(user.getEmail());
			signinuser.setUserString(user.toString());
			return signinuser;
		}
	}

	/* Commented Open-Id endpoint code
	//@ApiMethod(
		name = "retailers.signOpenId",
		httpMethod = HttpMethod.GET,
		scopes = { "https://www.googleapis.com/auth/userinfo.profile" , "https://www.googleapis.com/auth/userinfo.email" }
	)
	public RetailerSignIn signInRetailerOpen(HttpServletRequest req)
		throws IOException
	//public RetailerSignIn signInRetailer(@Nullable @Named("destinationURL") String destinationURL, User user)	
	{
		String destinationURL = "";
		
		if(log.isLoggable(Level.FINE))
			log.fine("signInRetailers called with destimationURL: " + destinationURL);

		destinationURL = "https://scannertest12345.appspot.com/data/import.html";
		//destinationURL = "http://localhost:8888/data/import.html";


		UserService userService = UserServiceFactory.getUserService();
        User newUser = userService.getCurrentUser();


		RetailerSignIn signIn = new RetailerSignIn(newUser);

		signIn.sign(destinationURL);	
		
		if(log.isLoggable(Level.FINE))
			log.fine("signInRetailers signed? : " + signIn.getSigned());

		return signIn;
	}
	*/
	
	public RetailerOfy getRetailerOfy() {
		return retailerOfy;
	}

	@Inject
	public void setRetailerOfy(RetailerOfy retailerOfy) {
		this.retailerOfy = retailerOfy;
	}
}

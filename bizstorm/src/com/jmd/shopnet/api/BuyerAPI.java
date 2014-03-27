package com.jmd.shopnet.api;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
//import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.Nullable;
import javax.inject.Named;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.InternalServerErrorException;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.search.GetRequest;
import com.google.appengine.api.search.SearchException;
import com.google.appengine.api.users.User;
import com.google.inject.Inject;
import com.googlecode.objectify.Key;
import com.jmd.shopnet.dao.BookmarkOfy;
import com.jmd.shopnet.dao.CustomerOfy;
import com.jmd.shopnet.dao.RetailerOfy;
//import com.jmd.shopnet.entity.Country;
import com.jmd.shopnet.entity.Bookmark;
import com.jmd.shopnet.entity.Country;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.entity.Friend;
import com.jmd.shopnet.entity.Retailer;
import com.jmd.shopnet.search.RetailerIndexer;
import com.jmd.shopnet.search.RetailerSearch;
import com.jmd.shopnet.utils.Ids;
//import com.jmd.shopnet.utils.SignIn;
//import com.jmd.shopnet.utils.SignInUser;
import com.jmd.shopnet.utils.StringConversions;

/**
 * Defines v1 of a Search Interface as part of the endpoints API, which
 * provides clients the ability to query for geospatial search for items of
 * interest.
 */
@Api(name = "sts"
		,version = "v1"
		,clientIds = {Ids.WEB_CLIENT_ID, com.google.api.server.spi.Constant.API_EXPLORER_CLIENT_ID}
		,scopes = {Ids.SCOPES}
		)
public class BuyerAPI {
	public static final int MAX_API_LIMIT = 200;
	Logger log = Logger.getLogger(BuyerAPI.class.getName());
	private RetailerOfy retailerOfy;

	private CustomerOfy customerOfy;
	private BookmarkOfy bookmarkOfy;


	private RetailerSearch retailerSearch;

	private RetailerIndexer retailerIndexer;


	@ApiMethod(name="search.shops", httpMethod = "POST")
	public List<Retailer> searchRetailers(
			@Named("latitude") double lat//Not float - to handle JS 64-bit numbers
			,@Named("longitude") double lng
			,@Nullable @Named("radius") Float radius
			,@Nullable @Named("categories") String categoryCsv
			,@Nullable @Named("sort") String sort
			,@Nullable @Named("limit") Integer limit
			,@Nullable @Named("offset") Integer offset
			,@Nullable User user
	) throws InternalServerErrorException {
		if(limit == null)
			limit = RetailerIndexer.DEFAULT_LIMIT;
		else if(limit > RetailerIndexer.MAX_LIMIT)
			limit = RetailerIndexer.MAX_LIMIT;
		if(radius == null)
			radius = RetailerIndexer.DEFAULT_RADIUS;
		else if(radius > RetailerIndexer.MAX_RADIUS)
			radius = RetailerIndexer.MAX_RADIUS;
		List<Category> categories = null;
		if(categoryCsv != null && !categoryCsv.isEmpty()) {
			categories = StringConversions.extractCategoriesFromCsv(categoryCsv);
		}
		try {
			return retailerSearch.searchRetailers(lat, lng, radius, categories, sort, limit, offset);
		} catch (SearchException e) {
			log.logp(Level.SEVERE, BuyerAPI.class.getName(), "searchRetailers", "Shop search failed. " + e.getMessage(), e);
			throw new InternalServerErrorException("Shop search failed. ", e);
		}
	}

	


	

	
	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param retailer the entity to be inserted.
	 * In APIs Explorer, joiningDate can be given in ISO-8601 date format. e.g. 2013-09-29.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="admin.retailer.save", path="admin_retailer_save")
	public void insertRetailer(
		Retailer retailer
		,User user
		) throws OAuthRequestException, IOException {
		Key<Retailer> key = this.retailerOfy.saveEntity(retailer);
		if(log.isLoggable(Level.FINE))
			log.fine("Added Retailer with key: " + key);
	}

	/**
	 * This method is used for updating an existing entity. If the entity does not
	 * exist in the datastore, an exception is thrown.
	 * It uses HTTP PUT method.
	 *
	 * @param retailer the entity to be updated.
	 * @return The updated entity.
	@ApiMethod(name="admin.retailer.update")
	public void updateRetailer(Retailer retailer) {
		Key<Retailer> key = this.retailerOfy.saveEntity(retailer);
		if(log.isLoggable(Level.FINE))
			log.fine("Updated Retailer with key: " + key);
	}
	 */
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@ApiMethod(name="admin.retailers.list")
	public List<Retailer> listRetailer(
			@Nullable @Named("cursor") String cursorString,
			@Nullable @Named("limit") Integer limit
			) {
		if(limit == null)
			limit = new Integer(RetailerIndexer.DEFAULT_LIMIT);
		return this.retailerOfy.fetchRetailers(limit);
		//return CollectionResponse.<Retailer> builder().setItems(execute).setNextPageToken(cursorString).build();
	}
	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name="admin.retailer.removeById")
	public void removeRetailer(@Named("id") Long id) {
		this.retailerOfy.deleteEntity(id);
	}
	@ApiMethod(name="admin.retailers.removeK")//TODO delete method before going live
	public void removeRetailersK() {
		List<Retailer> retailers = this.retailerOfy.deleteEntities(1000);
		this.retailerIndexer.deleteFromIndex(retailers);
	}

	@ApiMethod(name="admin.retailers.cleanData")//TODO delete method before going live
	public void cleanData() {
		log.info("admin.retailers.cleanData: starting With clean-up");
		// delete all entries from all the entitiles :
		// 1 : delete event reviews.


		// 4 : delete retailers.
		this.retailerOfy.deleteEntities(MAX_API_LIMIT);

		// 5 : delete bookmarkOfy
		this.bookmarkOfy.deleteEntities(MAX_API_LIMIT);


		// 7 : delete customer.
		this.customerOfy.deleteEntities(MAX_API_LIMIT);

		// Now delete the indexes
		
		final GetRequest request = GetRequest.newBuilder().setLimit(MAX_API_LIMIT) // max limit allowed to fetch indexing
				.setReturningIdsOnly(true).build();

		
		

		
	
		
		log.info("admin.retailers.cleanData  : Done With clean -up");
	}
	
	@ApiMethod(name="admin.retailers.deleteSearchSchema")//TODO delete method before going live
	public void deleteRetailerSearchSchema() {
		this.retailerIndexer.deleteSchema();
	}

	
	/**
	 * This method lists all the entities inserted in datastore.
	 * It uses HTTP GET method and paging support.
	 *
	 * @return A CollectionResponse class containing the list of all entities
	 * persisted and a cursor to the next page.
	 */
	@ApiMethod(name="admin.customers.list")
	public List<Customer> listCustomer(
			@Nullable @Named("cursor") String cursorString
			,@Nullable @Named("limit") Integer limit
			,User user
			) throws OAuthRequestException, IOException {
		return this.customerOfy.fetchCustomers(limit);
	}
	/**
	 * This method removes the entity with primary key id.
	 * It uses HTTP DELETE method.
	 *
	 * @param id the primary key of the entity to be deleted.
	 */
	@ApiMethod(name="admin.customer.removeById")
	public void removeCustomer(
		@Named("id") Long id
		,User user
		) throws OAuthRequestException, IOException {
		this.customerOfy.deleteEntity(id);
	}

	@ApiMethod(name="admin.customers.removeK")//TODO delete method before going live
	public void removeCustomersK(
		User user) throws OAuthRequestException, IOException {
		this.customerOfy.deleteEntities(1000);
	}

	/* User */

	/*
	@ApiMethod(
		name="user.signin.gae"
		,httpMethod = "POST"
	)
	public SignIn signInGae(@Named("destinationURL") String destinationURL, User user) throws OAuthRequestException, IOException {
		if (log.isLoggable(Level.FINE))
			log.fine("signInRetailers called with destimationURL: " + destinationURL);

		if (log.isLoggable(Level.FINE) && user == null)
			log.fine("signInRetailers called with user null ");
		else
			log.fine("signInRetailers called with user exist ");

		SignIn signIn = new SignIn(user);
		signIn.sign(destinationURL);

		if (log.isLoggable(Level.FINE))
			log.fine("signInRetailers signed? : " + signIn.getSigned());

		return signIn;
	}
	*/


	/*
	
	@ApiMethod(
		name="user.signin.verify"
		,httpMethod = "POST"
	)
	public SignInUser verifySignedin(User user) throws OAuthRequestException, IOException {
		SignInUser signinuser = new SignInUser();
		if (log.isLoggable(Level.FINE))
			log.fine("verifySignedin found no user.");
		if (user == null) {
			signinuser.setSigned(new Boolean(false));
			signinuser.setNickName("Invalid");
			signinuser.setEmail("Invalid");
			signinuser.setUserString("Invalid");
			return signinuser;
		} else {
			log.info("signin info for user: " + user);
			signinuser.setSigned(new Boolean(true));
			signinuser.setNickName(user.getNickname());
			signinuser.setEmail(user.getEmail());
			signinuser.setUserString(user.toString());
			return signinuser;
		}
	}

	*/


	/* User-Customer */

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name="user.customer.fetch", path="user_customer_fetch")
	public Customer fetchCustomer(
		@Named("id") Long id
		,User user
		) throws OAuthRequestException, IOException {
		return this.customerOfy.fetchCustomer(id);
	}

	@ApiMethod(name="user.customer.fetchByUser", path="user_customer_fetchByUser")
	public List<Customer> fetchCustomerByuser(
		User user
		) throws OAuthRequestException, IOException {
		return this.customerOfy.fetchByUser(user);
	}

	@ApiMethod(name="user.customer.fetchByName", path="user_customer_fetchByName")
	public List<Customer> fetchCustomerByName(
		@Named("name") String name
		,User user
		) throws OAuthRequestException, IOException {
		return this.customerOfy.fetchByName(name);
	}

	@ApiMethod(name="user.customer.fetchByEmail", path="user_customer_fetchByEmail")
	public List<Customer> fetchCustomerByEmail(
		User user
		) throws OAuthRequestException, IOException {
		if(null == user)
		{
			log.info("found no user.");
			return new java.util.LinkedList<Customer>();
		}
		else
		{
			log.info("user found. " + user.getEmail());
			return this.customerOfy.fetchByEmail(user.getEmail());
		}
	}

	@ApiMethod(name="user.customer.fetchByPhone", path="user_customer_fetchByPhone")
	public List<Customer> fetchCustomerByPhone(
		@Named("name") String phone
		,User user
		) throws OAuthRequestException, IOException {
		return this.customerOfy.fetchByPhone(phone);
	}

	@ApiMethod(name="user.customer.fetchByDOB", path="user_customer_fetchByDOB")
	public List<Customer> fetchCustomerByDOB(
		@Named("name") String dob
		,User user
		) throws OAuthRequestException, IOException {
		return this.customerOfy.fetchByDOB(dob);
	}

	@ApiMethod(name="user.customer.createByProps", path="user_customer_createByProps")
	public List<Customer> createCustomer(
			@Named("name") String name
			,@Nullable @Named("picture") String picture
			,User user
			) throws OAuthRequestException, IOException {
		if(null == user)
		{
			if (log.isLoggable(Level.FINE))
				log.fine("found no user.");

			return new java.util.LinkedList<Customer>();
		}

		log.info("signin info for user: " + user);

		//custList = this.customerOfy.fetchByUser(user);
		List<Customer> custList = this.customerOfy.fetchByEmail(user.getEmail());
		
		log.info("customer list.size(): " + custList.size());
		if(custList.size() > 0)
		{
			log.info("get customer: " + custList.get(0));
			return custList;
		}
		else
		{
			Customer e = new Customer();
			e.setName(name);

			if(null == picture)
			{
				log.info("picture is null: ");
			}
			else
			{
				e.setPictureLogin(picture);
			}
			e.setEmail(user.getEmail());
			
			log.info("creating customer: " + e);
			Key<Customer> key = this.customerOfy.saveEntity(e);
			if(key != null) {
				e.setId(key.getId());
				custList.add(e);
				//customerIndexer.pushToIndex(e);//TODO Handle Exception
				return custList;
			} else {
				return custList;
			}
		}
	}

	@ApiMethod(name="user.customer.saveLatLng", path="user_customer_saveLatLng")
	public void saveLatLng(
			@Named("latitude") double latitude
			,@Named("longitude") double longitude
			,User user
			) throws OAuthRequestException, IOException {
		if(null == user)
			log.info("user is null: ");
		else
		{
			List<Customer> custList = this.customerOfy.fetchByEmail(user.getEmail());
			
			if(custList.size() > 0)
			{
				Customer customer =  custList.get(0);
				
				try {
					customer.setLatLng(new Float(latitude), new Float(longitude));
					Key<Customer> key = this.customerOfy.saveEntity(customer);
					
					if(log.isLoggable(Level.FINE))
						log.fine("Added Customer with key: " + key);
				} catch (Exception e) {
					log.logp(Level.SEVERE, BuyerAPI.class.getName(), "insertCustomer", "insert customer failed. " + e.getMessage(), e);
				}
			}
		}
	}

	@ApiMethod(name="user.customer.genPictureName", path="user_customer_genPictureName")
	public Map<String, String> genPictureName(
			User user
			) throws OAuthRequestException, IOException {
		Map<String, String> map = new HashMap<String, String>();
		map.put("picture", null);
		if(null == user)
			log.info("user is null: ");
		else
		{
			List<Customer> custList = this.customerOfy.fetchByEmail(user.getEmail());
			
			if(custList.size() > 0)
			{
				Customer customer =  custList.get(0);
				
				try {
					String date = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss").format(new Date());
					String pictureName = "" + customer.getId() + date;

					if(log.isLoggable(Level.FINE))
						log.fine("pictureName: " + pictureName);

					map.put("picture", pictureName);
				} catch (Exception e) {
					log.logp(Level.SEVERE, BuyerAPI.class.getName(), "genPictureName", "generate Picture Name failed. " + e.getMessage(), e);
				}
			}
		}
		return map;
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param customer the entity to be inserted.
	 * In APIs Explorer, joiningDate can be given in ISO-8601 date format. e.g. 2013-09-29.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="user.customer.save", path="user_customer_save")
	public void insertCustomer(
		Customer customer
		, User user
		) throws OAuthRequestException, IOException{
		Key<Customer> key = this.customerOfy.saveEntity(customer);
		if(log.isLoggable(Level.FINE))
			log.fine("Added Customer with key: " + key);
	}
	
	@ApiMethod(name="user.customer.update", path="user_customer_update")
	public Customer updateCustomer(
			Customer customer			
			,User user
			) throws OAuthRequestException, IOException {
		
		if(customer != null && customer.getFbId() != null && customer.getFbId() != ""){			
			List<Friend> friends = customer.getFriends();
			log.info("Updating customer customer : "+customer.getEmail()+", friends : "+ friends.size());
			for (Friend friend : friends) {
				if(friends != null){
					List<Customer> existingCustomer = this.customerOfy.fetchByFbId(friend.getFbId());
					if(existingCustomer != null && existingCustomer.size() > 0){
						friend.setEmail(existingCustomer.get(0).getEmail());
					}
				}
				
			}
			Key<Customer> key = this.customerOfy.saveEntity(customer);
			if(key != null) {
				customer.setId(key.getId());
			}
		}	
		return customer;
	}

	

	
	@ApiMethod(name="user.retailer.createByProps", path="user_retailer_createByProps")
	public Retailer createRetailer(
			@Named("name") String name
			, @Named("latitude") double lat
			, @Named("longitude") double lng
			, @Nullable @Named("type") String type
			, @Nullable @Named("details") String details
			, @Nullable @Named("categories") String categoryCsv
			, @Nullable @Named("subCategories") String subCategoryCsv
			, @Nullable @Named("brands") String brandCsv
			, @Nullable @Named("phones") String phoneCsv
			, @Nullable @Named("smsNumbers") String smsNumberCsv
			, @Nullable @Named("emails") String emailCsv
			, @Nullable @Named("website") String website
			, @Nullable @Named("address") String address
			, @Nullable @Named("locality") String locality
			//, @Nullable @Named("country") String country
			, @Nullable @Named("zip") String zip
			//, @Nullable @Named("joiningDate") Date joiningDate
			, @Nullable @Named("contactName") String contactName
			, @Nullable @Named("contactPhones") String contactPhoneCsv
			, @Nullable @Named("rating") Float rating
			, @Nullable @Named("owner") String owner
			, @Nullable @Named("specialOfferings") String specialOfferings
			,User user
			) throws OAuthRequestException, IOException{

//		if(null == user)
//		{
//			log.severe("user.retailer.createByProps: User not present: ");
//			return null;
//		}
		Retailer r = new Retailer();
		r.setCrowdSourced(true);
		r.setName(name);
		r.setLatLng((float)lat, (float)lng);
		r.setDetails(details);
		r.setPhoneCsv(phoneCsv);
		r.setEmailCsv(emailCsv);
		r.setAddress(address);
		r.setLocality(locality);
		r.setCategoryTags(categoryCsv);
		r.setSubCategoryTags(subCategoryCsv);
		r.setBrandTags(brandCsv);
		//r.setCity(City.valueOf(city));
		r.setContactName(contactName);
		r.setContactPhoneCsv(contactPhoneCsv);
		//r.setCountry(Country.valueOf(country));//TODO handle parse exception
		//r.setJoiningDate(joiningDate);//TODO handle parse exception
		r.setOwner(owner);
		r.setSpecialOfferings(specialOfferings);
		//r.setRating(rating);
		r.setSmsCsv(smsNumberCsv);
		r.setWebsiteUrl(website);
		r.setZip(zip);
		r.setAddedByEmail(user.getEmail());
		log.info("creating retailer: " + r);
		Key<Retailer> key = this.retailerOfy.saveEntity(r);//TODO Handle Exception
		if(key != null) {
			r.setId(key.getId());
			String status = retailerIndexer.pushToIndex(r);//TODO Handle Exception
			log.info(status);
			return r;
		} else {
			log.severe("Key not found for retailer: " + r);
			return null;
		}
	}


	@ApiMethod(name="user.retailer.setProps", path="user_retailer_setProps")
	public Retailer setRetailer(
			  @Named("id") Long id
			, @Nullable @Named("name") String name
			, @Nullable @Named("latitude") double lat
			, @Nullable @Named("longitude") double lng
			, @Nullable @Named("type") String type
			, @Nullable @Named("details") String details
			, @Nullable @Named("categories") String categoryCsv
			, @Nullable @Named("subCategories") String subCategoryCsv
			, @Nullable @Named("brands") String brandCsv
			, @Nullable @Named("phones") String phoneCsv
			, @Nullable @Named("smsNumbers") String smsNumberCsv
			, @Nullable @Named("emails") String emailCsv
			, @Nullable @Named("website") String website
			, @Nullable @Named("address") String address
			, @Nullable @Named("locality") String locality
			//, @Nullable @Named("country") String country
			, @Nullable @Named("zip") String zip
			//, @Nullable @Named("joiningDate") Date joiningDate
			, @Nullable @Named("contactName") String contactName
			, @Nullable @Named("contactPhones") String contactPhoneCsv
			, @Nullable @Named("rating") Float rating
			, @Nullable @Named("owner") String owner
			, @Nullable @Named("specialOfferings") String specialOfferings
			,User user
			) throws OAuthRequestException, IOException{

		Retailer r = this.retailerOfy.fetchRetailer(id);

		if(null != name)
			r.setName(name);
		//if(null != lat && null != lng)
			//r.setLatLng((float)lat, (float)lng);
		if(null != details)
			r.setDetails(details);
		if(null != categoryCsv)
			r.setCategoryTags(categoryCsv);
		if(null != subCategoryCsv)
			r.setSubCategoryTags(subCategoryCsv);
		if(null != brandCsv)
			r.setBrandTags(brandCsv);
		if(null != phoneCsv)
			r.setPhoneCsv(phoneCsv);
		if(null != smsNumberCsv)
			r.setSmsCsv(smsNumberCsv);
		if(null != emailCsv)
			r.setEmailCsv(emailCsv);
		if(null != website)
			r.setWebsiteUrl(website);
		if(null != address)
			r.setAddress(address);
		if(null != locality)
			r.setLocality(locality);
		
		//r.setCity(City.valueOf(city));
		if(null != zip)
			r.setZip(zip);
		if(null != contactName)
			r.setContactName(contactName);
		if(null != contactPhoneCsv)
			r.setContactPhoneCsv(contactPhoneCsv);
		//r.setCountry(Country.valueOf(country));//TODO handle parse exception
		//r.setJoiningDate(joiningDate);//TODO handle parse exception
		if(null != owner)
			r.setOwner(owner);
		if(null != specialOfferings)
			r.setSpecialOfferings(specialOfferings);
		//r.setRating(rating);
		
		
		
		log.info("setting retailer: " + r);
		Key<Retailer> key = this.retailerOfy.saveEntity(r);//TODO Handle Exception
		if(key != null) {
			return r;
		} else {
			log.severe("Key not found for retailer: " + r);
			return null;
		}
	}

	/**
	 * This method gets the entity having primary key id. It uses HTTP GET method.
	 *
	 * @param id the primary key of the java bean.
	 * @return The entity with primary key id.
	 */
	@ApiMethod(name="user.retailer.fetch", path="user_retailer_fetch")
	public Retailer fetchRetailer(
		@Named("id") Long id
		,User user
		) throws OAuthRequestException, IOException {
		return this.retailerOfy.fetchRetailer(id);
	}

	@ApiMethod(name="user.retailer.fetchByUser", path="user_retailer_fetchByUser")
	public List<Retailer> fetchRetailerByUser(
		User user
		) throws OAuthRequestException, IOException {
		if(null == user)
		{
			return new java.util.LinkedList<Retailer>();
		}
		else
		{
			return this.retailerOfy.fetchByUserEmail(user.getEmail());
		}
	}

	/**
	 * This inserts a new entity into App Engine datastore. If the entity already
	 * exists in the datastore, an exception is thrown.
	 * It uses HTTP POST method.
	 *
	 * @param retailer the entity to be inserted.
	 * In APIs Explorer, joiningDate can be given in ISO-8601 date format. e.g. 2013-09-29.
	 * @return The inserted entity.
	 */
	@ApiMethod(name="user.retailer.save", path="user_retailer_save")
	public void insertRetailerCustomer(
		Retailer retailer
		, User user
		) throws OAuthRequestException, IOException{
		Key<Retailer> key = this.retailerOfy.saveEntity(retailer);
		if(log.isLoggable(Level.FINE))
			log.fine("Added Retailer with key: " + key);
	}



	/* User-Bookmark */

	@ApiMethod(name="user.bookmark.createByProps", path="user_bookmark_createByProps")
	public Bookmark createBookmark(
			 @Named("retailEventId") Long retailEventId
			,@Named("retailerId") Long retailerId
			,User user
			)throws OAuthRequestException, IOException {
		if(null == user)
		{
			log.severe("user.bookmark.createByProps: User not present: ");
			return null;
		}

		Bookmark b = new Bookmark();
		Key<Retailer> retailerKey = Key.create(Retailer.class, retailerId);
		List<Customer> custList = this.customerOfy.fetchByEmail(user.getEmail());
		b.setCustomer(custList.get(0));
		
		log.info("creating bookmark: " + b);
		Key<Bookmark> key = this.bookmarkOfy.saveEntity(b);
		if(key != null) {
			b.setId(key.getId());
			return b;
		} else {
			return null;
		}
	}

	@ApiMethod(name="user.bookmark.fetchByUser", path="user_bookmark_fetchByUser")
	public List<Bookmark> fetchBookmarkByUser(
		 User user
		) throws OAuthRequestException, IOException {
		if(null == user)
		{
			return new java.util.LinkedList<Bookmark>();
		}
		else
		{
			List<Customer> custList = this.customerOfy.fetchByEmail(user.getEmail());
			if(false == custList.isEmpty())
				return this.bookmarkOfy.fetchByCustomer(custList.get(0));
			else
				return new java.util.LinkedList<Bookmark>();
		}
	}


	
	@ApiMethod(name = "user.retailer.match")
	public List<Retailer> getMatchingRetailersByName(
			@Named("lat") double lat
			, @Named("lng") double lng
			, @Nullable @Named("name") String name
			, @Nullable @Named("limit") Integer limit) {
		return retailerSearch.getMatchingRetailersByName(lat, lng, name, limit);
	}

	@ApiMethod(name="admin.retailer.createByProps")
	public Retailer createApprovedRetailer(
			@Named("name") String name
			, @Named("latitude") double lat
			, @Named("longitude") double lng
			, @Named("agentId") String agentId
			, @Nullable @Named("details") String details
			, @Nullable @Named("categories") String categoryCsv
			, @Nullable @Named("subCategories") String subCategoryCsv
			, @Nullable @Named("brands") String brandCsv
			, @Nullable @Named("phones") String phoneCsv
			, @Nullable @Named("smsNumbers") String smsNumberCsv
			, @Nullable @Named("emails") String emailCsv
			, @Nullable @Named("pictureUrl") String pictureUrlCsv
			, @Nullable @Named("website") String website
			, @Nullable @Named("address") String address
			, @Nullable @Named("locality") String locality
			, @Nullable @Named("country") String country
			, @Nullable @Named("zip") String zip
			, @Nullable @Named("joiningDate") Date joiningDate
			, @Nullable @Named("contactName") String contactName
			, @Nullable @Named("contactPhones") String contactPhoneCsv
			, @Nullable @Named("paymentModes") String paymentModeCsv
			, @Nullable @Named("keywords") String keywordCsv
			, @Nullable @Named("products") String productCsv
			, @Nullable @Named("serviceTypes") String serviceTypeCsv
			, @Nullable @Named("rating") Double rating
			, @Nullable @Named("owner") String owner
			, @Nullable @Named("isPartner") boolean isPartner
			, @Nullable @Named("specialOfferings") String specialOfferings
			, @Nullable @Named("workdays") String workdays//TODO handle
			, @Nullable @Named("timeOpen") String timeOpen
			, @Nullable @Named("timeClose") String timeClose
			,User user
			) throws OAuthRequestException, IOException{
		log.info("Retailer sent to be added by sales user: " + user);
		if(null == user) {
			log.severe("user.retailer.createByProps: User not present: ");
//			return null;
		}

		Retailer r = new Retailer();
		r.setName(name);
		r.setLatLng((float)lat, (float)lng);
		r.setAddedByEmail(agentId);
		r.setDetails(details);
		r.setPhoneCsv(phoneCsv);
		r.setEmailCsv(emailCsv);
		r.setAddress(address);
		r.setLocality(locality);
		r.setCategoryTags(categoryCsv);
		r.setSubCategoryTags(subCategoryCsv);
		r.setBrandTags(brandCsv);
		r.setContactName(contactName);
		r.setContactPhoneCsv(contactPhoneCsv);
		r.setKeywordTags(keywordCsv);
		r.setPaymentModeCsv(paymentModeCsv);
		r.setProductTags(productCsv);
		r.setServiceTypeTags(serviceTypeCsv);
		r.setRetailerPicturesCsv(pictureUrlCsv);
		r.setPartner(isPartner);
		r.setCrowdSourced(false);
		r.setApproved(true);
		try {
			r.setCountry(Country.valueOf(country));
		} catch (Exception e) {
			log.log(Level.WARNING, "Country not found: " + country, e);//ignore
		}
		r.setJoiningDate(joiningDate);
		r.setOwner(owner);
		r.setSpecialOfferings(specialOfferings);
		if(rating != null)
			r.setEditorRating(rating.floatValue());
		r.setSmsCsv(smsNumberCsv);
		r.setWebsiteUrl(website);
		r.setZip(zip);
		r.setAddedBy(user);
		if(user != null) {
			r.setAddedByEmail(user.getEmail());
		} else {
			r.setAddedByEmail("sales@locoserv.in");//TODO remove this and throw access error after login integration
		}
		
		//r.setDaysOfWeek(daysOfWeek);//TODO provide half days
		//r.setTimeOpen(timeOpen);
		//r.setTimeClose(timeClose);
		log.info("creating retailer: " + r);
		Key<Retailer> key = this.retailerOfy.saveEntity(r);//TODO Handle Exception
		if(key != null) {
			log.info("created retailer ID: " + key.getId());
			r.setId(key.getId());
			String status = retailerIndexer.pushToIndex(r);//TODO Handle Exception
			log.info("status: " + status);
		} else {
			log.severe("Key not found for retailer: " + r);
			return null;
		}
		log.info("returning Retailer: " + r);
		return r;
	}


	/* Non API methods */
	@Inject
	public void setRetailerSearch(RetailerSearch retailerSearch) {
		this.retailerSearch = retailerSearch;
	}
	
	@Inject
	public void setRetailerIndexer(RetailerIndexer retailerIndexer) {
		this.retailerIndexer = retailerIndexer;
	}
	
	@Inject
	public void setCustomerOfy(CustomerOfy customerOfy) {
		this.customerOfy = customerOfy;
	}

	@Inject
	public void setBookmarkOfy(BookmarkOfy bookmarkOfy) {
		this.bookmarkOfy = bookmarkOfy;
	}

}
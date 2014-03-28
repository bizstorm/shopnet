package com.jmd.shopnet.dao;

import java.util.logging.Logger;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.google.inject.Injector;
import com.googlecode.objectify.ObjectifyFactory;
import com.jmd.shopnet.entity.Customer;
import com.jmd.shopnet.entity.Product;
import com.jmd.shopnet.entity.ProductOffer;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.Bookmark;



@Singleton
public class OfyFactory extends ObjectifyFactory
{
	protected static Logger log = Logger.getLogger(OfyFactory.class.getName());

	@Inject private static Injector injector;

	/** Register the entity types */
	public OfyFactory() {
		long time = System.currentTimeMillis();

		this.register(Business.class);
		this.register(Customer.class);
		this.register(Product.class);
		this.register(ProductOffer.class);
		this.register(Bookmark.class);


		long millis = System.currentTimeMillis() - time;
		log.info("Registration took " + millis + " millis");
	}

	/** Guice to make instances */
	@Override
	public <T> T construct(Class<T> type) {
		return injector.getInstance(type);
	}

	@Override
	public Ofy begin() {
		return new Ofy(this);
	}
}
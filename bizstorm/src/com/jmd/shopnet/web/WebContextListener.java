package com.jmd.shopnet.web;

import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Singleton;
import javax.servlet.ServletContextEvent;

import com.google.api.server.spi.guice.GuiceSystemServiceServletModule;
import com.google.appengine.tools.appstats.AppstatsFilter;
import com.google.appengine.tools.appstats.AppstatsServlet;
import com.google.common.collect.Maps;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.name.Names;
import com.google.inject.servlet.GuiceServletContextListener;
import com.googlecode.objectify.ObjectifyFilter;
import com.jmd.shopnet.api.CustomerAPI;
import com.jmd.shopnet.dao.OfyFactory;

public class WebContextListener extends GuiceServletContextListener {
	Logger log = Logger.getLogger(WebContextListener.class.getName());

	static class ServletModule extends GuiceSystemServiceServletModule
	{
		@Override
		protected void configureServlets() {
		    super.configureServlets();

		    Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		    serviceClasses.add(CustomerAPI.class);
		    this.serveGuiceSystemServiceServlet("/_ah/spi/*", serviceClasses);
		  
			Map<String, String> appstatsParams = Maps.newHashMap();
			//appstatsParams.put("logMessage", "Appstats: /admin/appstats/details?time={ID}");
			appstatsParams.put("calculateRpcCosts", "true");
			filter("/*").through(AppstatsFilter.class, appstatsParams);
			serve("/appstats/*").with(AppstatsServlet.class);
			serve("/smscin").with(SmscRecieverServlet.class);
			serve("/location").with(SmscRecieverServlet.class);

			filter("/*").through(ObjectifyFilter.class);
		}
	}

	/** Public so it can be used by unit tests */
	public static class InjectionModule extends AbstractModule {
		Logger logger = Logger.getLogger(InjectionModule.class.getName());

		@Override
		protected void configure() {
			try {
				Properties props = loadProperties("config.properties");
				Names.bindProperties(binder(), props);
			} catch (Exception e) {
				logger.logp(Level.SEVERE, InjectionModule.class.getName(), "configure", "Failed to load properties. ", e);
			}
			requestStaticInjection(OfyFactory.class);

			// Lets us use @Transact
			//bindInterceptor(Matchers.any(), Matchers.annotatedWith(Transact.class), new TransactInterceptor());

			/* Use jackson for JSON jaxrs */
			bind(ObjectMapperProvider.class);

			// External things that don't have Guice annotations
			bind(AppstatsFilter.class).in(Singleton.class);
			bind(AppstatsServlet.class).in(Singleton.class);
			bind(ObjectifyFilter.class).in(Singleton.class);
			bind(SmscRecieverServlet.class).in(Singleton.class);

			/* Jersey controller binding with @Path */
			bind(Retailers.class);
			//bind(SignIn.class);
			//bind(TxnTest.class);

			//bind(RetailerFormatter.class).to(RetailerFormatter1.class);
			//bind(RetailerConverter.class).to(CsvRetailersGaur1Converter.class);
			//bind(RetailEventConverter.class).to(CsvRetailEventsGaur1Converter.class);
		}
	}

	/**
	 * Logs the time required to initialize Guice
	 */
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		long time = System.currentTimeMillis();

		super.contextInitialized(servletContextEvent);

		long millis = System.currentTimeMillis() - time;
		log.info("Guice initialization took " + millis + " millis");
	}

	@Override
	protected Injector getInjector() {
		return Guice.createInjector(new ServletModule(), new InjectionModule());
	}

	private static Properties loadProperties(String filePath) throws Exception {
		Properties properties = new Properties();
		ClassLoader loader = InjectionModule.class.getClassLoader();
		URL url = loader.getResource(filePath);
		properties.load(url.openStream());
		return properties;
	}
}

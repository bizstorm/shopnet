package com.jmd.shopnet.partners;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class SmscSender {
	
	private static Logger log = Logger.getLogger(SmscSender.class.getName());

	@Inject
	public void setUsername(@Named("smsc.send.usr") String username) {
		this.username = username;
	}

	@Inject
	public void setPassword(@Named("smsc.send.pwd") String password) {
		this.password = password;
	}

	@Inject
	public void setServiceUrl(@Named("smsc.send.url") String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	@Inject
	public void setSenderId(@Named("smsc.send.sid") String senderId) {
		this.senderId = senderId;
	}

	private String username;
	private String password;
	private String senderId;
	private String serviceUrl;
	private String MESSAGE_TYPE = "N";
	private String DELIVERY_REPORT_REQUIRED = "Y";
	
	
	public String send(String smsNumber, String message) {
		log.info("serviceUrl: " + serviceUrl);
		log.info("message: " + message);
		StringBuffer postData = new StringBuffer();
		String postDataEnc;
		try {
			postData.append("User=").append(URLEncoder.encode(username, "UTF-8"))
					.append("&passwd=").append(URLEncoder.encode(password, "UTF-8")) 
					.append("&mobilenumber=").append(smsNumber)
					.append("&message=").append(URLEncoder.encode(message, "UTF-8")) 
					.append("&sid=").append(senderId)
					.append("&mtype=").append(MESSAGE_TYPE)
					.append("&DR=").append(DELIVERY_REPORT_REQUIRED);
			if(log.isLoggable(Level.FINE))
				log.fine("postData: " + postData);
			postDataEnc = postData.toString();
		} catch (UnsupportedEncodingException e) {
			log.logp(Level.SEVERE, SmscSender.class.getName(), "send", "URL Encoding failed for sending SMS. ", e);
			//throw new RuntimeException("URL Encoding failed for sending SMS. ", e);
			return null;//TODO throw custom exception and handle it
		}
		if(log.isLoggable(Level.FINE))
			log.fine("postDataEnc: " + postDataEnc);
		
		HttpURLConnection urlconnection;
		try {
			URL url = new URL(this.serviceUrl);
			urlconnection = (HttpURLConnection) url.openConnection();
			urlconnection.setRequestMethod("POST");
			urlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			urlconnection.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(urlconnection.getOutputStream());
			out.write(postDataEnc);
			out.close();
		} catch (Exception e) {
			log.logp(Level.SEVERE, SmscSender.class.getName(), "send", "Failed to send SMS. ", e);
			//throw new RuntimeException("Failed to send SMS. " + e.getMessage(), e);
			return null;//TODO throw custom exception and handle it
		}

		if(log.isLoggable(Level.INFO))
			log.info("Wrote postData: " + postDataEnc);
		StringBuffer retval;
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
			String decodedString;
			retval = new StringBuffer();
			while ((decodedString = in.readLine()) != null) {
				retval.append(decodedString);
			}
			in.close();
			String retStr = retval.toString();
			if(log.isLoggable(Level.INFO))
				log.info("return code from SMS: " + retStr);
			return retStr;
		} catch (IOException e) {
			log.logp(Level.WARNING, SmscSender.class.getName(), "send", "Failed to parse SMS response. ", e);
			return null;
		}
	}
}
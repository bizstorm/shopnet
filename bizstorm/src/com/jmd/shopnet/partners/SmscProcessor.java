package com.jmd.shopnet.partners;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.jmd.shopnet.dao.BusinessDAO;
import com.jmd.shopnet.entity.Business;
import com.jmd.shopnet.entity.Country;
import com.jmd.shopnet.entity.SmsRequest;
import com.jmd.shopnet.utils.StringConversions;
import com.jmd.shopnet.web.SmscRecieverServlet;

public class SmscProcessor {
	public static final int COMMON_PHONE_MAX_RETAILERS_WARN = 5;
	public static final String SMS_SYNTAX = "scan event <start_date_DD.MM.YY> <end_date_DD.MM.YY> <title_text>";

	protected Logger log = Logger.getLogger(SmscProcessor.class.getName());

	//public static final SimpleDateFormat SMSC_REQ_FORMAT = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
	public static final String SMSC_STS_KEYWORD = "STS";
	public static final String SMSC_RECV_PARAM_DATETIME = "receivedon";
	public static final String SMSC_RECV_PARAM_MESSAGE = "message";
	public static final String SMSC_RECV_PARAM_MOBILE = "mobilenumber";
	
	public static final String SMSC_SUCCESS_SMS_PART1 = "Check out the discounts. ";
	public static final String SMSC_SUCCESS_SMS_PART2 = " at ";
	public static final String SMSC_SUCCESS_SMS_PART3 = " at http://www.jmd.shopnet.com/#/events/";
	public static final String SMSC_SUCCESS_SMS_PART4 = "/";
	
	public static final String SMSC_ERROR_SMS_PART1 = "Your offer from ";
	public static final String SMSC_ERROR_SMS_PART2 = " to ";
	public static final String SMSC_ERROR_SMS_PART3 = " has ";
	public static final String SMSC_ERROR_SMS_PART4 = ". Please correct and re-send or contact customer care at ";

	private BusinessDAO retailerOfy;
	private SmscSender smscSender;
	private String supportPhone;

	public int processSmscReqParamMap(@SuppressWarnings("rawtypes") Map reqParamMap) {
		SmsRequest sms = new SmsRequest();
		try {
			if (reqParamMap.get(SMSC_RECV_PARAM_DATETIME) != null) {
				String dateStr = ((String[]) reqParamMap.get(SMSC_RECV_PARAM_DATETIME))[0];
				Date date = StringConversions.parseDateTimeAllFormats(dateStr);
				sms.setRecieveTime(date);
			}
		} catch (Exception e) {
			// log.warning("Ignoring failed parse of date-time. " + e.getMessage());
			// return SmscRecieverServlet.RESULT_PARTNER_BAD_PROVIDER_FORMAT;
			log.logp(Level.WARNING, SmscRecieverServlet.class.getName(), "processReqParamMap",
					"Ignoring failed parse of date-time. " + e.getMessage(), e);
		}
		if (reqParamMap.get(SMSC_RECV_PARAM_MESSAGE) != null) {
			String message = ((String[]) reqParamMap.get(SMSC_RECV_PARAM_MESSAGE))[0].trim();
			if (!message.isEmpty()) {
				sms.setFullMessage(message);
			} else {
				log.severe("SMS message param empty.");
				return SmscRecieverServlet.RESULT_PARTNER_BAD_REQUEST;
			}
		} else {
			log.severe("Missing SMS message param.");
			return SmscRecieverServlet.RESULT_PARTNER_BAD_REQUEST;
		}
		if (reqParamMap.get(SMSC_RECV_PARAM_MOBILE) != null) {
			String phoneNumber = ((String[]) reqParamMap.get(SMSC_RECV_PARAM_MOBILE))[0];
			if (!phoneNumber.trim().isEmpty()) {
				String smsNumber = StringConversions.deriveComparableSmsNumber(phoneNumber, Country.India);
				if(smsNumber == null){
					log.severe("Invalid SMS mobilenumber format.");
					return SmscRecieverServlet.RESULT_PARTNER_BAD_PROVIDER_FORMAT;
				}
				sms.setSmsNumber(smsNumber);
			} else {
				log.severe("SMS mobilenumber param empty.");
				return SmscRecieverServlet.RESULT_PARTNER_BAD_PROVIDER_FORMAT;
			}
		} else {
			log.severe("Missing SMS mobilenumber param.");
			return SmscRecieverServlet.RESULT_PARTNER_BAD_PROVIDER_FORMAT;
		}
		if(sms == null || sms.getSmsNumber() == null) {
			log.severe("No mobilenumber found to compare in SMS: " + sms);
			return SmscRecieverServlet.RESULT_PARTNER_BAD_PROVIDER_FORMAT;
		}
		return processMessage(sms);
	}

	private int processMessage(SmsRequest sms) {
		String message = sms.getFullMessage();
		if(log.isLoggable(Level.FINE)) 
			log.fine("processMessage sms: " + message);

		String errorMesgShort = null;
		String startDateStr = null;
		String endDateStr = null;
		String title = null;
		int returnCode = 0;

		List<Business> retailers = null;
		try {
			retailers = findRetailersBySmsNumber(sms.getSmsNumber());
		} catch (RuntimeException e) {
			log.logp(Level.SEVERE, SmscRecieverServlet.class.getName(), "processReqParamMap", "Error finding Retailer for the given mobilenumber. " + e.getMessage(), e);
			errorMesgShort = " failed for: " + sms.getSmsNumber();
			//smscSender.send(sms.getSmsNumber(), "No Start date to add event.");//TODO given format
			returnCode = SmscRecieverServlet.RESULT_PARTNER_AUTH_FAIL;
		}
		if(retailers == null || retailers.isEmpty()) {
			log.severe("No Retailer found for the given mobilenumber. " + sms.getSmsNumber());
			errorMesgShort = "no shop registered for: " + sms.getSmsNumber();
			returnCode = SmscRecieverServlet.RESULT_PARTNER_AUTH_FAIL;
		}
		if(retailers.size() > COMMON_PHONE_MAX_RETAILERS_WARN) {
			log.warning("Too many retailers: " + retailers.size());
		}
		if(message != null && !message.isEmpty()) {
			StringTokenizer tokens = new StringTokenizer(message);
			String sts_key = tokens.nextToken();
			if(!sts_key.equalsIgnoreCase("scan")) {
				log.info("Strange sts_key: " + sts_key);
			}
			
			String act = tokens.nextToken();
			if(act.equalsIgnoreCase("event") || act.equalsIgnoreCase("evt")) {//TODO handle default on no start word
				if(!tokens.hasMoreTokens()) {
					log.severe("No Start date to add event.");
					errorMesgShort = "no Start date";
					//smscSender.send(sms.getSmsNumber(), "No Start date to add event.");//TODO given format
					returnCode = SmscRecieverServlet.RESULT_PARTNER_INVALID_EVENT_DATES;
				} else {
					startDateStr = tokens.nextToken();
					if(!tokens.hasMoreTokens()) {
						log.severe("No End date to add event.");
						errorMesgShort = "no End date";
						//smscSender.send(sms.getSmsNumber(), "No End date to add event.");//TODO given format
						returnCode = SmscRecieverServlet.RESULT_PARTNER_INVALID_EVENT_DATES;
					} else {
						endDateStr = tokens.nextToken();
						if(!tokens.hasMoreTokens()) {
							log.severe("No Title to add event");
							errorMesgShort = "no Title";
							//smscSender.send(sms.getSmsNumber(), "No Title to add event");//TODO given format
							returnCode = SmscRecieverServlet.RESULT_PARTNER_NO_TITLE;
						} else {
							try {
								Date startDate = StringConversions.parseDateAllFormats(startDateStr);
								Date endDate = StringConversions.parseDateAllFormats(endDateStr);
								title = message.substring(message.indexOf(endDateStr) + endDateStr.length() + 1);//TODO handle same start end dates
								for (Business retailer : retailers) {
									
								}
								
								//smscSender.send(sms.getSmsNumber(), "New event added on SMS request. From: " + sms.getSmsNumber() + ". Start: " + startDateStr + ". End: " + endDateStr + ". Title: " + title); //TODO send on datastore notification
								returnCode = SmscRecieverServlet.RESULT_PARTNER_MOD_SUCCESSFUL;
							} catch (RuntimeException e) {
								log.logp(Level.SEVERE, SmscProcessor.class.getName(), "processMessage", "Invalid Event Dates or Title. Start: " + startDateStr + ". End: " + endDateStr + ". Message: " + message, e);
								errorMesgShort = "invalid Start-date: " + startDateStr + " / End-Date: " + endDateStr;
								//smscSender.send(sms.getSmsNumber(), "Bad date format in SMS. Please use format: " + SMS_SYNTAX);//TODO given format
								returnCode = SmscRecieverServlet.RESULT_PARTNER_INVALID_EVENT_DATES;
							}
						}
					}
				}
			} else {
				log.severe("Bad SMS format. Missing action(like 'event').");
				errorMesgShort = "Missing action(e.g. event)";
				//smscSender.send(sms.getSmsNumber(), "Bad SMS format. Missing action(like 'event'). Please use format: " + SMS_SYNTAX);//TODO given format
				//smscSender.send(sms.getSmsNumber(), "Bad SMS format. Please use format: " + SMS_SYNTAX);//TODO given format
				return SmscRecieverServlet.RESULT_PARTNER_BAD_REQUEST;
			}
		} else {
			//smscSender.send(sms.getSmsNumber(), "Bad SMS format. Please use format: " + SMS_SYNTAX);//TODO given format
			errorMesgShort = "no Message";
			return SmscRecieverServlet.RESULT_PARTNER_BAD_REQUEST;
		}
		if(errorMesgShort == null) {
			StringBuilder successSms = new StringBuilder(SMSC_SUCCESS_SMS_PART1);
			
			smscSender.send(sms.getSmsNumber(), successSms.toString());
			//smscSender.send(sms.getSmsNumber(), "Check out the discounts. " + event.getTitle() + " at " + event.getRetailer().getName() + " at http://www.jmd.shopnet.com/#/events/" + event.getRetailer().getId() + "/" + event.getId());
		} else {
			StringBuilder errorSms = new StringBuilder(SMSC_ERROR_SMS_PART1);
			errorSms.append(startDateStr).append(SMSC_ERROR_SMS_PART2).append(endDateStr)
				.append(SMSC_ERROR_SMS_PART3).append(errorMesgShort).append(SMSC_ERROR_SMS_PART4).append(this.supportPhone);
			smscSender.send(sms.getSmsNumber(), errorSms.toString());
		}
		return returnCode;
	}

	private List<Business> findRetailersBySmsNumber(String smsNumber) {
		if(smsNumber == null || smsNumber.isEmpty())
			return null;
		List<Business> retailers = retailerOfy.getBySmsNumbers(smsNumber);
		if(retailers == null || retailers.isEmpty()) {
			retailers = retailerOfy.getByPhoneNumbers(smsNumber);
			if(retailers == null || retailers.isEmpty()) {
				return null;
			}
		}
		return retailers;
	}

/*	private Retailer findRetailerByPhone(PhoneNum phoneNum) {
		if(phoneNum == null || phoneNum.getRawNumber() == null || phoneNum.getRawNumber().isEmpty())
			return null;
		List<Retailer> retailers = retailerOfy.fetchBySmsNumbers(phoneNum.getRawNumber());
		if(retailers == null || retailers.isEmpty())
			return null;
		if(retailers.size() > 1) {
			throw new RuntimeException("Multiple retailers found");//TODO handle for multiple countries
		} else
			return retailers.get(0);
	}*/

	public String getSupportPhone() {
		return supportPhone;
	}

	@Inject
	public void setSupportPhone(@Named("support.phone") String supportPhone) {
		this.supportPhone = supportPhone;
	}

	public BusinessDAO getRetailerOfy() {
		return retailerOfy;
	}

	@Inject
	public void setRetailerOfy(BusinessDAO retailerOfy) {
		this.retailerOfy = retailerOfy;
	}



	public SmscSender getSmscSender() {
		return smscSender;
	}

	@Inject
	public void setSmscSender(SmscSender smscSender) {
		this.smscSender = smscSender;
	}



}

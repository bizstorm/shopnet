package com.jmd.shopnet.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Link;
import com.google.appengine.api.datastore.PhoneNumber;
import com.jmd.shopnet.entity.Country;
import com.jmd.shopnet.entity.PaymentMode;

public class StringConversions {
	
	public static final SimpleDateFormat DATE_FORMAT0 = new SimpleDateFormat("d.M.yy");
	public static final SimpleDateFormat DATE_FORMAT1 = new SimpleDateFormat("dd.MM.yy");
	public static final SimpleDateFormat DATE_FORMAT2 = new SimpleDateFormat("dd.MM.yyyy");
	public static final SimpleDateFormat DATE_FORMAT3 = new SimpleDateFormat("dd/MM/yy");
	public static final SimpleDateFormat DATE_FORMAT4 = new SimpleDateFormat("dd/MM/yyyy");
	public static final SimpleDateFormat DATE_FORMAT5 = new SimpleDateFormat("d/M/yy");
	public static final SimpleDateFormat DATE_FORMAT6 = new SimpleDateFormat("d/M/yyyy");
	public static final SimpleDateFormat DATE_FORMAT7 = new SimpleDateFormat("yyyy-MM-dd");//ISO-8601 date format

	public static final SimpleDateFormat DATETIME_FORMAT0 = new SimpleDateFormat("M/d/yyyy");
	public static final SimpleDateFormat DATETIME_FORMAT1 = new SimpleDateFormat("M/d/yy");
	public static final SimpleDateFormat DATETIME_FORMAT2 = new SimpleDateFormat("M/d/yyyy hh:mm:ss a");
	public static final SimpleDateFormat DATETIME_FORMAT3 = new SimpleDateFormat("M/d/yy hh:mm:ss a");
	public static final SimpleDateFormat DATETIME_FORMAT4 = new SimpleDateFormat("M/d/yyyy hh:mm:ss");
	public static final SimpleDateFormat DATETIME_FORMAT5 = new SimpleDateFormat("MM/dd/yy hh:mm:ss a");
	public static final SimpleDateFormat DATETIME_FORMAT6 = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");

	public static String joinCatsWithSpace(List<Category> cats) {
		StringBuilder joined = new StringBuilder();
		for (Category c : cats) {
			if(c != null){
				if(joined.length() != 0)
					joined.append(" ");
				joined.append(c.getCategory());
			}
		}
		return joined.toString();
	}
	/**
	 * Throws java.lang.NullPointerException if list contains null element. 
	 */
	public static String formCsvFromCategories(List<Category> cats) {
		if(cats == null || cats.isEmpty())
			return null;
		StringBuilder csv = new StringBuilder();
		csv.append(cats.get(0).getCategory());
		for (int i = 1; i < cats.size(); i++) {
			csv.append(",").append(cats.get(i).getCategory());
		}
		return csv.toString();
	}

	/**
	 * Split by comma and make lower-case before making 
	 */
	public static List<Category> extractCategoriesFromCsv(String categoryCsv) {
		if(categoryCsv == null || categoryCsv.isEmpty())
			return null;
		else {
			List<Category> list = new ArrayList<Category>();
			if(!categoryCsv.contains(",")) {
				list.add(new Category(categoryCsv));
				return list;
			}
			String[] cats = categoryCsv.toLowerCase().split(",");
			for (int i = 0; i < cats.length; i++)
				if(!cats[i].trim().isEmpty())
					list.add(new Category(cats[i].trim()));
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	
	/**
	 * Split by comma and make lower-case before making 
	 */
	public static List<Category> extractCategoriesFromList(List<String> categories) {
		if(categories == null || categories.size()== 0)
			return null;
		else {
			List<Category> list = new ArrayList<Category>();
			for (String category : categories) {
				if(!category.trim().isEmpty())
					list.add(new Category(category.trim()));
			}			
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	public static List<PhoneNumber> extractPhonesFromCsv(String phone) {
		if(phone == null || phone.isEmpty())
			return  null;
		else {
			List<PhoneNumber> list = new ArrayList<PhoneNumber>();
			if(!phone.contains(",")) {
				list.add(new PhoneNumber(phone));
				return list;
			}
			String[] phones = phone.split(",");
			for (int i = 0; i < phones.length; i++)
				if(!phones[i].trim().isEmpty())
					list.add(new PhoneNumber(phones[i].trim()));
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	public static List<PhoneNumber> formCompleteSmsNumbersFromCsv(String phone, Country country) {
		if(phone == null || phone.isEmpty())
			return  null;
		else {
			List<PhoneNumber> list = new ArrayList<PhoneNumber>();
			if(!phone.contains(",")) {
				String numeric = deriveComparableSmsNumber(phone, country);
				if(numeric == null)
					return null;
				list.add(new PhoneNumber(numeric));
				return list;
			}
			String[] phones = phone.split(",");
			for (int i = 0; i < phones.length; i++) {
				String numeric = deriveComparableSmsNumber(phones[i], country);
				if(numeric != null && !numeric.isEmpty())
					list.add(new PhoneNumber(numeric));
			}
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	public static List<PhoneNumber> extractNumericPhonesFromCsv(String csv, Country country) {
		if(csv == null || csv.isEmpty())
			return  null;
		else {
			List<PhoneNumber> list = new ArrayList<PhoneNumber>();
			if(!csv.contains(",")) {
				String numeric = deriveComparableSmsNumber(csv, country);
				if(numeric == null) {
					return null;
				}
				list.add(new PhoneNumber(numeric));
				return list;
			}
			String[] split = csv.split(",");
			for (int i = 0; i < split.length; i++) {
				String numeric = deriveComparableSmsNumber(split[i], country);
				if(numeric != null && !numeric.isEmpty())
					list.add(new PhoneNumber(numeric));
			}
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}


	public static List<String> extractNumericsFromCsv(String csv) {
		if(csv == null || csv.isEmpty())
			return  null;
		else {
			List<String> list = new ArrayList<String>();
			if(!csv.contains(",")) {
				list.add(csv);
				return list;
			}
			String[] split = csv.split(",");
			for (int i = 0; i < split.length; i++) {
				String numeric = extractNumerics(split[i]);
				if(numeric != null && !numeric.isEmpty())
					list.add(numeric);
			}
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	public static List<String> extractStringsFromCsv(String csv) {
		if(csv == null || csv.isEmpty())
			return  null;
		else {
			List<String> list = new ArrayList<String>();
			if(!csv.contains(",")) {
				list.add(csv);
				return list;
			}
			String[] split = csv.split(",");
			for (int i = 0; i < split.length; i++)
				if(!split[i].trim().isEmpty())
					list.add(split[i].trim());
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	/**
	 * Throws java.lang.NullPointerException if list contains null element. 
	 */
	public static String formCsvFromPhones(List<PhoneNumber> phones) {
		if(phones == null || phones.isEmpty())
			return null;
		else {
			StringBuilder csv = new StringBuilder();
			csv.append(phones.get(0).getNumber());
			for (int i = 1; i < phones.size(); i++) {
				csv.append(",")
					.append(phones.get(i).getNumber());
			}
			return csv.toString();
		}
	}

	public static List<Email> extractEmailsFromCsv(String csv) {
		if(csv == null || csv.isEmpty())
			return  null;
		else {
			List<Email> list = new ArrayList<Email>();
			if(!csv.contains(",")) {
				list.add(new Email(csv));
				return list;
			}
			String[] split = csv.split(",");
			for (int i = 0; i < split.length; i++)
				if(!split[i].trim().isEmpty())
					list.add(new Email(split[i].trim()));
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	/**
	 * Throws java.lang.NullPointerException if list contains null element. 
	 */
	public static String formCsvFromEmails(List<Email> list) {
		if(list == null || list.isEmpty())
			return null;
		else {
			StringBuilder csv = new StringBuilder();
			csv.append(list.get(0).getEmail());
			for (int i = 1; i < list.size(); i++) {
				csv.append(",").append(list.get(i).getEmail());
			}
			return csv.toString();
		}
	}

	public static List<Link> extractLinksFromCsv(String csv) {
		if(csv == null || csv.isEmpty())
			return  null;
		else {
			List<Link> list = new ArrayList<Link>();
			if(!csv.contains(",")) {
				list.add(new Link(csv.trim()));
				return list;
			}
			String[] split = csv.split(",");
			for (int i = 0; i < split.length; i++)
				if(!split[i].trim().isEmpty())
					list.add(new Link(split[i].trim()));
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	/**
	 * Throws java.lang.NullPointerException if list contains null element. 
	 */
	public static String formCsvFromLinks(List<Link> list) {
		if(list == null || list.isEmpty())
			return null;
		else {
			StringBuilder csv = new StringBuilder();
			csv.append(list.get(0).getValue());
			for (int i = 1; i < list.size(); i++) {
				csv.append(",").append(list.get(i).getValue());
			}
			return csv.toString();
		}
	}

	public static List<PaymentMode> extractPaymentModesFromCsv(String csv) {
		if(csv == null || csv.isEmpty())
			return  null;
		else {
			List<PaymentMode> list = new ArrayList<PaymentMode>();
			if(!csv.contains(",")) {
				list.add(PaymentMode.getByText(csv.trim()));
				return list;
			}
			String[] split = csv.split(",");
			for (int i = 0; i < split.length; i++)
				if(!split[i].trim().isEmpty())
					list.add(PaymentMode.getByText(split[i].trim()));
			if(!list.isEmpty())
				return list;
			else 
				return null;
		}
	}

	/**
	 * Throws java.lang.NullPointerException if list contains null element. 
	 */
	public static String formCsvFromPaymentModes(List<PaymentMode> list) {
		if(list == null || list.isEmpty())
			return null;
		else {
			StringBuilder csv = new StringBuilder();
			csv.append(list.get(0).toString());
			for (int i = 1; i < list.size(); i++) {
				csv.append(",").append(list.get(i).toString());
			}
			return csv.toString();
		}
	}

	public static String deriveComparableSmsNumber(String smsNumber, Country country) {
		smsNumber = extractNumerics(smsNumber);
		Integer mobileLength = JMDConstants.MOBILE_NUMBER_LENGTHS.get(country);
		String countryCode = JMDConstants.COUNTRY_CODES.get(country);
		if(mobileLength == null)
			mobileLength = JMDConstants.MOBILE_NUMBER_LENGTHS.get(Country.India);//TODO remove Assumption of India
		if(countryCode == null)
			countryCode = JMDConstants.COUNTRY_CODES.get(Country.India);//TODO remove Assumption of India
		if(smsNumber.isEmpty() || smsNumber.length() < mobileLength) {
			return null;
		} else if(smsNumber.length() == (mobileLength + countryCode.length())
				&& smsNumber.startsWith(countryCode)) {
			return smsNumber;
		} else {
			smsNumber = smsNumber.substring(smsNumber.length() - mobileLength);
			return countryCode + smsNumber;
		}
	}
	public static String extractNumerics(String smsNumber) {
		return smsNumber.replaceAll( "[^\\d]", "" );
	}

	public static List<String> deriveComparableSmsNumbers(List<PhoneNumber> smsNumbers, Country country) {
		if(smsNumbers == null)
			return null;
		if(smsNumbers.isEmpty())
			return Collections.<String>emptyList();
		List<String> comparableSmsNumbers = new ArrayList<String>();
		for (PhoneNumber phone : smsNumbers) {
			String numeric = deriveComparableSmsNumber(phone.getNumber(), country);
			if(numeric != null)
				comparableSmsNumbers.add(numeric);
		}
		return comparableSmsNumbers;
	}

	public static Link formLink(String url) {
		if (invalidUrl(url)) {
			return null;
		}
		if(!url.contains("://")) {
			return new Link("http://" + url);
		} else 
			return new Link(url);
	}

	public static boolean invalidUrl(String url) {
		return url == null || url.isEmpty() 
				|| !url.contains(".");
	}

	public static Date parseDateAllFormats(String dateStr) {
		Date date;
		try {
			date = DATE_FORMAT0.parse(dateStr);
		} catch (ParseException e0) {
			try {
				date = DATE_FORMAT1.parse(dateStr);
			} catch (ParseException e1) {
				try {
					date = DATE_FORMAT2.parse(dateStr);
				} catch (ParseException e2) {
					try {
						date = DATE_FORMAT3.parse(dateStr);
					} catch (ParseException e3) {
						try {
							date = DATE_FORMAT4.parse(dateStr);
						} catch (ParseException e4) {
							try {
								date = DATE_FORMAT5.parse(dateStr);
							} catch (ParseException e5) {
								try {
									date = DATE_FORMAT6.parse(dateStr);
								} catch (ParseException e6) {
									try {
										date = DATE_FORMAT7.parse(dateStr);
									} catch (ParseException e7) {
										throw new RuntimeException("Failed to parse date " + dateStr);
									}
								}
							}
						}
					}
				}
			}
		}
		return date;
	}

	public static Date parseDateTimeAllFormats(String dateTimeStr) {
		Date date;
		try {
			date = DATETIME_FORMAT0.parse(dateTimeStr);
		} catch (ParseException e0) {
			try {
				date = DATETIME_FORMAT1.parse(dateTimeStr);
			} catch (ParseException e1) {
				try {
					date = DATETIME_FORMAT2.parse(dateTimeStr);
				} catch (ParseException e2) {
					try {
						date = DATETIME_FORMAT3.parse(dateTimeStr);
					} catch (ParseException e3) {
						try {
							date = DATETIME_FORMAT4.parse(dateTimeStr);
						} catch (ParseException e4) {
							try {
								date = DATETIME_FORMAT5.parse(dateTimeStr);
							} catch (ParseException e5) {
								try {
									date = DATETIME_FORMAT6.parse(dateTimeStr);
								} catch (ParseException e6) {
									throw new RuntimeException("Failed to parse date " + dateTimeStr);
								}
							}
						}
					}
				}
			}
		}
		return date;
	}
}

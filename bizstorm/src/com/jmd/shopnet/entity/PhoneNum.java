package com.jmd.shopnet.entity;

import com.jmd.shopnet.utils.JMDConstants;
import com.jmd.shopnet.utils.StringConversions;

public class PhoneNum {

	private Country country = Country.India;

	/** excludes country-code (includes city-code if applicable) and has only numeric characters */
	private String rawNumber;
	private String numericWithCountryCode;

	public PhoneNum(Country country, String phoneNumber) {
		this.country = country;
		setPhoneNumber(phoneNumber);
	}

	public PhoneNum() {
		country = Country.India;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
		markDirtyNumeric();
	}

	public String getRawNumber() {
		return rawNumber;
	}

	public String getNumericWithCountryCode() {
		if(isDirtyNumeric())
			numericWithCountryCode = JMDConstants.COUNTRY_CODES.get(country) + rawNumber;
		return numericWithCountryCode;
	}

	public boolean isDirtyNumeric() {
		return numericWithCountryCode == null;
	}

	public void markDirtyNumeric() {
		numericWithCountryCode = null;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.rawNumber = StringConversions.deriveComparableSmsNumber(phoneNumber, this.country);
		markDirtyNumeric();
	}

	@Override
	public String toString() {
		return "PhoneNum [country=" + country + ", rawNumber=" + rawNumber + "]";
	}
}

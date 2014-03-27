package com.jmd.shopnet.entity;

import java.util.Date;


public class SmsRequest {

	private String smsNumber;
	private Date recieveTime;
	private String fullMessage;
	private PartnerAction action;

	public String getSmsNumber() {
		return smsNumber;
	}

	public void setSmsNumber(String smsNumber) {
		this.smsNumber = smsNumber;
	}

	public PartnerAction getAction() {
		return action;
	}

	public void setAction(PartnerAction action) {
		this.action = action;
	}

	public Date getRecieveTime() {
		return recieveTime;
	}

	public void setRecieveTime(Date recieveTime) {
		this.recieveTime = recieveTime;
	}

	public String getFullMessage() {
		return fullMessage;
	}

	public void setFullMessage(String fullMessage) {
		this.fullMessage = fullMessage;
	}

	@Override
	public String toString() {
		return "SmsRequest [smsNumber=" + smsNumber + ", recieveTime=" + recieveTime + ", fullMessage=" + fullMessage + ", action=" + action + "]";
	}
}

package com.jmd.shopnet.entity;

import java.util.Date;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class SmsRequest {

	@Setter @Getter private String smsNumber;
	private Date recieveTime;
	private String fullMessage;

	
}

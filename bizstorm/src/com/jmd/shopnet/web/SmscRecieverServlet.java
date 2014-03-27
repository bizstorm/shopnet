package com.jmd.shopnet.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.inject.Inject;
import com.jmd.shopnet.partners.SmscProcessor;

public class SmscRecieverServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected Logger log = Logger.getLogger(SmscRecieverServlet.class.getName());

	public static final int RESULT_PARTNER_MOD_SUCCESSFUL = 120;
	public static final int RESULT_PARTNER_BAD_REQUEST = 140;
	public static final int RESULT_PARTNER_BAD_PROVIDER_FORMAT = 141;
	public static final int RESULT_PARTNER_EXPIRED = 142;
	public static final int RESULT_PARTNER_AUTH_FAIL = 143;
	public static final int RESULT_PARTNER_NOT_FOUND = 144;
	public static final int RESULT_PARTNER_INVALID_EVENT_DATES = 146;
	public static final int RESULT_PARTNER_NO_TITLE = 148;
	public static final int RESULT_PARTNER_UNKNOWN_ERROR = 150;
	
	private SmscProcessor smscProcessor;

	public void init() throws ServletException {
		// no initialization
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.logp(Level.INFO, SmscRecieverServlet.class.getName(), "doGet", "SMSC request QueryString: " + request.getQueryString());
		int status = smscProcessor.processSmscReqParamMap(request.getParameterMap());
		if (status == RESULT_PARTNER_MOD_SUCCESSFUL)
			response.setStatus(HttpServletResponse.SC_OK);
		else if (status == RESULT_PARTNER_AUTH_FAIL
				|| status == RESULT_PARTNER_EXPIRED
				|| status == RESULT_PARTNER_NOT_FOUND)
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
		else
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}

	public void destroy() {
		// nothing to release
	}

	public SmscProcessor getSmsRequests() {
		return smscProcessor;
	}

	@Inject
	public void setSmsRequests(SmscProcessor smscProcessor) {
		this.smscProcessor = smscProcessor;
	}
}
package com.jmd.shopnet.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jmd.shopnet.utils.SignIn;

@SuppressWarnings("serial")
public class SignInServlet extends HttpServlet {

	private static final Map<String, String> openIdProviders;
	static {
		openIdProviders = new HashMap<String, String>();
		openIdProviders.put("Google", "https://www.google.com/accounts/o8/id");
		openIdProviders.put("Yahoo", "yahoo.com");
		openIdProviders.put("MySpace", "myspace.com");
		openIdProviders.put("AOL", "aol.com");
		openIdProviders.put("MyOpenId.com", "myopenid.com");
	}

	/*
	 * @Override public void doGet(HttpServletRequest req, HttpServletResponse
	 * resp) throws IOException { UserService userService =
	 * UserServiceFactory.getUserService(); User user =
	 * userService.getCurrentUser(); // or req.getUserPrincipal() Set<String>
	 * attributes = new HashSet();
	 * 
	 * resp.setContentType("text/html"); PrintWriter out = resp.getWriter();
	 * 
	 * if (user != null) { out.println("Hello <i>" + user.getNickname() + " " +
	 * req.getRequestURL() + "</i>!"); out.println("[<a href=\"" +
	 * userService.createLogoutURL(req.getRequestURI()) + "\">sign out</a>]"); }
	 * else { out.println("Hello world! Sign in at:" + " " + req.getRequestURL()
	 * + " " ); for (String providerName : openIdProviders.keySet()) { String
	 * providerUrl = openIdProviders.get(providerName); String loginUrl =
	 * userService.createLoginURL(req .getRequestURI(), null, providerUrl,
	 * attributes); out.println("[<a href=\"" + loginUrl + "\">" + providerName
	 * + "</a>] "); } } }
	 */

	/*
	 * This is for exclusive signin page
	 * 
	 * @Override public void doGet(HttpServletRequest req, HttpServletResponse
	 * resp) throws IOException {
	 * 
	 * RetailerSignIn signIn = new RetailerSignIn();
	 * 
	 * signIn.sign(req.getRequestURL().toString());
	 * 
	 * resp.setContentType("text/html"); PrintWriter out = resp.getWriter();
	 * 
	 * if(new Boolean(true).equals(signIn.getSigned())) {
	 * out.println("Hello <i>" + signIn.getName() + " " + req.getRequestURL() +
	 * "</i>!"); out.println("[<a href=\"" + signIn.getLogoutURL() +
	 * "\">sign out</a>]"); } else { Map<String, String> loginAttr =
	 * signIn.getLoginAttributes(); out.println("Hello world! Sign in at:" + " "
	 * + req.getRequestURL() + " " ); for (Map.Entry<String, String> entry :
	 * loginAttr.entrySet()) { out.println("[<a href=\"" + entry.getValue() +
	 * "\">" + entry.getKey() + "</a>] "); } } }
	 */

	@Override
	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

		SignIn signIn = new SignIn();
		String destinationURL = req.getParameter("destinationURL");

		// destinationURL = req.getRequestURL().toString();
		signIn.sign(destinationURL);

		resp.setContentType("text/html");
		resp.setHeader("Cache-Control", "no-cache");
		PrintWriter out = resp.getWriter();

		if (new Boolean(true).equals(signIn.getSigned())) {
			out.print("[<a href=\"" + signIn.getLogoutURL() + "\">Sign out -" + signIn.getName() + "</a>]");
		} else {
			Map<String, String> loginAttr = signIn.getLoginAttributes();
			for (Map.Entry<String, String> entry : loginAttr.entrySet()) {
				out.print("[<a href=\"" + entry.getValue() + "\">" + entry.getKey() + "</a>] ");
			}
		}
	}
}
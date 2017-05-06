package com.authentication;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.commons.configuration.ConfigurationException;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class Authentication {
	protected static Client client;
	protected static WebTarget webTarget;
	private static Object jSessionID;
	protected static Object lwsso_cookie_key;
	protected static Response response;
	protected static MultivaluedMap<String, Object> headers;
	private static String redirectURL;
	
	static{
		client=ClientBuilder.newClient();
		webTarget=client.target(Constants.getBaseurl());
		jSessionID=ConfigurationProperties.getProperty("JSESSIONID");
		lwsso_cookie_key=ConfigurationProperties.getProperty("LWSSO_COOKIE_KEY");
		headers=new MultivaluedHashMap<String, Object>();
		redirectURL="";
	}
	
	/**
	 * Verifies if the user is authenticated using the credentials mentioned in the GlobalSettings.properties file.
	 * @return True, if the user is authenticated. False, otherwise
	 * @throws ConfigurationException
	 */
	public static boolean isAuthenticated() throws ConfigurationException{
		boolean isAuthenticated=false;
		
		initializeHeaders();
		webTarget=client.target(Constants.getHosturl()+"/is-authenticated");
		response= webTarget.request().headers(headers).cookie("LWSSO_COOKIE_KEY", ConfigurationProperties.getProperty("LWSSO_COOKIE_KEY")).get();
		
		if(response.getStatus()==401){
			System.out.println("User has not been authenticated. Provide your credentials in the GloablSettings.properties file.");
			setjSessionID(response);
			setRedirectURL(response);
			initializeHeaders();
		} else if(response.getStatus()==200){
			initializeHeaders();
			isAuthenticated=true;
			System.out.println("Authenticated user's session details are still valid and so no authentication is required.");
		}
		
		return isAuthenticated;
	}
	
	/**
	 * Authenticates the user using the credentials mentioned in the GlobalSettings.properties file. 
	 * @throws ConfigurationException
	 */
	public static void authenticate() throws ConfigurationException{
		ClientConfig clientConfig = new ClientConfig();
		
		HttpAuthenticationFeature httpAuthenticationFeature = HttpAuthenticationFeature.basic(Constants.getUsername(), Constants.getPassword());
		clientConfig.register(httpAuthenticationFeature);
		
		client = ClientBuilder.newClient(clientConfig);
		webTarget = client.target(getRedirectURL()+"/authenticate");
		Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
		response=invocationBuilder.get();
		
		if(response.getStatus()==200){
			System.out.println("User has been authenticated.");
			setLwsso_cookie_key(response);
		}else
			System.out.println("Invalid login credentials.");
	}
	
/*	*//**
	 * @return the jSessionID
	 *//*
	private static Object getjSessionID() {
		return jSessionID;
	}*/

	/**
	 * @param jSessionID the jSessionID to set
	 * @throws ConfigurationException 
	 */
	private static void setjSessionID(Response response) throws ConfigurationException {
		jSessionID=response.getHeaders().get("Set-Cookie").get(0).toString();
		ConfigurationProperties.setProperty("JSESSIONID", jSessionID.toString());
	}

	/**
	 * @return the lwsso_cookie_key
	 *//*
	private static Object getLwsso_cookie_key() {
		return ConfigurationProperties.getProperty("LWSSO_COOKIE_KEY");
	}*/

	/**
	 * @param lwsso_cookie_key the lwsso_cookie_key to set
	 * @throws ConfigurationException 
	 */
	private static void setLwsso_cookie_key(Response response) throws ConfigurationException {
		String value =response.getHeaders().get("Set-Cookie").toString();
		value=value.substring(value.indexOf("EY=")+3, value.indexOf(";P"));
		lwsso_cookie_key=value;
		ConfigurationProperties.setProperty("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString());
	}

	private static void initializeHeaders(){
		headers.add("JSESSIONID", ConfigurationProperties.getProperty("JSESSIONID"));
	}
	
		/**
	 * @return the redirectURL
	 */
	private static String getRedirectURL() {
		redirectURL = response.getHeaders().get("WWW-Authenticate").toString();
		redirectURL=redirectURL.substring(redirectURL.indexOf("\"")+1, redirectURL.length()-2);
		return redirectURL;
	}

	private static void setRedirectURL(Response response){
		redirectURL=Constants.getBaseurl()+"/authentication-point";
	}
}

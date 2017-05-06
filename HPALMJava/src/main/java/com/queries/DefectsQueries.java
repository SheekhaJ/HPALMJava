package com.queries;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.authentication.Authentication;
import com.authentication.Constants;

public class DefectsQueries extends Authentication{
	private static Client client;
	private static WebTarget webTarget;
	private static Response response;
	private static MultivaluedMap<String, Object> headers;
	private static String baseURL;
	private static Object lwsso_cookie_key;
	
	static{
		client=Authentication.client;
		webTarget=Authentication.webTarget;
		response=Authentication.response;
		headers=Authentication.headers;
		lwsso_cookie_key=Authentication.lwsso_cookie_key;
		baseURL=Constants.getBaseurl()+"/defects";
	}
	
	/**
	 * 
	 * @param testSetID
	 * @return
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getDefect(String testSetID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{cycle-id['"+testSetID+"'];status['Passed']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=test-id,owner,cycle-id,duration,status");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
}

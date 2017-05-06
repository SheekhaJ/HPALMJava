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

public class TestsQueries extends Authentication{
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
		baseURL=Constants.getBaseurl()+"/tests";
	}
	
	/**
	 * Gets all the tests whose parent test-folder id is passed as parameter. 
	 * @param parentFolderID id of the parent test-folder. 
	 * @return id, name and parent-id of all the tests under the test-folder passed as parameter. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getTests(String parentFolderID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{parent-id['"+parentFolderID+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets the test name whose id is passed as parameter. 
	 * @param testID id of the test which is passed as parameter. 
	 * @return name of the test whose id is passed as parameter. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getTestName(String testID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{id['"+testID+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
}

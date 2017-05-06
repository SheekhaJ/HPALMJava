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

public final class TestRunsQueries extends Authentication{
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
		baseURL=Constants.getBaseurl()+"/runs";
	}
	
	/**
	 * Gets the passed test-instances of all the test-instances belonging to a particular test-set. 
	 * @param testSetID test-set id whose passed runs have to be fetched. 
	 * @return test-id, owner, cycle-id, duration, status of the test-instances belonging to the test-set passed as parameter. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getPassedTestRuns(String testSetID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{cycle-id['"+testSetID+"'];status['Passed']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=test-id,owner,cycle-id,duration,status");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets all the failed runs of all test-instances which are part of the test-set id passed as parameter. 
	 * @param testSetID id of the test-set whose test-instances' failed runs are to be returned. 
	 * @return test-id, owner, cycle-id, duration, status of the test-instances which are part of the test-set id passed as parameter.
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getFailedTestRuns(String testSetID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{cycle-id['"+testSetID+"'];status['Failed']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=test-id,owner,cycle-id,duration,status");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets all the runs which are part of the test-set id passed as parameter. 
	 * @param testSetID Test-set id whose test-runs have to be fetched. 
	 * @return test-id, owner, cycle-id, duration, status of the test-instances which are part of the test-set id passed as parameter. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getAllRuns(String testSetID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{cycle-id['"+testSetID+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=test-id,owner,cycle-id,duration,status");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets all the runs of the test-id passed as parameter irrespective of the test-set id. 
	 * @param testID test-id whose runs are to be fetched. 
	 * @return owner, cycle-id, duration, status of a test whose id is passed as parameter. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getRuns(String testID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{test-id['"+testID+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=owner,cycle-id,duration,status");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets all the runs of the test-id belonging to the test-set id passed as parameters. 
	 * @param testID test-set id of the test whose runs have to be fetched. 
	 * @param cycleID  
	 * @return owner, cycle-id, duration, status of the test-id belonging to the test-set id passed as parameters.
	 * @throws UnsupportedEncodingException
	 */
	public static String getRuns(String testID, String cycleID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{test-id['"+testID+"'];cycle-id["+cycleID+"]}", "UTF-8").replaceAll("\\+", "%20")+"&fields=owner,cycle-id,duration,status");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
}

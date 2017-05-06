package com.queries;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.authentication.Authentication;
import com.authentication.Constants;
import com.parseXML.DOM.ParseXML;

public final class TestSetFoldersQueries extends Authentication{
	private static Client client;
	private static WebTarget webTarget;
	private static Response response;
	private static MultivaluedMap<String, Object> headers;
	private static String baseURL;
	private static Object lwsso_cookie_key;
	private static ParseXML parseXML;
	
	static{
		client=Authentication.client;
		webTarget=Authentication.webTarget;
		response=Authentication.response;
		headers=Authentication.headers;
		lwsso_cookie_key=Authentication.lwsso_cookie_key;
		baseURL=Constants.getBaseurl()+"/test-set-folders";
	}
	
	/**
	 * Gets the test-set-folder id of the test-set folder name passed as a parameter. 
	 * @param testSetFolderName Test-set folder name whose id has to be fetched. 
	 * @return XML string containing the id of the test-set folder name passed as parameter. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getTestSetFolderID(String testSetFolderName) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{name['"+testSetFolderName+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets the test-set folder id of the test-set folder name which has parent test-set-folder id passed as parameter.   
	 * @param testSetFolderName Test-set folder name whose id has to be fetched and which has parent-id passed as parameter.  
	 * @param parentFolderID id of the parent-folder.
	 * @return XML string containing the id of the test-set-folder name passed as parameter. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getTestSetFolderID(String testSetFolderName, String parentFolderID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{name['"+testSetFolderName+"'];parent-id["+parentFolderID+"]}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets the child test-sets of the parent test-set-folder whose / containing path is passed as parameter. 
	 * @param parentTestSetFolderPath Path of the parent-test-set folder. 
	 * @return XML string containing the id of the folder indicated by the test-set-folder path passed as parameter. 
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws XPathExpressionException
	 * @author Sheekha Jariwala
	 */
	public static String getTestSetFolderIDFromPath(String parentTestSetFolderPath) throws SAXException, IOException, ParserConfigurationException, XPathExpressionException{
		String response=null;
		String folderName=null,parentID=null,childFolderName=null;

		if(parentTestSetFolderPath.contains("/")){
			String folders[]=parentTestSetFolderPath.split("/", 30);

			for(int i=0; i<folders.length-1; i++){
				folderName=folders[i];
				if(i==0){
					if(folders[0].compareToIgnoreCase("Subject")==0)
						parentID="2";
					else {
						String resp=getTestSetFolderID(folderName);
						parseXML=new ParseXML(resp);
						parentID=parseXML.getID();
						response=resp;
					}
				}
				childFolderName=folders[i+1];
				response=getTestSetFolderID(childFolderName,parentID);
				parseXML=new ParseXML(response);
				parseXML.setResponseString(response);
				parentID=parseXML.getID();
			}
		} 
		response=getTestSetFolderID(childFolderName);
		parseXML=new ParseXML(response);
		parseXML.setResponseString(response);
		parentID=parseXML.getID();

		return response;
	}
	
	/**
	 * Gets the Child test-set folder ids of the parent test-set folder id passed as a parameter. 
	 * @param parentTestSetFolderID id of the parent-test-set folder. 
	 * @return XML string containing all the child test-set-folders of a test-set folder. 
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getChildTestSetFolderID(String parentTestSetFolderID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{parent-id['"+parentTestSetFolderID+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
}

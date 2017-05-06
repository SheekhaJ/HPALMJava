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

public final class TestFolderQueries extends Authentication{
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
		baseURL=Constants.getBaseurl()+"/test-folders";
	}
	
	/**
	 * Gets the test-folder ID of the test-folder passed as parameter. 
	 * @return Returns the XML string containing the id, name and parent-id of the folder name passed as parameter.
	 * @author Sheekha Jariwala 
	 */
	public static String getTestFolderID(String folderName) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{name['"+folderName+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets the name of the test-folder whose id is passed as parameter. 
	 * @param folderID Test-folder ID whose name has to be fetched. 
	 * @return Returns the XML string containing the id, name and parent-id of the folder id passed as parameter.
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getTestFolderName(String folderID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{id['"+folderID+"']}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets the id of the child folder whose parent-id is passed as parameter. 
	 * @param folderName Name of the child folder. 
	 * @param parentFolderID ID of the parent-folder. 
	 * @return Returns the XML string containing the id, name and parent-id of the folder name and parent folder-id passed as parameter.
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getChildFolderID(String folderName, String parentFolderID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{name['"+folderName+"'];parent-id["+parentFolderID+"]}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets all the Child folders of the parent folder-id passed as parameter. 
	 * @param parentFolderID Parent Folder id whose child folders are to be fetched. 
	 * @return Returns the XML string containing the id, name and parent-id of the parent folder-id passed as parameter.
	 * @throws UnsupportedEncodingException
	 * @author Sheekha Jariwala
	 */
	public static String getChildFolders(String parentFolderID) throws UnsupportedEncodingException{
		webTarget=client.target(baseURL+"?query="+URLEncoder.encode("{parent-id["+parentFolderID+"]}", "UTF-8").replaceAll("\\+", "%20")+"&fields=id,name,parent-id");
		response = webTarget.request(MediaType.APPLICATION_XML).headers(headers).cookie("LWSSO_COOKIE_KEY", lwsso_cookie_key.toString()).get();
		return response.readEntity(String.class);
	}
	
	/**
	 * Gets the test-folder's id of the / containing folder path passed as parameter.  
	 * @param parentFolderPath Parent folder path. 
	 * @return Returns the XML string containing the id, name and parent-id of the folder name whose parent folder-id is passed as parameter.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @author Sheekha Jariwala
	 */
	public static String getTestFolderIDFromPath(String parentFolderPath) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
		String response=null;
		String folderName=null,parentID=null,childFolderName=null;

		if(parentFolderPath.contains("/")){
			String folders[]=parentFolderPath.split("/", 30);

			for(int i=0; i<folders.length-1; i++){
				folderName=folders[i];
				if(i==0){
					if(folders[0].compareToIgnoreCase("Subject")==0)
						parentID="2";
					else {
						String resp=getTestFolderID(folderName);
						parseXML=new ParseXML(resp);
						parentID=parseXML.getID();
						response=resp;
					}
				}
				childFolderName=folders[i+1];
				response=getChildFolderID(childFolderName,parentID);
				parseXML=new ParseXML(response);
				parseXML.setResponseString(response);
				parentID=parseXML.getID();
			}
		} 
		response=getTestFolderID(childFolderName);
		parseXML=new ParseXML(response);
		parseXML.setResponseString(response);
		parentID=parseXML.getID();

		return response;
	}
}

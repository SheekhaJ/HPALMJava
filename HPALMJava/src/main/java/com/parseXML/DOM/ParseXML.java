package com.parseXML.DOM;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public final class ParseXML {
	private String response;
	private DocumentBuilderFactory documentBuilderFactory;
	private DocumentBuilder documentBuilder;
	private Document document;
	
	XPathFactory xpathFactory;
	XPath xpath;
	XPathExpression xPathExpression;
	
	/**
	 * Initializes the ParseXML class to the response string passed as parameter.  
	 * @param response
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public ParseXML(String response) throws ParserConfigurationException, SAXException, IOException{
		this.response=response;
		documentBuilderFactory=DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
		documentBuilder=documentBuilderFactory.newDocumentBuilder();
		document=documentBuilder.parse(new InputSource(new StringReader(response)));
		document.getDocumentElement().normalize();
		
		xpathFactory=XPathFactory.newInstance();
		xpath=xpathFactory.newXPath();
	}
	
	/**
	 * Sets the string to be parsed to the string that has been passed as parameter. 
	 * @param resp Valid XML string that has to be parsed. 
	 * @throws SAXException
	 * @throws IOException
	 * @throws XPathExpressionException
	 * @author Sheekha Jariwala
	 */
	public void setResponseString(String resp) throws SAXException, IOException, XPathExpressionException{
		response=resp;
		document=documentBuilder.parse(new InputSource(new StringReader(response)));
		document.getDocumentElement().normalize();
	}
	
	/**
	 * Gets the number of Fields nodes in the response string.   
	 * @return Number of Fields nodes in the response string. 
	 * @author Sheekha Jariwala
	 */
	public int getNumberOfFieldsNodes(){
		return document.getElementsByTagName("Fields").getLength();
	}
	
	/**
	 * Gets the id in the response string containing a single result.   
	 * @return id of the result in the response string. 
	 * @author Sheekha Jariwala
	 */
	public String getID(){
		String id=null;
		
		NodeList fieldsTagsNodeList = document.getElementsByTagName("Fields");
		//System.out.println("Fields tags: "+fieldsTagsNodeList.getLength());
		
		for(int i=0; i<fieldsTagsNodeList.getLength(); i++){
			
			Node fieldsTag = fieldsTagsNodeList.item(i);
			Element fieldsTagElement = (Element) fieldsTag;
			
			NodeList fieldTagsNodeList = fieldsTagElement.getElementsByTagName("Field");
			
			//System.out.println("Field Tags under Fields tag: "+fieldTagsNodeList.getLength());
			
			for(int j=0; j<fieldTagsNodeList.getLength(); j++){
				Node fieldTag = fieldTagsNodeList.item(j);
				Element fieldTagElement = (Element) fieldTag;
				
				if(fieldTagElement.getAttribute("Name").contentEquals("id")){
					//The first child is a Text node, second child is Element node and third child is Text Node.  
					NodeList valueTag = fieldTagElement.getChildNodes();
					
					for(int k=0; k<fieldTagElement.getChildNodes().getLength(); k++){
						if(valueTag.item(k).getNodeType() ==  Node.ELEMENT_NODE)
							id=valueTag.item(k).getTextContent();
					}
					
					/*Element valueTag = (Element) fieldTagElement.getElementsByTagName("Value").item(0);
					System.out.println(valueTag.getTextContent());*/
				} /*else if(fieldTagElement.getAttribute("Name").contentEquals("name")){
					Node valueTag = (Node) fieldTagElement.getChildNodes().item(1);
					System.out.println(valueTag.getTextContent());
				}*/
			}
			
		}
		
		return id;
	}
	
	/**
	 * Gets the name in the response string containing a single result.
	 * @return name of the result in the response string.
	 * @author Sheekha Jariwala
	 */
	public String getName() {

		String id=null;
		
		NodeList fieldsTagsNodeList = document.getElementsByTagName("Fields");
		//System.out.println("Fields tags: "+fieldsTagsNodeList.getLength());
		
		for(int i=0; i<fieldsTagsNodeList.getLength(); i++){
			
			Node fieldsTag = fieldsTagsNodeList.item(i);
			Element fieldsTagElement = (Element) fieldsTag;
			
			NodeList fieldTagsNodeList = fieldsTagElement.getElementsByTagName("Field");
			
			//System.out.println("Field Tags under Fields tag: "+fieldTagsNodeList.getLength());
			
			for(int j=0; j<fieldTagsNodeList.getLength(); j++){
				Node fieldTag = fieldTagsNodeList.item(j);
				Element fieldTagElement = (Element) fieldTag;
				
				if(fieldTagElement.getAttribute("Name").contentEquals("name")){
					//The first child is a Text node, second child is Element node and third child is Text Node.  
					NodeList valueTag = fieldTagElement.getChildNodes();
					
					for(int k=0; k<fieldTagElement.getChildNodes().getLength(); k++){
						if(valueTag.item(k).getNodeType() ==  Node.ELEMENT_NODE)
							id=valueTag.item(k).getTextContent();
					}
					
					/*Element valueTag = (Element) fieldTagElement.getElementsByTagName("Value").item(0);
					System.out.println(valueTag.getTextContent());*/
				} /*else if(fieldTagElement.getAttribute("Name").contentEquals("name")){
					Node valueTag = (Node) fieldTagElement.getChildNodes().item(1);
					System.out.println(valueTag.getTextContent());
				}*/
			}
			
		}
		
		return id;
	
	}
	
	/**
	 * Gets the text content of the value tag under the Field attribute passed as parameter.  
	 * @param attribute Field attribute whose value tag's text content has to be fetched.  
	 * @return Text content value of the Value tag under the Field having attribute passed as parameter. 
	 * @author Sheekha Jariwala
	 */
	public String getAttribute(String attribute){
		NodeList fieldsTagsNodeList = document.getElementsByTagName("Fields");
		//System.out.println("Fields tags: "+fieldsTagsNodeList.getLength());
		
		for(int i=0; i<fieldsTagsNodeList.getLength(); i++){
			
			Node fieldsTag = fieldsTagsNodeList.item(i);
			Element fieldsTagElement = (Element) fieldsTag;
			
			NodeList fieldTagsNodeList = fieldsTagElement.getElementsByTagName("Field");
			
			//System.out.println("Field Tags under Fields tag: "+fieldTagsNodeList.getLength());
			
			for(int j=0; j<fieldTagsNodeList.getLength(); j++){
				Node fieldTag = fieldTagsNodeList.item(j);
				Element fieldTagElement = (Element) fieldTag;
				
				if(fieldTagElement.getAttribute("Name").contentEquals(attribute)){
					//The first child is a Text node, second child is Element node and third child is Text Node.  
					NodeList valueTag = fieldTagElement.getChildNodes();
					
					for(int k=0; k<fieldTagElement.getChildNodes().getLength(); k++){
						if(valueTag.item(k).getNodeType() ==  Node.ELEMENT_NODE)
							attribute=valueTag.item(k).getTextContent();
					}
					
					/*Element valueTag = (Element) fieldTagElement.getElementsByTagName("Value").item(0);
					System.out.println(valueTag.getTextContent());*/
				} /*else if(fieldTagElement.getAttribute("Name").contentEquals("name")){
					Node valueTag = (Node) fieldTagElement.getChildNodes().item(1);
					System.out.println(valueTag.getTextContent());
				}*/
			}
			
		}
		
		return attribute;
	} 
	
	/**
	 * Gets the id and name from the V
	 * @return HashMap containing id and name from the response string.  
	 * @throws SAXException
	 * @throws IOException
	 * @author Sheekha Jariwala
	 */
	public LinkedHashMap<String, String> getIDName() throws SAXException, IOException{
		//setResponseString(response);
		LinkedHashMap<String, String> idName = new LinkedHashMap<String, String>();
		
		String id=null,name=null;
		
		NodeList fieldsTagsNodeList = document.getElementsByTagName("Fields");
		//System.out.println("Fields tags: "+fieldsTagsNodeList.getLength());
		
		for(int i=0; i<fieldsTagsNodeList.getLength(); i++){
			
			Node fieldsTag = fieldsTagsNodeList.item(i);
			Element fieldsTagElement = (Element) fieldsTag;
			
			NodeList fieldTagsNodeList = fieldsTagElement.getElementsByTagName("Field");
			
			//System.out.println("Field Tags under Fields tag: "+fieldTagsNodeList.getLength());
			
			for(int j=0; j<fieldTagsNodeList.getLength(); j++){
				Node fieldTag = fieldTagsNodeList.item(j);
				Element fieldTagElement = (Element) fieldTag;

				if(fieldTagElement.getAttribute("Name").contentEquals("id")){
					NodeList valueTag = fieldTagElement.getChildNodes();

					for(int k=0; k<fieldTagElement.getChildNodes().getLength(); k++){
						if(valueTag.item(k).getNodeType() ==  Node.ELEMENT_NODE)
							id=valueTag.item(k).getTextContent();
					}

					/*Element valueTag = (Element) fieldTagElement.getElementsByTagName("Value").item(0);
					System.out.println(valueTag.getTextContent());*/
				} else if(fieldTagElement.getAttribute("Name").contentEquals("name")){
					NodeList valueTag = fieldTagElement.getChildNodes();

					for(int k=0; k<fieldTagElement.getChildNodes().getLength(); k++){
						if(valueTag.item(k).getNodeType() ==  Node.ELEMENT_NODE)
							name=valueTag.item(k).getTextContent();
					}}

				idName.put(id, name);
			}
			
		}
		
		return idName;
	}
	
	/**
	 * Returns the nodes evaluated by the xpath expression passed as parameter.  
	 * @param xpathExpr Xpath expression to evaluate the response. 
	 * @return List containing the nodes matching the xpath expression.  
	 * @throws XPathExpressionException
	 * @author Sheekha Jariwala
	 */
	public List<String> getNodesByXpath(String xpathExpr) throws XPathExpressionException{
		List<String> list = new ArrayList<String>();
		
		xPathExpression = xpath.compile(xpathExpr);
		NodeList nodeList = (NodeList)xPathExpression.evaluate(document,XPathConstants.NODESET);
		
		for(int i=0; i<nodeList.getLength(); i++){
			list.add(nodeList.item(i).getTextContent());
		}
		
		return list;
	}
}

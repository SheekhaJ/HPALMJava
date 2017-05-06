package com.parseXML.JAXB;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;


public final class ParseXML {
	
	static String response = null;
	private static JAXBContext jaxbContext;
	private static Unmarshaller unmarshaller;
	private static StringReader stringReader;
	
	public ParseXML(String response) throws JAXBException{
		stringReader=new StringReader(response);
		jaxbContext=JAXBContext.newInstance(Entities.class);
		unmarshaller=jaxbContext.createUnmarshaller();
	}
	
	public void initializeNewResponseString(String newResponse){
		response=newResponse;
	}
	
	public static Object unmarshallResponse() throws JAXBException{
		Entities entities = (Entities) unmarshaller.unmarshal(stringReader);
		return entities;
	}
}

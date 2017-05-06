package com.parseXML.JAXB;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@XmlAccessorType(XmlAccessType.FIELD)
public class Field {
	
	private List<String> name = new ArrayList<String>();
	
	/*@XmlElement(name="Field")
	@XmlJavaTypeAdapter(Name)
	public List<> getName()*/
	
}

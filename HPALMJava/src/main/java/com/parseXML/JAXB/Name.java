package com.parseXML.JAXB;

import javax.xml.bind.annotation.XmlAttribute;

public abstract class Name {
	private String name;
	
	@XmlAttribute
	public String getName(){
		return name;
	}
	
	public void setName(String name){
		this.name=name;
	}
}

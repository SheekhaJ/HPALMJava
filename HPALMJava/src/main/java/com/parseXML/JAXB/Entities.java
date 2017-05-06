package com.parseXML.JAXB;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Entities {
	
	@XmlElement(name="Entity")
	@XmlAttribute(name="Type")
	private String Entity;
	
	@XmlElementWrapper(name="Fields")
	@XmlElement(name="Field")
	private List<Field> fields;
	
	
	
}

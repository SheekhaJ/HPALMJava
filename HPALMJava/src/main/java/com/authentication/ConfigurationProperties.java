package com.authentication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public final class ConfigurationProperties {
	
	private static PropertiesConfiguration propertiesConfiguration;
	private static HashMap<String, String> properties;
	
	static{
		try {
			properties=new HashMap<String, String>();
			propertiesConfiguration = new PropertiesConfiguration("GlobalSettings.properties");
			
			Iterator<String> keys = propertiesConfiguration.getKeys();
			while(keys.hasNext()){
				String key=keys.next();
				properties.put(key, propertiesConfiguration.getProperty(key).toString());
			}
			
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Fetches the property name passed as a parameter from the GlobalSettings.properties file. 
	 * @param propertyName Name of the property whose value has to be fetched. 
	 * @return Value of the property whose value has to be fetched. 
	 */
	public static String getProperty(String propertyName){
		String value=propertiesConfiguration.getProperty(propertyName).toString();
		return value;
	}
	
	/**
	 * Sets the property value of the property name passed as the parameter. 
	 * @param propertyName Name of the property to be set. 
	 * @param propertyValue Value to which the mentioned property has to be set to. 
	 * @throws ConfigurationException
	 */
	public static void setProperty(String propertyName, String propertyValue) throws ConfigurationException{
		propertiesConfiguration.setProperty(propertyName, propertyValue);
		propertiesConfiguration.save();
	}
	
	/**
	 * Fetches all the properties mentioned in the GlobalSettings.properties file. 
	 * @return Returns all the properties entered in the GlobalSettings.properties file.
	 */
	public static List<String> getAllPropertyName(){
		Iterator<String> propertyNames = propertiesConfiguration.getKeys();
		
		List<String> propertyN = new ArrayList<String>();
		
		for (Iterator<String> iterator = propertyNames; iterator.hasNext();) {
			String property = (String) iterator.next();
			propertyN.add(property);
		}
		
		return propertyN;
	}
	
	/**
	 * Fetches all the properties name and their corresponding values from GlobalSettings.properties file.
	 * @return HashMap containing the properties of all the properties name and their corresponding values from GlobalSettings.properties file.
	 */
	public static HashMap<String, String> getPropertiesNameValues(){
		return properties;
	}
}

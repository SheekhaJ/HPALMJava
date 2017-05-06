package com.authentication;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

public final class Constants {
	
	private static PropertiesConfiguration propertiesConfiguration;
	
	private static String hostURL;
	private static String username;
	private static String password;
	private static String domain;
	private static String project;
	private static String baseURL;

	static{
		try {
			propertiesConfiguration = new PropertiesConfiguration("GlobalSettings.properties");
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		hostURL=propertiesConfiguration.getProperty("hostURL").toString();
		username=propertiesConfiguration.getProperty("username").toString();
		password=propertiesConfiguration.getProperty("password").toString();
		domain=propertiesConfiguration.getProperty("domain").toString();
		project=propertiesConfiguration.getProperty("project").toString();
		baseURL=hostURL+"domains/"+domain+"/projects/"+project;
	}
	
	/**
	 * @return the baseurl
	 */
	public static String getBaseurl() {
		return baseURL;
	}
	/**
	 * @return the hosturl
	 */
	public static String getHosturl() {
		return hostURL;
	}
	/**
	 * @return the username
	 */
	public static String getUsername() {
		return username;
	}
	/**
	 * @return the password
	 */
	public static String getPassword() {
		return password;
	}
	/**
	 * @return the domain
	 */
	public static String getDomain() {
		return domain;
	}
	/**
	 * @return the project
	 */
	public static String getProject() {
		return project;
	}
	
	
}

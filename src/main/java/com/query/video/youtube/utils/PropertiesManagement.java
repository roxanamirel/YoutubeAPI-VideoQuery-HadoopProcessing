package com.query.video.youtube.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManagement {
	/**
	 * File that contains my developer's API key and application name
	 */
	
	private static final String PROPERTIES_FILENAME = "youtube.properties";
	/*
	 * Returns a property value from Properties
	 * 
	 */
	public static String getPropertyFromProperties(String propertyName) {
		Properties properties = getProperties();
		String appName = properties.getProperty(propertyName);
		return appName;
	}

	/*
	 * Returns the Properties object from youtube.properties file
	 * 
	 */
	private static Properties getProperties() {
		// Read the developer key from the properties file.
		Properties properties = new Properties();
		try {
			InputStream in = VideoQuery.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println(
					"There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
			System.exit(1);
		}
		return properties;
	}

	

}

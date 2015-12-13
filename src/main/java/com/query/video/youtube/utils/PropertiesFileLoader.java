package com.query.video.youtube.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.query.video.youtube.service.impl.VideoQueryServiceImpl;

public class PropertiesFileLoader {
	
	/**
	 * File that contains developer's API key and application name
	 */	
	private static final String PROPERTIES_FILENAME = "youtube.properties";
	
	public static final String PROP_APP_NAME = "app.name";
	public static final String PROP_YOUTUBE_APIKEY = "youtube.apikey";
	
	/**
	 * @param propertyName - the name of the property to be retrieved
	 * @return the property associated to propertyName
	 */
	public static String getPropertyFromProperties(String propertyName) {
		Properties properties = getProperties();
		return properties.getProperty(propertyName);
	}
	
	/**
	 * @return the Properties object from youtube.properties file
	 */
	private static Properties getProperties() {
		// Read the developer key from the properties file.
		Properties properties = new Properties();
		try {
			InputStream in = VideoQueryServiceImpl.class.getResourceAsStream("/" + PROPERTIES_FILENAME);
			properties.load(in);

		} catch (IOException e) {
			System.err.println(
					"There was an error reading " + PROPERTIES_FILENAME + ": " + e.getCause() + " : " + e.getMessage());
			System.exit(1);
		}
		return properties;
	}
}

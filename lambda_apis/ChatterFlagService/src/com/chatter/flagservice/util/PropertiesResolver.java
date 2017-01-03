package com.chatter.flagservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * PropertiesResolver
 * @author coreym
 *
 * Retrieves service properties from service properties file.
 * The maven profile determines the properties file from which
 * properties will be retrieved. Profiles are based on the 
 * environment (local, dev, prod).
 */
public class PropertiesResolver {
	
	private Properties props = null;
	private InputStream input = null;
	
	/**
	 * Sets up argument argument properties file if it can be
	 * retrieved from the classpath. Otherwise, props will be 
	 * NULL.
	 * @param propertiesFileName the properties file to retrieve from
	 * 		  the classpath
	 */
	public PropertiesResolver(String propertiesFileName){
		try {
			input = PropertiesResolver.class.getClassLoader()
						.getResourceAsStream(propertiesFileName);
			
			if (input != null) {
				props = new Properties();
				props.load(input);
			}
		}
		catch (IOException ioEx) {
			ioEx.printStackTrace();
		}
		finally {
			//Close input stream
			if (input != null) {
				try {
					input.close();
				}
				catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Retrieves argument property from classpath properties file
	 * @param property the property to retrieve
	 * @return the property value
	 */
	public String getProperty(String property) {
		String propValue = null;
		if (this.props != null) {
			propValue = this.props.getProperty(property);
		}
		
		return propValue;
	}
}

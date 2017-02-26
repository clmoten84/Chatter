package com.chatter.fileservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.chatter.fileservice.exceptions.PropertyRetrievalException;
import com.chatter.fileservice.util.PropertiesResolver;

/**
 * PropertiesResolver
 * @author coreym
 *
 * Utility class for resolving properties from
 * service properties files.
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
	 * @throws PropertyRetrievalException 
	 */
	public PropertiesResolver(String propertiesFileName) throws PropertyRetrievalException{
		try {
			input = PropertiesResolver.class.getClassLoader()
						.getResourceAsStream(propertiesFileName);
			
			if (input != null) {
				props = new Properties();
				props.load(input);
			}
		}
		catch (IOException ioEx) {
			throw new PropertyRetrievalException("ERROR: An error occurred " +
					"while trying to retrieve service properties from properties " +
					"file.");
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
	public String getProperty(String property) throws PropertyRetrievalException{
		String propValue = null;
		if (this.props != null) {
			propValue = this.props.getProperty(property);
		}
		else {
			throw new PropertyRetrievalException("ERROR: PropertiesResolver is NULL! "
					+ "There was an issue creating the PropertiesResolver object!");
		}
		
		return propValue;
	}
}

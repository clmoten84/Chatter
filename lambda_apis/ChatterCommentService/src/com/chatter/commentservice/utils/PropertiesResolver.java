package com.chatter.commentservice.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.chatter.commentservice.exceptions.PropertyRetrievalException;

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
	 * Sets up argument properties file if it can be retrieved from 
	 * the classpath. Otherwise props will be NULL.
	 * @param propertiesFile the properties file to retrieve from classpath
	 * @throws PropertyRetrievalException
	 */
	public PropertiesResolver(String propertiesFile) throws PropertyRetrievalException{
		try {
			input = PropertiesResolver.class.getClassLoader()
						.getResourceAsStream(propertiesFile);
			
			if (input != null) {
				props = new Properties();
				props.load(input);
			}
		}
		catch (IOException ioe) {
			throw new PropertyRetrievalException("ERROR: An error occurred " +
					"while trying to retrieve service properties from properties " +
					"file.");
		}
		finally {
			// Close input stream
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
	 * Retrieves argument property value from classpath properties file
	 * @param property the property to retrieve
	 * @return the property value
	 * @throws PropertyRetrievalException
	 */
	public String getProperty(String property) throws PropertyRetrievalException {
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

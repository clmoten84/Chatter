package com.chatter.dbservice.exceptions;

/**
 * PropertyRetrievalException
 * @author coreym
 *
 * Exception is thrown when an error is encountered while
 * attempting to retrieve service properties using the 
 * PropertiesResolver class.
 */
public class PropertyRetrievalException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public PropertyRetrievalException(String message) {
		super(message);
	}

}

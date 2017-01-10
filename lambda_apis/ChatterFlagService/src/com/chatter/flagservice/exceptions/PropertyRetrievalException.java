package com.chatter.flagservice.exceptions;

/**
 * PropertyRetrievalException
 * @author coreym
 *
 * Exception that is thrown when an error occurs while
 * attempting to retrieve property values from the service
 * properties file.
 */
public class PropertyRetrievalException extends Exception{

	private static final long serialVersionUID = 1L;

	public PropertyRetrievalException(String message) {
		super(message);
	}
}

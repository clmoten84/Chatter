package com.chatter.fileservice.exceptions;

/**
 * PropertyRetrievalException
 * @author coreym
 *
 * Exception that gets thrown when a service property
 * is unable to be retrieved from a service based
 * properties file.
 */
public class PropertyRetrievalException extends Exception{

	private static final long serialVersionUID = 1L;

	public PropertyRetrievalException (String message) {
		super(message);
	}
}

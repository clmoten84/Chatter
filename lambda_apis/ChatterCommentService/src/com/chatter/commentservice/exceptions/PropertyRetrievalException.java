package com.chatter.commentservice.exceptions;

/**
 * PropertyRetrievalException
 * @author coreym
 *
 * Exception that is thrown when an error occurs while attempting
 * to retrieve properties from service properties files.
 */
public class PropertyRetrievalException extends Exception{

	private static final long serialVersionUID = 1L;

	public PropertyRetrievalException(String message) {
		super(message);
	}
}

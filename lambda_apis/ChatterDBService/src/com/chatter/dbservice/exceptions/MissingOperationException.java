package com.chatter.dbservice.exceptions;

/**
 * MissingOperationException
 * @author coreym
 *
 * Exception that is thrown when an incoming request is missing
 * the required operation attribute value.
 */
public class MissingOperationException extends Exception{

	private static final long serialVersionUID = 1L;

	public MissingOperationException(String message) {
		super(message);
	}
}

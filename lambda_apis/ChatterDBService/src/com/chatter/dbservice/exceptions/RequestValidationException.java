package com.chatter.dbservice.exceptions;

/**
 * RequestValidationException
 * @author coreym
 *
 * Exception that is thrown when an incoming request does not
 * pass service validation.
 */
public class RequestValidationException extends Exception{

	private static final long serialVersionUID = 1L;

	public RequestValidationException(String message) {
		super(message);
	}
}

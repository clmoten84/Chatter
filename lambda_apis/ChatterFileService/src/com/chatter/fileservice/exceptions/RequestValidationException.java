package com.chatter.fileservice.exceptions;

/**
 * RequestValidationException
 * @author coreym
 *
 * Exception that gets thrown when an incoming request
 * fails service validation.
 */
public class RequestValidationException extends Exception {

	private static final long serialVersionUID = 1L;

	public RequestValidationException(String message) {
		super(message);
	}
}

package com.chatter.forumservice.exceptions;

/**
 * RequestValidationException
 * 
 * This exception is thrown when a request does not
 * pass validation.
 * @author coreym
 *
 */
public class RequestValidationException extends Exception {
	
	private static final long serialVersionUID = 1L;

	public RequestValidationException(String message) {
		super(message);
	}
}

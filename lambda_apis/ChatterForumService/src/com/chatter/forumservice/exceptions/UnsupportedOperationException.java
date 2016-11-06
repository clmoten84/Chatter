package com.chatter.forumservice.exceptions;

/**
 * UnsupportedOperationException
 * 
 * This exception is thrown when a request is received 
 * that references an operation that is not supported
 * by the service.
 * @author coreym
 *
 */
public class UnsupportedOperationException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnsupportedOperationException(String message) {
		super(message);
	}
}

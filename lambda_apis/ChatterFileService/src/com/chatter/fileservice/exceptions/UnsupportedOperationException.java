package com.chatter.fileservice.exceptions;

/**
 * UnsupportedOperationException
 * @author coreym
 *
 * Exception that gets thrown when an incoming request 
 * contains an operation that this service can not handle.
 */
public class UnsupportedOperationException extends Exception {

	private static final long serialVersionUID = 1L;

	public UnsupportedOperationException(String message) {
		super(message);
	}
}

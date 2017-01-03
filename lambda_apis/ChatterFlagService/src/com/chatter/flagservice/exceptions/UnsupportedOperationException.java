package com.chatter.flagservice.exceptions;

/**
 * UnsupportedOperationException
 * @author coreym
 *
 * Exception that is thrown when an incoming request
 * contains an invalid operation.
 */
public class UnsupportedOperationException extends Exception{

	private static final long serialVersionUID = 1L;

	public UnsupportedOperationException(String message) {
		super(message);
	}
}

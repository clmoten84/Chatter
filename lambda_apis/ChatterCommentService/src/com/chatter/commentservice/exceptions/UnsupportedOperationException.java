package com.chatter.commentservice.exceptions;

/**
 * UnsupportedOperationException
 * @author coreym
 *
 * Exception that is thrown when an incoming request contains
 * an operation that is not supported by this service.
 */
public class UnsupportedOperationException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UnsupportedOperationException(String message) {
		super(message);
	}

}

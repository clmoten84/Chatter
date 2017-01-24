package com.chatter.dbservice.exceptions;

/**
 * UnsupportedOperationException
 * @author coreym
 * 
 * Exception that is thrown when an incoming request contains an
 * operation value that this service does not support.
 */
public class UnsupportedOperationException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public UnsupportedOperationException(String message) {
		super(message);
	}
}

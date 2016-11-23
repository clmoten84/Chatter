package com.chatter.forumservice.exceptions;

/**
 * MissingOperationException
 * 
 * This exception is thrown when an incoming request
 * contains an empty or NULL value for the operation
 * attribute.
 * 
 * @author coreym
 *
 */
public class MissingOperationException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public MissingOperationException(String message) {
		super(message);
	}
}

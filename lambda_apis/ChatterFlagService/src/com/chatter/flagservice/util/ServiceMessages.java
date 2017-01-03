package com.chatter.flagservice.util;

/**
 * ServiceMessages
 * @author coreym
 *
 * Messages indicating request failure or completion
 */
public enum ServiceMessages {
	
	OPERATION_SUCCESS("Chatter flag service operation completed successfully!"),
	OPERATION_FAILED("Chatter flag service operation failed!"),
	UNSUPPORTED_OPERATION("Service received a request that contains an invalid operation!"),
	MISSING_OPERATION("Service received a request that is missing a required operation attribute!");
	
	private final String message;
	
	private ServiceMessages(final String message){
		this.message = message;
	}
	
	public String toString() {
		return this.message;
	}
}
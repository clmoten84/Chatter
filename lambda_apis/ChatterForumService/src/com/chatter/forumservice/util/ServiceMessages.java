package com.chatter.forumservice.util;

/**
 * ServiceMessages
 * @author coreym
 *
 * Messages indicating request failure or completion
 */
public enum ServiceMessages {
	
	OPERATION_SUCCESS("Chatter forum service operation completed successfully!"),
	OPERATION_FAILED("Chatter forum service operation failed!"),
	UNSUPPORTED_OPERATION("Service received a request that contains an invalid operation!");
	
	private final String message;
	
	private ServiceMessages(final String message){
		this.message = message;
	}
	
	public String toString() {
		return this.message;
	}
}

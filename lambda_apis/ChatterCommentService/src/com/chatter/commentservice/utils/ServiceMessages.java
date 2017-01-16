package com.chatter.commentservice.utils;

/**
 * ServiceMessages
 * @author coreym
 *
 * Messages indicating request failure or success
 */
public enum ServiceMessages {
	
	OPERATION_SUCCESS("Chatter comment service operation completed successfully!"),
	OPERATION_FAILURE("Chatter comment service operation failed!"),
	UNSUPPORTED_OPERATION("Service received a request that contains an invalid operation!"),
	MISSING_OPERATION("Service received a request that is missing a required operation "
			+ "attribute!");
	
	private final String message;
	
	private ServiceMessages(final String message) {
		this.message = message;
	}
	
	public String toString() {
		return this.message;
	}
}

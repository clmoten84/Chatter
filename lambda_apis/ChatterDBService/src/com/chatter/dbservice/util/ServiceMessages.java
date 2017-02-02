package com.chatter.dbservice.util;

/**
 * ServiceMessages
 * @author coreym
 *
 * Messages indicating success/failure of service requests
 */
public enum ServiceMessages {
	
	OPERATION_SUCCESS("Chatter DB service operation completed successfully!"),
	OPERATION_FAILURE("Chatter DB service operation failed!"),
	UNSUPPORTED_OPERATION("Service received a request for an operation that is not"
			+ " supported by this service."),
	MISSING_OPERATION("Service received a request that is missing the required"
			+ " operation attribute and value."),
	INVALID_REQUEST("Service received an invalid request!"),
	PROPERTY_RETRIEVAL_FAILURE("Service failed to retrieve a required property value!");

	private final String message;
	
	private ServiceMessages(final String message) {
		this.message = message;
	}
	
	@Override
	public String toString() {
		return this.message;
	}
}

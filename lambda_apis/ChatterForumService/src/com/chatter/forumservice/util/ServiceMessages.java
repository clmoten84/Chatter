package com.chatter.forumservice.util;

/**
 * ServiceMessages
 * @author coreym
 *
 * Messages indicating request failure or completion
 */
public enum ServiceMessages {
	
	CREATE_SUCCESS("Chatter forum created and saved successfully!"),
	CREATE_FAILURE("Chatter forum could not be created and saved!"),
	RETRIEVE_SUCCESS("Chatter forum retrieved successfully!"),
	RETRIEVE_FAILURE("Chatter forum could not be retrieved!"),
	UPDATE_SUCCESS("Chatter forum successfully updated!"),
	UPDATE_FAILURE("Chatter forum could not be updated!"),
	DELETE_SUCCESS("Chatter forum deleted successfully!"),
	DELETE_FAILURE("Chatter forum could not be deleted!");
	
	private final String message;
	
	private ServiceMessages(final String message){
		this.message = message;
	}
	
	public String toString() {
		return this.message;
	}
}

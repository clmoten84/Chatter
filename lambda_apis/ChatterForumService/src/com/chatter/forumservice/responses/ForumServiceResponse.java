package com.chatter.forumservice.responses;

/**
 * ForumServiceResponse
 * @author coreym
 * @param <E>
 * 
 * Encapsulates service response data:
 * 
 * payload (E): the response payload data
 * status (boolean): status flag indicating the success of request processing
 * exceptionThrown (boolean): flag indicating whether an exception was thrown
 * 							  during request processing
 * message (String): response message
 * exceptionMessage (String): message associated with exception if an exception
 * 							  is thrown during request processing
 */
public class ForumServiceResponse<E extends Object> {

	private E payload;
	private boolean status;
	private boolean exceptionThrown;
	private String message;
	private String exceptionMessage;
	
	public ForumServiceResponse() { }

	public E getPayload() {
		return payload;
	}

	public void setPayload(E data) {
		this.payload = data;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public boolean getExceptionThrown() {
		return exceptionThrown;
	}

	public void setExceptionThrown(boolean exceptionThrown) {
		this.exceptionThrown = exceptionThrown;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getExceptionMessage() {
		return exceptionMessage;
	}

	public void setExceptionMessage(String exceptionMessage) {
		this.exceptionMessage = exceptionMessage;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Forum Service response *****")
			.append("status: ").append(this.getStatus())
			.append("exception_thrown: ").append(this.getExceptionThrown())
			.append("message: ").append(this.getMessage())
			.append("exception_message: ").append(this.getExceptionMessage())
			.append("data: ").append(this.getPayload().toString())
			.toString();
	}
}

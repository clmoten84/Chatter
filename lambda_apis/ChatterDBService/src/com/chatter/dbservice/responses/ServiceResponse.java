package com.chatter.dbservice.responses;

/**
 * ServiceResponse
 * @author coreym
 *
 * Encapsulates service response data
 * 
 * payload (E): response payload data
 * status (boolean): status flag indicating success of request
 * exceptionThrown (boolean): flag indicating if an exception occurred 
 * 							  during request processing
 * message (String): response message
 * exceptionMessage (String): message associated with exception if 
 * 							  an exception occurs during processing
 * @param <E>
 */
public class ServiceResponse<E extends Object> {

	private E payload;
	private boolean status;
	private String message;
	private boolean exceptionThrown;
	private String exceptionMessage;
	
	public ServiceResponse() { }

	public E getPayload() {
		return payload;
	}

	public void setPayload(E payload) {
		this.payload = payload;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean getExceptionThrown() {
		return exceptionThrown;
	}

	public void setExceptionThrown(boolean exceptionThrown) {
		this.exceptionThrown = exceptionThrown;
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
			.append("***** DB Service Response *****")
			.append("\nstatus: ").append(this.getStatus())
			.append("\nexception_thrown: ").append(this.getExceptionThrown())
			.append("\nmessage: ").append(this.getMessage())
			.append("\nexception_message: ").append(this.getExceptionMessage())
			.append("\npayload: ").append(this.getPayload())
			.toString();
			
		
	}
}

package com.chatter.flagservice.responses;

/**
 * FlagServiceResponse
 * @author coreym
 *
 * Encapsulates service response data:
 * 
 * payload (E): the response payload data
 * status (boolean): flag indicating success of request processing
 * message (String): response message
 * exceptionThrown (boolean): flag indicating whether an exception was
 * 							  thrown during request processing
 * exceptionMessage (String): message associated with exception if an
 * 							  exception is thrown during request processing
 * @param <E>
 */
public class FlagServiceResponse<E extends Object> {
	
	private E payload;
	private boolean status;
	private String message;
	private boolean exceptionThrown;
	private String exceptionMessage;
	
	public FlagServiceResponse() { }

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
			.append("***** Flag Service Response *****")
			.append("\nstatus: ").append(this.getStatus())
			.append("\nexception_thrown: ").append(this.getExceptionThrown())
			.append("\nmessage: ").append(this.getMessage())
			.append("\nexception_message: ").append(this.getExceptionMessage())
			.append("\npayload: ").append(this.getPayload().toString())
			.toString();
	}
}

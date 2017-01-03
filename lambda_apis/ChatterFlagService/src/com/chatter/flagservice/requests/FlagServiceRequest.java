package com.chatter.flagservice.requests;

/**
 * FlagServiceRequest
 * @author coreym
 *
 * Represents a request to this service. Encapsulates data
 * related to the request.
 * 
 * data (E): request payload
 * @param <E>
 */
public class FlagServiceRequest<E extends Request> {

	private E data;
	
	public FlagServiceRequest() { }
	
	public FlagServiceRequest(E data) {
		this.data = data;
	}
	
	public E getData() {
		return this.data;
	}
	
	public void setData(E data) {
		this.data = data;
	}
}

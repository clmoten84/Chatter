package com.chatter.forumservice.requests;

/**
 * ForumServiceRequest
 * @author coreym
 * @param <E>
 *
 * Represents a request to this service. Encapsulates data
 * related to the request.
 * 
 * data (E): request payload
 */
public class ForumServiceRequest<E extends Request> {
	
	private E data;
	
	public ForumServiceRequest() {}
	
	public ForumServiceRequest(E data) {
		this.data = data;
	}
	
	public E getData() {
		return this.data;
	}
	
	public void setData(E data) {
		this.data = data;
	}
}

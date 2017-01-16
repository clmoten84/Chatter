package com.chatter.commentservice.requests;

/**
 * CommentServiceRequest
 * @author coreym
 *
 * Represents a request to this service. Serves as an
 * object wrapper for request data.
 * 
 * data (E): request payload
 * @param <E>
 */
public class CommentServiceRequest<E extends Request> {

	private E data;
	
	public CommentServiceRequest() { }
	
	public CommentServiceRequest(E data) {
		this.data = data;
	}

	public E getData() {
		return data;
	}

	public void setData(E data) {
		this.data = data;
	}
}

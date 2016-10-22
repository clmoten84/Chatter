package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * RetrieveForumRequest
 * @author coreym
 *
 * Encapsulates service request data to fulfill retrieving
 * a Forum instance from the database.
 * 
 * forumId (String): the id of the Forum instance to retrieve
 */
public class RetrieveForumRequest extends Request{
	
	private String forumId;
	
	public RetrieveForumRequest() { super(); }
	
	public RetrieveForumRequest(String forumId) {
		super(ServiceOperations.RETRIEVE, new Date().getTime());
		this.forumId = forumId;
	}
	
	public String getForumId() {
		return this.forumId;
	}
	
	public void setForumId(String forumId) {
		this.forumId = forumId;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Retrieve Forum Request *****")
			.append("\nforum_id: ").append(this.forumId)
			.toString();
	}
}

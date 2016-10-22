package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * DeleteForumRequest
 * @author coreym
 *
 * Encapsulates service request data to fulfill deleting a Forum
 * instance from the database.
 * 
 * forumId (String): the id of the Forum instance to delete
 */
public class DeleteForumRequest extends Request{

	private String forumId;
	
	public DeleteForumRequest() { super(); }
	
	public DeleteForumRequest(String forumId) {
		super(ServiceOperations.DELETE, new Date().getTime());
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
			.append("***** Delete Forum Request *****")
			.append("\nforum_id: ").append(this.forumId)
			.toString();
	}
}

package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * CreateForumRequest
 * @author coreym
 * 
 * Encapsulates service request data to create and save a new Chatter 
 * Forum object to the database.
 * 
 * 
 *
 */
public class CreateForumRequest extends Request{
	
	private String createdBy;
	private String title;
	
	public CreateForumRequest() { super(); }
	
	public CreateForumRequest(String createdBy, String title) {
		super(ServiceOperations.CREATE, new Date().getTime());
		this.createdBy = createdBy;
		this.title = title;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Create Chatter Forum request *****")
			.append("\ncreated_by: ").append(this.createdBy)
			.append("\ntitle: ").append(this.title)
			.append("\ntimestamp: ").append(this.getReqDate())
			.toString();
	}
}

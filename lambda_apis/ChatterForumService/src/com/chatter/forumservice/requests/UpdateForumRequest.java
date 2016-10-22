package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * UpdateForumRequest
 * @author coreym
 *
 * Encapsulates service request data to fulfill updating
 * a Forum instance that exists in the database.
 * 
 * forumId (String): the id of the Forum instance to update
 * titleUpdate (String): an updated title to apply to Forum instance
 * 
 */
public class UpdateForumRequest extends Request{
	
	private String forumId;
	private String titleUpdate;
	
	public UpdateForumRequest() { super(); }
	
	public UpdateForumRequest(String forumId, String titleUpdate) {
		super(ServiceOperations.UPDATE, new Date().getTime());
		this.forumId = forumId;
		this.titleUpdate = titleUpdate;
	}

	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	public String getTitleUpdate() {
		return titleUpdate;
	}

	public void setTitleUpdate(String titleUpdate) {
		this.titleUpdate = titleUpdate;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Update Forum request *****")
			.append("forum_id: ").append(this.forumId)
			.append("title_update: ").append(this.titleUpdate)
			.toString();
	}
}

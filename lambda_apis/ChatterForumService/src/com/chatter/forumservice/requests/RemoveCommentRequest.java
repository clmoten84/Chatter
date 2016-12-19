package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * RemoveCommentRequest
 * @author coreym
 *
 * Encapsulates service request data to fulfill the removal
 * of a comment from a Forum instance that exists in the database.
 * 
 * forumId (String): the id of the Forum instance to update
 * commentId (String): the id of the Comment instance to add to Forum instance
 */
public class RemoveCommentRequest extends Request{
	private String forumId;
	private String commentId;
	
	public RemoveCommentRequest() { super(); }
	
	public RemoveCommentRequest(String forumId, String commentId) {
		super(ServiceOperations.REMOVE_COMMENT, new Date().getTime());
		this.forumId = forumId;
		this.commentId = commentId;
	}

	public String getForumId() {
		return forumId;
	}

	public void setForumId(String forumId) {
		this.forumId = forumId;
	}

	public String getCommentId() {
		return commentId;
	}

	public void setCommentId(String commentId) {
		this.commentId = commentId;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Remove Comment request *****")
			.append("\nforum_id: ").append(this.forumId)
			.append("\ncomment_id: ").append(this.commentId)
			.toString();
	}
}

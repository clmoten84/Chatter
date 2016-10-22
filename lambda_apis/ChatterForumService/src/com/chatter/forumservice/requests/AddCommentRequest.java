package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * AddCommentRequest
 * @author coreym
 *
 * Encapsulates service request data to fulfill the addition
 * of a comment to a Forum instance that exists in the database.
 * 
 * forumId (String): the id of the Forum instance to update
 * commentId (String): the id of the Comment instance to add to Forum instance
 */
public class AddCommentRequest extends Request{
	
	private String forumId;
	private String commentId;
	
	public AddCommentRequest() { super(); }
	
	public AddCommentRequest(String forumId, String commentId) {
		super(ServiceOperations.ADD_COMMENT, new Date().getTime());
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
			.append("***** Add Comment request *****")
			.append("forum_id: ").append(this.forumId)
			.append("comment_id: ").append(this.commentId)
			.toString();
	}
}

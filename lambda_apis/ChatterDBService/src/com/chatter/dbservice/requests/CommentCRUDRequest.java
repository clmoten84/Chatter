package com.chatter.dbservice.requests;

import java.util.Date;
import java.util.Map;

import com.chatter.dbservice.util.ops.ChatterCommentOps;

/**
 * CommentCRUDRequest
 * @author coreym
 *
 * Encapsulates incoming requests related to Chatter Comment data
 */
public class CommentCRUDRequest {

	private ChatterCommentOps operation;
	private Long reqDate;
	private Map<String, ?> args;
	
	public CommentCRUDRequest() { 
		this.reqDate = new Date().getTime();
	}
	
	public CommentCRUDRequest(ChatterCommentOps operation, Map<String, ?> args) {
		this.operation = operation;
		this.reqDate = new Date().getTime();
		this.args = args;
	}

	public ChatterCommentOps getOperation() {
		return operation;
	}

	public void setOperation(ChatterCommentOps operation) {
		this.operation = operation;
	}

	public Long getReqDate() {
		return reqDate;
	}

	public void setReqDate(Long reqDate) {
		this.reqDate = reqDate;
	}

	public Map<String, ?> getArgs() {
		return args;
	}

	public void setArgs(Map<String, ?> args) {
		this.args = args;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("***** Request Details (Comment) *****");
		builder.append("\nOperation: ").append(this.operation.toString());
		builder.append("\nRequest Date: ").append(new Date(this.reqDate).toString());
		builder.append("\nArguments:");
		
		if (this.args != null && !args.isEmpty()) {
			for (String key : args.keySet()) {
				builder.append("\n\t").append(key).append(": ").append(this.args.get(key));
			}
		}
		else {
			builder.append("\n\tNo args found in request...");
		}
		return builder.toString();
	}
}

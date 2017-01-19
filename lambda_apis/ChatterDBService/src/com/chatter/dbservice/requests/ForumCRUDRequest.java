package com.chatter.dbservice.requests;

import java.util.Date;
import java.util.Map;

import com.chatter.dbservice.util.ops.ChatterForumOps;

/**
 * ForumCRUDRequest
 * @author coreym
 *
 * Encapsulates incoming requests related to Chatter Forum data
 */
public class ForumCRUDRequest {

	private ChatterForumOps operation;
	private Long reqDate;
	private Map<String, String> args;
	
	public ForumCRUDRequest() { }
	
	public ForumCRUDRequest(ChatterForumOps operation, Long reqDate, 
			Map<String, String> args) {
		this.operation = operation;
		this.reqDate = reqDate;
		this.args = args;
	}

	public ChatterForumOps getOperation() {
		return operation;
	}

	public void setOperation(ChatterForumOps operation) {
		this.operation = operation;
	}

	public Long getReqDate() {
		return reqDate;
	}

	public void setReqDate(Long reqDate) {
		this.reqDate = reqDate;
	}

	public Map<String, String> getArgs() {
		return args;
	}

	public void setArgs(Map<String, String> args) {
		this.args = args;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("***** Request Details (Forum) *****");
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

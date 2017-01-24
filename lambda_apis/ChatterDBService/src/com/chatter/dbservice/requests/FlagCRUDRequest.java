package com.chatter.dbservice.requests;

import java.util.Date;
import java.util.Map;

import com.chatter.dbservice.util.ops.ChatterFlagOps;

/**
 * FlagCRUDRequest
 * @author coreym
 *
 * Encapsulates incoming requests related to Chatter Flag data
 */
public class FlagCRUDRequest {

	private ChatterFlagOps operation;
	private Long reqDate;
	private Map<String, ?> args;
	
	public FlagCRUDRequest() { 
		this.reqDate = new Date().getTime();
	}
	
	public FlagCRUDRequest(ChatterFlagOps operation, Map<String, ?> args) {
		this.operation = operation;
		this.reqDate = new Date().getTime();
		this.args = args;
	}

	public ChatterFlagOps getOperation() {
		return operation;
	}

	public void setOperation(ChatterFlagOps operation) {
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
		builder.append("***** Request Details (Flag) *****");
		builder.append("\nOperation: ").append(this.operation.toString());
		builder.append("\nRequest Date: ").append(new Date(this.reqDate).toString());
		builder.append("Arguments:");
		
		if (this.args != null && !this.args.isEmpty()) {
			for (String key : this.args.keySet()) {
				builder.append("\n\t").append(key).append(": ").append(this.args.get(key));
			}
		}
		else {
			builder.append("\n\tNo args found in request...");
		}
		return builder.toString();
	}
}

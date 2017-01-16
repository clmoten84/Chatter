package com.chatter.commentservice.requests;

import java.util.Date;
import java.util.Map;

import com.chatter.commentservice.utils.ServiceOperations;

/**
 * Request
 * @author coreym
 *
 * Encapsulates service request data
 * 
 * operation (ServiceOperations): the operation to perform
 * reqDate (Long): the date this request was made
 * args (Map<String, String>): map of request specific arguments
 */
public class Request {

	private ServiceOperations operation;
	private Long reqDate;
	private Map<String, String> args;
	
	public Request() { }
	
	public Request(ServiceOperations operation, Long reqDate, 
				Map<String, String> args) {
		this.operation = operation;
		this.reqDate = reqDate;
		this.args = args;
	}

	public ServiceOperations getOperation() {
		return operation;
	}

	public void setOperation(ServiceOperations operation) {
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
		builder.append("***** CHATTER COMMENT SERVICE REQUEST *****");
		builder.append("\nOperation: ").append(this.operation.toString());
		builder.append("\nRequest Date: ").append(new Date(this.reqDate).toString());
		builder.append("\nArguments:");
		
		if (this.args != null) {
			for (String key : this.args.keySet()) {
				builder.append("\n\t").append(key).append(": ").append(this.args.get(key));
			}
		}
		else {
			builder.append("\nNo arguments found in request.");
		}
		
		return builder.toString();
	}
}

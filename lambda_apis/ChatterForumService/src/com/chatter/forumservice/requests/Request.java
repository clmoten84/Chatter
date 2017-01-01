package com.chatter.forumservice.requests;

import java.util.Map;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * Request
 * @author coreym
 * 
 * Request class that encapsulates service request data.
 * 
 * operation (ServiceOperation): the service operation to perform
 * reqDate (Long): the date this request was made
 * args (Map<String, String>): map of request specific arguments
 *
 */
public class Request {

	private ServiceOperations operation;
	private Long reqDate;
	private Map<String, String> args;
	
	public Request() {}
	
	public Request(ServiceOperations operation, Long reqDate) {
		this.operation = operation;
		this.reqDate = reqDate;
	}
	
	public Request(ServiceOperations operation, Long reqDate, Map<String, String> args) {
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
}

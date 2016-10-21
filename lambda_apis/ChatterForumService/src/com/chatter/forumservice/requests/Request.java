package com.chatter.forumservice.requests;

import com.chatter.forumservice.util.ServiceOperation;

/**
 * Request
 * @author coreym
 * 
 * Abstract request class that encapsulates general request info.
 * All service request objects extend from this class.
 * 
 * operation (ServiceOperation): the service operation to perform
 * requestDate (Long): the date this request was made
 *
 */
public abstract class Request {

	private ServiceOperation operation;
	private Long reqDate;
	
	public Request() {}
	
	public Request(ServiceOperation operation, Long reqDate) {
		this.operation = operation;
		this.reqDate = reqDate;
	}
	
	public ServiceOperation getOperation() {
		return operation;
	}
	
	public void setOperation(ServiceOperation operation) {
		this.operation = operation;
	}
	
	public Long getReqDate() {
		return reqDate;
	}
	
	public void setReqDate(Long reqDate) {
		this.reqDate = reqDate;
	}
}

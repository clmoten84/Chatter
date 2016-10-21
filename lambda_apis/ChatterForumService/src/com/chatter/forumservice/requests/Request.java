package com.chatter.forumservice.requests;

import com.chatter.forumservice.util.ServiceOperations;

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

	private ServiceOperations operation;
	private Long reqDate;
	
	public Request() {}
	
	public Request(ServiceOperations operation, Long reqDate) {
		this.operation = operation;
		this.reqDate = reqDate;
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
}

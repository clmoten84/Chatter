package com.chatter.fileservice.requests;

import java.util.Date;
import java.util.Map;

import com.chatter.fileservice.util.ServiceOps;

/**
 * ServiceRequest
 * @author coreym
 *
 * Encapsulates request data sent from client to process
 * in the service.
 */
public class ServiceRequest {

	private ServiceOps operation;
	private Long reqDate;
	private Map<String, ?> args;
	private byte[] fileData;
	
	public ServiceRequest() { }
	
	public ServiceRequest(ServiceOps operation, Long reqDate, 
			Map<String, ?> args, byte[] fileData) {
		this.operation = operation;
		this.reqDate = reqDate;
		this.args = args;
		this.fileData = fileData;
	}

	public ServiceOps getOperation() {
		return operation;
	}

	public void setOperation(ServiceOps operation) {
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
	
	public byte[] getFileData() {
		return fileData;
	}
	
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("***** Request Details (FileService) *****");
		builder.append("\nOperation: ").append(this.operation.toString());
		builder.append("\nRequest Date: ").append(new Date(this.reqDate).toString());
		builder.append("\nArguments: ");
		
		if (this.args != null && !args.isEmpty()) {
			for (String key : args.keySet()) {
				builder.append("\n\t").append(key).append(": ").append(this.args.get(key));
			}
		}
		else {
			builder.append("\n\tNo args found in request...");
		}
		
		if (this.fileData != null) {
			builder.append("\n\tFile Data size: ").append(this.fileData.length);
		}
		else {
			builder.append("\n\tNo file data included in request...");
		}
		return builder.toString();
	}
}

package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * PingRequest
 * @author coreym
 *
 * Encapsulates a request that simply pings the ChatterForumService
 * to ensure the service is available.
 */
public class PingRequest extends Request{
	
	public PingRequest() {
		super();
	}
	
	public PingRequest(String ping) {
		super(ServiceOperations.PING, new Date().getTime());
	}
	
	public String toString() {
		return "***** Ping Service Request *****";
	}
}

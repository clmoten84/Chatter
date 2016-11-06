package com.chatter.forumservice.requests;

import java.util.Date;

import com.chatter.forumservice.util.ServiceOperations;

/**
 * QueryByCreatorRequest
 * @author coreym
 *
 * Encapsulates service request to query Chatter_Forum table using
 * created_by_index global secondary index.
 * 
 * created_by (String): the id of the entity that created this Forum
 * 						instance.
 */
public class QueryByCreatorRequest extends Request{

	private String createdBy;
	
	public QueryByCreatorRequest() { super(); }
	
	public QueryByCreatorRequest(String createdBy) {
		super(ServiceOperations.QUERY_BY_CREATOR, new Date().getTime());
		this.createdBy = createdBy;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Query By Creator request *****")
			.append("created_by: ").append(this.createdBy)
			.toString();
	}
}

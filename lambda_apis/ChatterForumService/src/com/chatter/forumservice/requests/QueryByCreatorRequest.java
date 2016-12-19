package com.chatter.forumservice.requests;

import java.util.Date;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
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
	private Long timeStampFrom;
	private Long timeStampTo;
	private Map<String, AttributeValue> exclusiveStartKey;
	
	public QueryByCreatorRequest() { super(); }
	
	public QueryByCreatorRequest(String createdBy, Map<String, AttributeValue> exclusiveStartKey,
			Long timeStampFrom, Long timeStampTo) {
		super(ServiceOperations.QUERY_BY_CREATOR, new Date().getTime());
		this.createdBy = createdBy;
		this.exclusiveStartKey = exclusiveStartKey;
		this.timeStampFrom = timeStampFrom;
		this.timeStampTo = timeStampTo;
	}
	
	public String getCreatedBy() {
		return this.createdBy;
	}
	
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	
	public Map<String, AttributeValue> getExclusiveStartKey() {
		return exclusiveStartKey;
	}

	public void setExclusiveStartKey(Map<String, AttributeValue> exclusiveStartKey) {
		this.exclusiveStartKey = exclusiveStartKey;
	}

	public Long getTimeStampFrom() {
		return timeStampFrom;
	}

	public void setTimeStampFrom(Long timeStampFrom) {
		this.timeStampFrom = timeStampFrom;
	}

	public Long getTimeStampTo() {
		return timeStampTo;
	}

	public void setTimeStampTo(Long timeStampTo) {
		this.timeStampTo = timeStampTo;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Query By Creator request *****")
			.append("\ncreated_by: ").append(this.createdBy)
			.append("\ntime_stamp_from: ").append(this.timeStampFrom)
			.append("\ntime_stamp_to: ").append(this.timeStampTo)
			.toString();
	}
}

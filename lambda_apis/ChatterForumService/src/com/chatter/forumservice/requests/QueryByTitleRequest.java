package com.chatter.forumservice.requests;

import java.util.Date;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chatter.forumservice.util.ServiceOperations;

/**
 * QueryByTitleRequest
 * @author coreym
 *
 * Encapsulates service request to query the Chatter_Forum table
 * using title_index global secondary index.
 * 
 * title (String): the title of the Forum instance to query for
 */
public class QueryByTitleRequest extends Request{

	private String title;
	private Map<String, AttributeValue> exclusiveStartKey;
	
	public QueryByTitleRequest() { super(); }
	
	public QueryByTitleRequest(String title, Map<String, AttributeValue> exclusiveStartKey) {
		super(ServiceOperations.QUERY_BY_TITLE, new Date().getTime());
		this.title = title;
		this.exclusiveStartKey = exclusiveStartKey;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public Map<String, AttributeValue> getExclusiveStartKey() {
		return exclusiveStartKey;
	}

	public void setExclusiveStartKey(Map<String, AttributeValue> exclusiveStartKey) {
		this.exclusiveStartKey = exclusiveStartKey;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Query By Title request *****")
			.append("\ntitle: ").append(this.title)
			.toString();
	}
}

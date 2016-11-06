package com.chatter.forumservice.requests;

import java.util.Date;

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
	
	public QueryByTitleRequest() { super(); }
	
	public QueryByTitleRequest(String title) {
		super(ServiceOperations.QUERY_BY_TITLE, new Date().getTime());
		this.title = title;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	@Override
	public String toString() {
		return new StringBuilder()
			.append("***** Query By Title request *****")
			.append("title: ").append(this.title)
			.toString();
	}
}

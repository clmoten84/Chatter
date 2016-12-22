package com.chatter.forumservice.responses;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chatter.model.ChatterForum;

/**
 * Encapsulates query page results for return to client
 * @author coreym
 *
 * pageResults: the list of ChatterForum objects in this page of query results
 * lastEvaluatedKey: used to retrieve the next page of results in a subsequent request
 * resultCount: the count of ChatterForum objects returned in response
 */
public class ForumResultPage {
	private List<ChatterForum> pageResults;
	private Map<String, AttributeValue> lastEvaluatedKey;
	private int resultCount;
	private boolean moreResults;
	
	public ForumResultPage() { }
	
	public ForumResultPage(List<ChatterForum> pageResults,
			Map<String, AttributeValue> lastEvaluatedKey, int resultCount) {
		this.pageResults = pageResults;
		this.lastEvaluatedKey = lastEvaluatedKey;
		this.resultCount = resultCount;
		
		// If the lastEvalutedKey is null, that indicates that the
		// query or scan result set is complete. If lastEvaluatedKey
		// is not null, then there are more results to retrieve from
		// the database.
		if(lastEvaluatedKey != null)
			moreResults = true;
		else
			moreResults = false;
	}

	public List<ChatterForum> getPageResults() {
		return pageResults;
	}

	public void setPageResults(List<ChatterForum> pageResults) {
		this.pageResults = pageResults;
	}

	public Map<String, AttributeValue> getLastEvaluatedKey() {
		return lastEvaluatedKey;
	}

	public void setLastEvaluatedKey(Map<String, AttributeValue> lastEvaluatedKey) {
		this.lastEvaluatedKey = lastEvaluatedKey;
	}

	public int getResultCount() {
		return resultCount;
	}

	public void setResultCount(int resultCount) {
		this.resultCount = resultCount;
	}
	
	public boolean getMoreResults() {
		return moreResults;
	}
	
	public void setMoreResults(boolean moreResults) {
		this.moreResults = moreResults;
	}
}

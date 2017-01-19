package com.chatter.dbservice.responses;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chatter.dbservice.model.ChatterForum;

/**
 * ForumResultPage
 * @author coreym
 *
 * Encapsulates Chatter Forum query results:
 * 
 * pageResults: the list of forum objects for this page of results
 * lastEvaluatedKey: used to retrieve next page of results in a subsequent request
 * resultCount: the count of results returned in this page of results
 * moreResults: flag indicating whether there are more results to retrieve
 * 				to satisfy query
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
		if (lastEvaluatedKey != null) {
			this.moreResults = true;
		}
		else {
			this.moreResults = false;
		}
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

	public boolean isMoreResults() {
		return moreResults;
	}

	public void setMoreResults(boolean moreResults) {
		this.moreResults = moreResults;
	}
}

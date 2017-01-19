package com.chatter.dbservice.responses;

import java.util.List;
import java.util.Map;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chatter.dbservice.model.ChatterComment;

/**
 * CommentResultPage
 * @author coreym
 * 
 * Encapsulates Chatter Comment query results:
 * 
 * pageResults: the list of comments objects for this page of results
 * lastEvaluatedKey: used to retrieve next page of results in a subsequent request
 * resultCount: the count of results returned in this page of results
 * moreResults: flag indicating whether there are more results to retrieve
 * 				to satisfy query
 *
 */
public class CommentResultPage {
	
	private List<ChatterComment> pageResults;
	private Map<String, AttributeValue> lastEvaluatedKey;
	private int resultCount;
	private boolean moreResults;
	
	public CommentResultPage() { }
	
	public CommentResultPage(List<ChatterComment> pageResults, 
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

	public List<ChatterComment> getPageResults() {
		return pageResults;
	}

	public void setPageResults(List<ChatterComment> pageResults) {
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

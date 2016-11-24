package com.chatter.forumservice;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;
import com.chatter.forumservice.handler.ChatterForumServiceRequestHandler;
import com.chatter.forumservice.requests.AddCommentRequest;
import com.chatter.forumservice.requests.CreateForumRequest;
import com.chatter.forumservice.requests.DeleteForumRequest;
import com.chatter.forumservice.requests.QueryByCreatorRequest;
import com.chatter.forumservice.requests.QueryByTitleRequest;
import com.chatter.forumservice.requests.RetrieveForumRequest;
import com.chatter.forumservice.requests.UpdateForumRequest;
import com.chatter.model.ChatterForum;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ChatterForumServiceRequestHandlerTest {

    /*
     * Globals
     */
	private ChatterForumServiceRequestHandler handler = new ChatterForumServiceRequestHandler();
	private TestContext testCtx = new TestContext();
	private ChatterForum forum;
	private List<ChatterForum> forumList;
	
	/**
	 * Create and return a test Chatter forum 
	 * @return ChatterForum
	 */
	private ChatterForum createForum() {
		ChatterForum forum = new ChatterForum();
		forum.setTitle("Just a Test Forum");
		forum.setCreatedBy("Conde Nast");
		forum.setTimeStamp(new Date().getTime());
		forum.setCommentIds(new HashSet<String>());
		return forum;
	}
	
	/************** Sample Request generation ************/
	
	/**
	 * Generate a sample CreateForumRequest object
	 * @param forumIn
	 * @return CreateForumRequest
	 */
	private CreateForumRequest generateCreateForumRequest(ChatterForum forumIn) {
		CreateForumRequest request = new CreateForumRequest(forumIn.getCreatedBy(), 
				forumIn.getTitle());
		return request;
	}
	
	/**
	 * Generate a sample RetrieveForumRequest object
	 * @param forumId
	 * @return RetrieveForumRequest
	 */
	private RetrieveForumRequest generateRetrieveForumRequest(String forumId) {
		return new RetrieveForumRequest(forumId);
	}
	
	/**
	 * Generate a sample QueryByCreatorRequest object
	 * @param createdBy
	 * @return QueryByCreatorRequest
	 */
	private QueryByCreatorRequest generateQueryByCreatorRequest(String createdBy) {
		//Set date conditions to be from two weeks ago to right now
		final long DAYS_IN_MS = 1000 * 60 * 60 * 24;
		long rightNow = new Date().getTime();
		long twoWeeksAgo = rightNow - (14 * DAYS_IN_MS);
		return new QueryByCreatorRequest("Conde Nast", null, twoWeeksAgo, rightNow);
	}
	
	/**
	 * Generate a sample QueryByTitleRequest object
	 * @param title
	 * @return QueryByTitleRequest
	 */
	private QueryByTitleRequest generateQueryByTitleRequest(String title) {
		return new QueryByTitleRequest("Just a Test Forum", null);
	}
	
	/**
	 * Generate a sample UpdateForumRequest object
	 * @param forumId
	 * @return UpdateForumRequest
	 */
	private UpdateForumRequest generateUpdateForumRequest(String forumId) {
		return new UpdateForumRequest(forumId, "I just modified this forum name");
	}
	
	/**
	 * Generate a sample AddCommentRequest object
	 * @param forumId
	 * @return AddCommentRequest
	 */
	private AddCommentRequest generateAddCommentRequest(String forumId) {
		return new AddCommentRequest(forumId, "1234-TEST");
	}
	
	/**
	 * Generate a sample DeleteForumRequest object
	 * @param forumId
	 * @return DeleteForumRequest
	 */
	private DeleteForumRequest generateDeleteForumRequest(String forumId) {
		return new DeleteForumRequest(forumId);
	}

	/****************** Tests *****************/
	/**
	 * Test creating and saving a new forum object to DB
	 */
	@Test
	public void testCreateForumRequest() {
		
	}
	
	/**
	 * Test retrieving a forum object from DB using forum id
	 */
	@Test
	public void testRetrieveForumRequest() {
		
	}
	
	/**
	 * Test querying DB for forum objects using createdBy attribute
	 */
	@Test
	public void testQueryByCreatorRequest() {
		
	}
	
	/**
	 * Test querying DB for forum objects using title attribute
	 */
	@Test
	public void testQueryByTitleRequest() {
		
	}
	
	/**
	 * Test updating a forum object in DB
	 */
	@Test
	public void testUpdateForumRequest() {
		
	}
	
	/**
	 * Test adding a comment (comment id) to a forum object in DB
	 */
	@Test
	public void testAddCommentRequest() {
		
	}
	
	/**
	 * Test deleting a forum object from DB
	 */
	@Test
	public void testDeleteForumRequest() {
		
	}
}

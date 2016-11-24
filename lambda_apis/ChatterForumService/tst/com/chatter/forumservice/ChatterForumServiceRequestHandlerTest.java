package com.chatter.forumservice;

import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.chatter.forumservice.handler.ChatterForumServiceRequestHandler;
import com.chatter.forumservice.requests.AddCommentRequest;
import com.chatter.forumservice.requests.CreateForumRequest;
import com.chatter.forumservice.requests.DeleteForumRequest;
import com.chatter.forumservice.requests.ForumServiceRequest;
import com.chatter.forumservice.requests.PingRequest;
import com.chatter.forumservice.requests.QueryByCreatorRequest;
import com.chatter.forumservice.requests.QueryByTitleRequest;
import com.chatter.forumservice.requests.RetrieveForumRequest;
import com.chatter.forumservice.requests.UpdateForumRequest;
import com.chatter.forumservice.responses.ForumServiceResponse;
import com.chatter.forumservice.responses.ServicePropsResponse;
import com.chatter.forumservice.util.ServiceMessages;
import com.chatter.forumservice.util.ServiceOperations;
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
	
	/**
	 * Generate a sample PingRequest object
	 * @return PingRequest
	 */
	private PingRequest generatePingRequest() {
		return new PingRequest();
	}

	/****************** Tests *****************/
	
	/**
	 * Test a ping request to this service. Should return information
	 * about this service wrapped in an object wrapper.
	 */
	public void testPingRequest() {
		System.out.println("ChatterForumService test: testPingRequest");
		this.testCtx.setFunctionName(ServiceOperations.PING.toString());
		
		ForumServiceRequest<PingRequest> request = new ForumServiceRequest<>();
		request.setData(this.generatePingRequest());
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ServicePropsResponse> response = 
			(ForumServiceResponse<ServicePropsResponse>) handler.handleRequest(request, 
					this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS, response.getMessage());
	}
	
	/**
	 * Test creating and saving a new forum object to DB
	 */
	@Test
	public void testCreateForumRequest() {
		System.out.println("ChatterForumService test: testCreateForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.CREATE.toString());
		
		ForumServiceRequest<CreateForumRequest> request = new ForumServiceRequest<>();
		request.setData(this.generateCreateForumRequest(this.createForum()));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ChatterForum> response = (ForumServiceResponse<ChatterForum>)
			handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS, response.getMessage());
		
		if (response.getPayload() != null) {
			this.forum = response.getPayload();
			Assert.assertNotNull(this.forum.getForumId());
			Assert.assertEquals("Just a Test Forum", this.forum.getTitle());
			Assert.assertEquals("Conde Nast", this.forum.getCreatedBy());
			Assert.assertNotNull(this.forum.getCommentIds());
		}
	}
	
	/**
	 * Test retrieving a forum object from DB using forum id
	 */
	@Test
	public void testRetrieveForumRequest() {
		System.out.println("ChatterForumService test: testRetrieveForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.QUERY_BY_ID.toString());
	}
	
	/**
	 * Test querying DB for forum objects using createdBy attribute
	 */
	@Test
	public void testQueryByCreatorRequest() {
		System.out.println("ChatterForumService test: testQueryByCreatorRequest");
		this.testCtx.setFunctionName(ServiceOperations.QUERY_BY_CREATOR.toString());
	}
	
	/**
	 * Test querying DB for forum objects using title attribute
	 */
	@Test
	public void testQueryByTitleRequest() {
		System.out.println("ChatterForumService test: testQueryByTitleRequest");
		this.testCtx.setFunctionName(ServiceOperations.QUERY_BY_TITLE.toString());
	}
	
	/**
	 * Test updating a forum object in DB
	 */
	@Test
	public void testUpdateForumRequest() {
		System.out.println("ChatterForumService test: testUpdateForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.UPDATE.toString());
	}
	
	/**
	 * Test adding a comment (comment id) to a forum object in DB
	 */
	@Test
	public void testAddCommentRequest() {
		System.out.println("ChatterForumService test: testAddCommentRequest");
		this.testCtx.setFunctionName(ServiceOperations.ADD_COMMENT.toString());
	}
	
	/**
	 * Test deleting a forum object from DB
	 */
	@Test
	public void testDeleteForumRequest() {
		System.out.println("ChatterForumService test: testDeleteForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.DELETE.toString());
	}
}

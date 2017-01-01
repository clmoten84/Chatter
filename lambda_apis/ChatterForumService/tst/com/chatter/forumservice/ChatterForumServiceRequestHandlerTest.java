package com.chatter.forumservice;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.chatter.forumservice.handler.ChatterForumServiceRequestHandler;
import com.chatter.forumservice.model.ChatterForum;
import com.chatter.forumservice.requests.ForumServiceRequest;
import com.chatter.forumservice.requests.Request;
import com.chatter.forumservice.responses.ForumResultPage;
import com.chatter.forumservice.responses.ForumServiceResponse;
import com.chatter.forumservice.responses.ServicePropsResponse;
import com.chatter.forumservice.util.ServiceMessages;
import com.chatter.forumservice.util.ServiceOperations;

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
	
	/**
	 * Create and return a test Chatter forum 
	 * @return ChatterForum
	 */
	private ChatterForum createForum() {
		ChatterForum forum = new ChatterForum();
		forum.setTitle("Just a Test Forum");
		forum.setCreatedBy("Conde Nast");
		forum.setTimeStamp(new Date().getTime());
		return forum;
	}
	
	/************** Sample Request generation ************/
	
	/**
	 * Generate a sample Request to create a new Forum object
	 * and save to the database.
	 * 
	 * @param forumIn
	 * @return Request
	 */
	private Request generateCreateForumRequest(ChatterForum forumIn) {
		Request request = new Request();
		request.setOperation(ServiceOperations.CREATE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", forumIn.getCreatedBy());
		args.put("title", forumIn.getTitle());
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample Request to retrieve a Forum object
	 * from the database.
	 * 
	 * @param forumId
	 * @return Request
	 */
	private Request generateRetrieveForumRequest(String forumId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.QUERY_BY_ID);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample Request to query the Forum table using
	 * the created_by attribute value.
	 * 
	 * @param createdBy
	 * @return Request
	 */
	private Request generateQueryByCreatorRequest(String createdBy) {
		Request request = new Request();
		request.setOperation(ServiceOperations.QUERY_BY_CREATOR);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", createdBy);
		
		// Set date conditions to be from two weeks ago to right now
		final long DAYS_IN_MS = 1000 * 60 * 60 *24;
		Long rightNow = new Date().getTime();
		Long twoWeeksAgo = rightNow - (14 * DAYS_IN_MS);
		args.put("timeStampTo", rightNow.toString());
		args.put("timeStampFrom", twoWeeksAgo.toString());
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample Request to query the Forum table using 
	 * the title attribute.
	 * 
	 * @param title
	 * @return Request
	 */
	private Request generateQueryByTitleRequest(String title) {
		Request request = new Request();
		request.setOperation(ServiceOperations.QUERY_BY_TITLE);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("title", title);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a Request to update an existing Forum object
	 * in the database.
	 * 
	 * @param forumId
	 * @return Request
	 */
	private Request generateUpdateForumRequest(String forumId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.UPDATE);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		args.put("titleUpdate", "I just modified this forum title");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample Request to add a comment id to a Forum
	 * object in the database.
	 * 
	 * @param forumId
	 * @return Request
	 */
	private Request generateAddCommentRequest(String forumId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.ADD_COMMENT);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		args.put("commentId", "1234-TEST");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample Request to remove a comment id from a Forum
	 * object in the database.
	 * 
	 * @param forumId
	 * @return Request
	 */
	private Request generateRemoveCommentRequest(String forumId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.REMOVE_COMMENT);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		args.put("commentId", "1234-TEST");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample Request to delete a Forum object
	 * from the database.
	 * 
	 * @param forumId
	 * @return Request
	 */
	private Request generateDeleteForumRequest(String forumId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.DELETE);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample Request to ping the service and retrieve
	 * service information.
	 * 
	 * @return Request
	 */
	private Request generatePingRequest() {
		Request request = new Request();
		request.setOperation(ServiceOperations.PING);
		request.setReqDate(new Date().getTime());
		return request;
	}

	/****************** Tests *****************/
	
	// I need to execute tests in a specific order since creating and 
	// saving a new Forum object generates the forumId for the object. 
	// This forumId is used in several of the other tests here. So I
	// need that test to execute before the other tests. To accomplish
	// this I am running the tests in a "test suite" so that I can control
	// the order of execution of the test methods. I know this violates
	// JUnit testing principles, but...WTF.
	
	@Test
	public void runChatterForumServiceTests() {
		this.testPingRequest();
		this.testCreateForumRequest();
		this.testRetrieveForumRequest();
		this.testQueryByCreatorRequest();
		this.testQueryByTitleRequest();
		this.testUpdateForumRequest();
		this.testAddCommentRequest();
		this.testRemoveCommentRequest();
		this.testDeleteForumRequest();
	}
	
	/**
	 * Test a ping request to this service. Should return information
	 * about this service wrapped in an object wrapper.
	 */
	private void testPingRequest() {
		System.out.println("ChatterForumService test: testPingRequest");
		this.testCtx.setFunctionName(ServiceOperations.PING.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
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
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			System.out.println(response.getPayload().toString());
		}
	}
	
	/**
	 * Test creating and saving a new forum object to DB
	 */
	private void testCreateForumRequest() {
		System.out.println("ChatterForumService test: testCreateForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.CREATE.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateCreateForumRequest(this.createForum()));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ChatterForum> response = (ForumServiceResponse<ChatterForum>)
			handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			this.forum = response.getPayload();
			Assert.assertNotNull(this.forum.getForumId());
			Assert.assertEquals("Just a Test Forum", this.forum.getTitle());
			Assert.assertEquals("Conde Nast", this.forum.getCreatedBy());
		}
	}
	
	/**
	 * Test retrieving a forum object from DB using forum id
	 */
	private void testRetrieveForumRequest() {
		System.out.println("ChatterForumService test: testRetrieveForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.QUERY_BY_ID.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateRetrieveForumRequest(this.forum.getForumId()));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ChatterForum> response = (ForumServiceResponse<ChatterForum>)
			handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			Assert.assertEquals("Just a Test Forum", response.getPayload().getTitle());
			Assert.assertEquals("Conde Nast", response.getPayload().getCreatedBy());
		}
	}
	
	/**
	 * Test querying DB for forum objects using createdBy attribute
	 */
	public void testQueryByCreatorRequest() {
		System.out.println("ChatterForumService test: testQueryByCreatorRequest");
		this.testCtx.setFunctionName(ServiceOperations.QUERY_BY_CREATOR.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateQueryByCreatorRequest("Conde Nast"));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ForumResultPage> response = (ForumServiceResponse<ForumResultPage>)
				handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		Assert.assertNotNull(response.getPayload());
		
		if (response.getPayload() != null) {
			ForumResultPage page = response.getPayload();
			
			// Assert result page details
			Assert.assertTrue(page.getResultCount() > 0);
			Assert.assertNull(page.getLastEvaluatedKey());
			Assert.assertFalse(page.getMoreResults());
			
			// Assert actual retrieved forum details
			List<ChatterForum> forums = page.getPageResults();
			Assert.assertNotNull(forums);
			Assert.assertFalse(forums.isEmpty());
			Assert.assertEquals(this.forum.getCreatedBy(), forums.get(0).getCreatedBy());
		}
	}
	
	/**
	 * Test querying DB for forum objects using title attribute
	 */
	public void testQueryByTitleRequest() {
		System.out.println("ChatterForumService test: testQueryByTitleRequest");
		this.testCtx.setFunctionName(ServiceOperations.QUERY_BY_TITLE.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateQueryByTitleRequest("Just a Test Forum"));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ForumResultPage> response = (ForumServiceResponse<ForumResultPage>)
				handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		Assert.assertNotNull(response.getPayload());
		
		if (response.getPayload() != null) {
			ForumResultPage page = response.getPayload();
			
			// Assert result page details
			Assert.assertTrue(page.getResultCount() > 0);
			Assert.assertNull(page.getLastEvaluatedKey());
			Assert.assertFalse(page.getMoreResults());
			
			// Assert actual retrieved forum details
			List<ChatterForum> forums = page.getPageResults();
			Assert.assertNotNull(forums);
			Assert.assertFalse(forums.isEmpty());
			Assert.assertEquals(this.forum.getTitle(), forums.get(0).getTitle());
		}
	}
	
	/**
	 * Test updating a forum object in DB
	 */
	public void testUpdateForumRequest() {
		System.out.println("ChatterForumService test: testUpdateForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.UPDATE.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateUpdateForumRequest(this.forum.getForumId()));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ChatterForum> response = (ForumServiceResponse<ChatterForum>)
			handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			Assert.assertEquals(this.forum.getForumId(), response.getPayload().getForumId());
			Assert.assertEquals("I just modified this forum title", 
					response.getPayload().getTitle());
			Assert.assertEquals("Conde Nast", response.getPayload().getCreatedBy());
		}
	}
	
	/**
	 * Test adding a comment (comment id) to a forum object in DB
	 */
	public void testAddCommentRequest() {
		System.out.println("ChatterForumService test: testAddCommentRequest");
		this.testCtx.setFunctionName(ServiceOperations.ADD_COMMENT.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateAddCommentRequest(this.forum.getForumId()));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ChatterForum> response = (ForumServiceResponse<ChatterForum>)
			handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			Assert.assertEquals(this.forum.getForumId(), response.getPayload().getForumId());
			Assert.assertTrue(response.getPayload().getCommentIds().size() > 0);
		}
	}
	
	/**
	 * Test removing a comment (comment id) from a forum object in Db
	 */
	public void testRemoveCommentRequest() {
		System.out.println("ChatterForumService test: testRemoveCommentRequest");
		this.testCtx.setFunctionName(ServiceOperations.REMOVE_COMMENT.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateRemoveCommentRequest(this.forum.getForumId()));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ChatterForum> response = (ForumServiceResponse<ChatterForum>)
			handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			Assert.assertEquals(this.forum.getForumId(), response.getPayload().getForumId());
			Assert.assertNull(response.getPayload().getCommentIds());
		}
	}
	
	/**
	 * Test deleting a forum object from DB
	 */
	public void testDeleteForumRequest() {
		System.out.println("ChatterForumService test: testDeleteForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.DELETE.toString());
		
		ForumServiceRequest<Request> request = new ForumServiceRequest<>();
		request.setData(this.generateDeleteForumRequest(this.forum.getForumId()));
		
		@SuppressWarnings("unchecked")
		ForumServiceResponse<ChatterForum> response = (ForumServiceResponse<ChatterForum>)
			handler.handleRequest(request, this.testCtx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
	}
}

package com.chatter.forumservice;

import java.util.Date;
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
import com.chatter.forumservice.requests.RemoveCommentRequest;
import com.chatter.forumservice.requests.RetrieveForumRequest;
import com.chatter.forumservice.requests.UpdateForumRequest;
import com.chatter.forumservice.responses.ForumResultPage;
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
		return new UpdateForumRequest(forumId, "I just modified this forum title");
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
	 * Generate a sample RemoveCommentRequest object
	 * @param forumId
	 * @return RemoveCommentRequest
	 */
	private RemoveCommentRequest generateRemoveCommentRequest(String forumId) {
		return new RemoveCommentRequest(forumId, "1234-TEST");
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
		//this.testQueryByCreatorRequest();
		//this.testQueryByTitleRequest();
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
		
		ForumServiceRequest<RetrieveForumRequest> request = new ForumServiceRequest<>();
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
		
		ForumServiceRequest<QueryByCreatorRequest> request = new ForumServiceRequest<>();
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
			Assert.assertNotNull(page.getLastEvaluatedKey());
			Assert.assertFalse(page.getMoreResults());
			
			// Assert actual retrieved forum details
			List<ChatterForum> forums = page.getPageResults();
			Assert.assertNotNull(forums);
			Assert.assertFalse(forums.isEmpty());
			Assert.assertEquals(this.forum.getForumId(), forums.get(0).getForumId());
			Assert.assertEquals(this.forum.getCreatedBy(), forums.get(0).getCreatedBy());
		}
	}
	
	/**
	 * Test querying DB for forum objects using title attribute
	 */
	public void testQueryByTitleRequest() {
		System.out.println("ChatterForumService test: testQueryByTitleRequest");
		this.testCtx.setFunctionName(ServiceOperations.QUERY_BY_TITLE.toString());
		
		ForumServiceRequest<QueryByTitleRequest> request = new ForumServiceRequest<>();
		request.setData(this.generateQueryByTitleRequest("Conde Nast"));
		
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
			Assert.assertNotNull(page.getLastEvaluatedKey());
			Assert.assertFalse(page.getMoreResults());
			
			// Assert actual retrieved forum details
			List<ChatterForum> forums = page.getPageResults();
			Assert.assertNotNull(forums);
			Assert.assertFalse(forums.isEmpty());
			Assert.assertEquals(this.forum.getForumId(), forums.get(0).getForumId());
			Assert.assertEquals(this.forum.getTitle(), forums.get(0).getTitle());
		}
	}
	
	/**
	 * Test updating a forum object in DB
	 */
	public void testUpdateForumRequest() {
		System.out.println("ChatterForumService test: testUpdateForumRequest");
		this.testCtx.setFunctionName(ServiceOperations.UPDATE.toString());
		
		ForumServiceRequest<UpdateForumRequest> request = new ForumServiceRequest<>();
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
		
		ForumServiceRequest<AddCommentRequest> request = new ForumServiceRequest<>();
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
		
		ForumServiceRequest<RemoveCommentRequest> request = new ForumServiceRequest<>();
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
		
		ForumServiceRequest<DeleteForumRequest> request = new ForumServiceRequest<>();
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

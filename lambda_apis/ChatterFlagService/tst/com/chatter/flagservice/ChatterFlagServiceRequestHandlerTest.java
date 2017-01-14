package com.chatter.flagservice;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.chatter.flagservice.handler.ChatterFlagServiceRequestHandler;
import com.chatter.flagservice.model.ChatterFlag;
import com.chatter.flagservice.requests.FlagServiceRequest;
import com.chatter.flagservice.requests.Request;
import com.chatter.flagservice.responses.FlagServiceResponse;
import com.chatter.flagservice.responses.ServicePropsResponse;
import com.chatter.flagservice.util.ServiceMessages;
import com.chatter.flagservice.util.ServiceOperations;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class ChatterFlagServiceRequestHandlerTest {

    /************** Globals *************/
	private ChatterFlagServiceRequestHandler handler = new ChatterFlagServiceRequestHandler();
	private TestContext ctx = new TestContext();
	private ChatterFlag flag;
	
	/**
	 * Create and return a sample ChatterFlag instance
	 * @return ChatterFlag
	 */
	private ChatterFlag createSampleFlag() {
		ChatterFlag flag = new ChatterFlag();
		flag.setCreatedBy("anonymous_user");
		flag.setForumId("123456789");
		flag.setCommentId("987654321");
		flag.setFlagDescription("This comment is extremely rude and disturbing!");
		return flag;
	}
	
	/**************** Sample request generation **************/
	/**
	 * Generate a sample request to create and save a new ChatterFlag
	 * in the database.
	 * @param flagIn
	 * @return
	 */
	private Request generateCreateFlagRequest(ChatterFlag flagIn) {
		Request request = new Request();
		request.setOperation(ServiceOperations.CREATE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", flagIn.getCreatedBy());
		args.put("forumId", flagIn.getForumId());
		args.put("commentId", flagIn.getCommentId());
		args.put("flagDescription", flagIn.getFlagDescription());
		request.setArgs(args);
		
		return request;
	}
	
	/**
	 * Generate a sample request to retrieve a ChatterFlag object
	 * from the database by flag id.
	 * @param flagId
	 * @return
	 */
	private Request generateRetrieveFlagRequest(String flagId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.RETRIEVE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("flagId", flagId);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample request to update a ChatterFlag object.
	 * @param flagId
	 * @return
	 */
	private Request generateUpdateFlagRequest(String flagId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.UPDATE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("flagId", flagId);
		args.put("flagDescriptionUpdate", 
				"I changed my mind. This comment isn't that bad after all!");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample request to delete a ChatterFlag object.
	 * @param flagId
	 * @return
	 */
	private Request generateDeleteFlagRequest(String flagId) {
		Request request = new Request();
		request.setOperation(ServiceOperations.DELETE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("flagId", flagId);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate a sample request to ping this service.
	 * @return
	 */
	private Request generatePingRequest() {
		Request request = new Request();
		request.setOperation(ServiceOperations.PING);
		request.setReqDate(new Date().getTime());
		return request;
	}
	
	/******************* Tests ******************/
	
	// I need to execute tests in a specific order since creating and 
	// saving a new Flag object generates the flagId for the object. 
	// This flagId is used in several of the other tests here. So I
	// need that test to execute before the other tests. To accomplish
	// this I am running the tests in a "test suite" so that I can control
	// the order of execution of the test methods. I know this violates
	// JUnit testing principles, but...WTF.
	
	@Test
	public void runServiceTestSuite() {
		this.testPingRequest();
		this.testCreateFlagRequest();
		this.testRetrieveFlagRequest();
		this.testUpdateFlagRequest();
		this.testDeleteFlagRequest();
	}
	
	/**
	 * Tests a ping request to this service. Should return service
	 * info wrapped in an object wrapper.
	 */
	private void testPingRequest() {
		System.out.println("ChatterFlagService test: testPingRequest");
		this.ctx.setFunctionName(ServiceOperations.PING.toString());
		
		FlagServiceRequest<Request> request = new FlagServiceRequest<>();
		request.setData(this.generatePingRequest());
		
		@SuppressWarnings("unchecked")
		FlagServiceResponse<ServicePropsResponse> response = 
				(FlagServiceResponse<ServicePropsResponse>) handler.handleRequest(request, 
						ctx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			System.out.println(response.getPayload().toString());
		}
		else {
			System.out.println("Ping response payload was NULL!");
		}
	}
	
	/**
	 * Test a request to create and save a new ChatterFlag to the database
	 */
	private void testCreateFlagRequest() {
		System.out.println("ChatterFlagService test: testCreateFlagRequest");
		this.ctx.setFunctionName(ServiceOperations.CREATE.toString());
		
		FlagServiceRequest<Request> request = new FlagServiceRequest<>();
		request.setData(this.generateCreateFlagRequest(this.createSampleFlag()));
		
		@SuppressWarnings("unchecked")
		FlagServiceResponse<ChatterFlag> response =
			(FlagServiceResponse<ChatterFlag>) handler.handleRequest(request, ctx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			this.flag = response.getPayload();
			Assert.assertNotNull(this.flag.getFlagId());
			Assert.assertNotNull(this.flag.getTimeStamp());
			Assert.assertEquals("anonymous_user", this.flag.getCreatedBy());;
			Assert.assertEquals("123456789", this.flag.getForumId());
			Assert.assertEquals("987654321", this.flag.getCommentId());
			Assert.assertEquals("This comment is extremely rude and disturbing!", this.flag.getFlagDescription());
		}
	}
	
	/**
	 * Test a request to retrieve a ChatterFlag object from database using
	 * flag id value.
	 */
	private void testRetrieveFlagRequest() {
		System.out.println("ChatterFlagService test: testRetrieveFlagRequest");
		this.ctx.setFunctionName(ServiceOperations.RETRIEVE.toString());
		
		FlagServiceRequest<Request> request = new FlagServiceRequest<>();
		request.setData(this.generateRetrieveFlagRequest(this.flag.getFlagId()));
		
		@SuppressWarnings("unchecked")
		FlagServiceResponse<ChatterFlag> response = 
			(FlagServiceResponse<ChatterFlag>) handler.handleRequest(request, ctx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			Assert.assertEquals(this.flag.getFlagId(), response.getPayload().getFlagId());
		}
	}
	
	/**
	 * Test a request to update an existing ChatterFlag object in the database
	 */
	private void testUpdateFlagRequest() {
		System.out.println("ChatterFlagService test: testUpdateFlagRequest");
		this.ctx.setFunctionName(ServiceOperations.UPDATE.toString());
		
		FlagServiceRequest<Request> request = new FlagServiceRequest<>();
		request.setData(this.generateUpdateFlagRequest(this.flag.getFlagId()));
		
		@SuppressWarnings("unchecked")
		FlagServiceResponse<ChatterFlag> response = 
			(FlagServiceResponse<ChatterFlag>) handler.handleRequest(request, ctx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertNotNull(response.getPayload());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
		
		if (response.getPayload() != null) {
			Assert.assertEquals(this.flag.getFlagId(), response.getPayload().getFlagId());
			Assert.assertEquals("I changed my mind. This comment isn't that bad after all!", 
					response.getPayload().getFlagDescription());
		}
	}
	
	/**
	 * Test a request to delete an existing ChatterFlag object
	 * from the database.
	 */
	private void testDeleteFlagRequest() {
		System.out.println("ChatterFlagService test: testDeleteFlagRequest");
		this.ctx.setFunctionName(ServiceOperations.DELETE.toString());
		
		FlagServiceRequest<Request> request = new FlagServiceRequest<>();
		request.setData(this.generateDeleteFlagRequest(this.flag.getFlagId()));
		
		@SuppressWarnings("unchecked")
		FlagServiceResponse<ChatterFlag> response = 
			(FlagServiceResponse<ChatterFlag>) handler.handleRequest(request, ctx);
		
		/* Assertions */
		Assert.assertTrue(response.getStatus());
		Assert.assertFalse(response.getExceptionThrown());
		Assert.assertNull(response.getExceptionMessage());
		Assert.assertEquals(ServiceMessages.OPERATION_SUCCESS.toString(), response.getMessage());
	}
}

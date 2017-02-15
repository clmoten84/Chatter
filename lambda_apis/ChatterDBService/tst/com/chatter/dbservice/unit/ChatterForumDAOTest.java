package com.chatter.dbservice.unit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.Assert;

import com.amazonaws.AmazonClientException;
import com.chatter.dbservice.dao.ForumDAO;
import com.chatter.dbservice.dao.impl.ForumDAOImpl;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterForum;
import com.chatter.dbservice.requests.ForumCRUDRequest;
import com.chatter.dbservice.responses.ForumResultPage;
import com.chatter.dbservice.responses.ServicePropsResponse;
import com.chatter.dbservice.util.ops.ChatterForumOps;

/**
 * ChatterForumDBRequestHandlerTest
 * 
 * Since each method in this class requires different
 * set up and tear down. The set up and tear down is
 * handled by each individual test method.
 * @author coreym
 *
 * Unit tests for Chatter Forum DAO
 */
public class ChatterForumDAOTest {
	
	/* Globals */
	private ForumDAO dao;
	
	/**
	 * Initialize the DAO to be tested
	 * @return ForumDAO
	 */
	private ForumDAO getDAO() {
		if (this.dao == null) {
			try {
				this.dao = new ForumDAOImpl();
			}
			catch (PropertyRetrievalException pre) {
				System.out.println("Could not retrieve necessary"
						+ " properties to generate Forum DAO!");
			}
		}
		
		return this.dao;
	}
	
	/**
	 * Create and return a test Chatter Forum object
	 * @return ChatterForum
	 */
	private ChatterForum createTestForum() {
		ChatterForum forum = new ChatterForum();
		forum.setTitle("Just a Test Forum");
		forum.setCreatedBy("dbservice");
		forum.setTimeStamp(new Date().getTime());
		return forum;
	}
	
	/**
	 * Create a list of 40 test Chatter Forum objects
	 * @return List<ChatterForum>
	 */
	private List<ChatterForum> createTestForums() {
		List<ChatterForum> forums = new ArrayList<>();
		for(int i = 0; i < 40; i++) {
			ChatterForum forum = new ChatterForum();
			forum.setTitle("Test Forum #" + i);
			forum.setCreatedBy("dbservice");
			forum.setTimeStamp(new Date().getTime());
			forums.add(forum);
		}
		
		return forums;
	}
	
	/* *************** DAO argument generation ************* */
	
	/**
	 * Generate argument for creating and saving a new ChatterForum
	 * object to the database.
	 * @param forumIn
	 * @return
	 */
	private ForumCRUDRequest generateCreateArgs(ChatterForum forumIn) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.CREATE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", forumIn.getCreatedBy());
		args.put("title", forumIn.getTitle());
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate argument for retrieving ChatterForum object from database
	 * @param forumId
	 * @return
	 */
	private ForumCRUDRequest generateRetrieveArgs(String forumId) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.QUERY_BY_ID);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate argument for querying ChatterForum table using
	 * createdBy attribute
	 * @param createdBy
	 * @return
	 */
	private ForumCRUDRequest generateQueryByCreatorArgs(String createdBy, 
				String startId, String startCreator, String startTimeStamp) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.QUERY_BY_CREATOR);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", createdBy);
		
		if (startId != null && !startId.isEmpty()) 
			args.put("exclusiveStartId", startId);
		
		if (startCreator != null && !startCreator.isEmpty())
			args.put("exclusiveStartCreator", startCreator);
		
		if (startTimeStamp != null && !startTimeStamp.isEmpty())
			args.put("exclusiveStartTimeStamp", startTimeStamp);
		
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
	 * Generate argument for querying ChatterForum table using
	 * title attribute
	 * @param title
	 * @return
	 */
	private ForumCRUDRequest generateQueryByTitleArgs(String title) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.QUERY_BY_TITLE);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("title", title);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate argument for updating existing ChatterForum object
	 * data in database
	 * @param forumId
	 * @return
	 */
	private ForumCRUDRequest generateUpdateArgs(String forumId) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.UPDATE);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		args.put("titleUpdate", "I just modified this forum title");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate argument for adding a comment id to ChatterForum object
	 * in database
	 * @param forumId
	 * @return
	 */
	private ForumCRUDRequest generateAddCommentArgs(String forumId) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.ADD_COMMENT);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		args.put("commentId", "1234-TEST");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate argument for removing a comment id from ChatterForum object
	 * in database
	 * @param forumId
	 * @return
	 */
	private ForumCRUDRequest generateRemoveCommentArgs(String forumId) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.REMOVE_COMMENT);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		args.put("commentId", "1234-TEST");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate argument for deleting ChatterForum object from database
	 * @param forumId
	 * @return
	 */
	private ForumCRUDRequest generateDeleteArgs(String forumId) {
		ForumCRUDRequest request = new ForumCRUDRequest();
		request.setOperation(ChatterForumOps.DELETE);
		request.setReqDate(new Date().getTime());
		
		// Generate a map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		
		request.setArgs(args);
		return request;
	}
	
	/* ***************** TESTS ***************** */
	
	@Test
	public void testPingService() {
		System.out.println("Forum DAO Test: testPingService()");
		
		ForumDAO dao = this.getDAO();
		
		try {
			ServicePropsResponse props = dao.getServiceProperties();
			
			// Assertions
			Assert.assertNotNull(props);
		}
		catch (PropertyRetrievalException pre) {
			System.out.println("An exception occurred while trying to retrieve "
					+ "service properties.");
		}
	}
	
	/**
	 * Tests creating and deleting a ChatterForum. 
	 * The ChatterForum is created and cleaned up
	 * by this method.
	 */
	@Test
	public void testCreateAndDeleteForum() {
		System.out.println("Forum DAO Test: testCreateAndDeleteForum()");
		
		ForumDAO dao = this.getDAO();
		
		try {
			// Create and save a new ChatterForum to DB
			ChatterForum forum = dao.createForum(this.generateCreateArgs(
					this.createTestForum()));
			
			Assert.assertNotNull(forum);
			Assert.assertNotNull(forum.getForumId());
			Assert.assertNull(forum.getCommentIds());
			Assert.assertEquals("Just a Test Forum", forum.getTitle());
			Assert.assertEquals("dbservice", forum.getCreatedBy());
			
			// Delete created ChatterForum object from DB
			boolean deleteSucceeded = dao.deleteForum(this.generateDeleteArgs(
					forum.getForumId()));
			
			Assert.assertTrue(deleteSucceeded);
		}
		catch (RequestValidationException rve) {
			System.out.println("Erroneous arguments to DAO method!");
		}
		catch (AmazonClientException ace) {
			System.out.println("Something bad happened on AWS side!");
		}
	}
	
	/**
	 * Tests retrieving a Chatter Forum instance from the database.
	 * Setup: requires creation of a ChatterForum instance.
	 * Tear down: requires deletion of created ChatterForum instance.
	 */
	@Test
	public void testRetrieveForum() {
		System.out.println("Forum DAO Test: testRetrieveForum()");
		
		ForumDAO dao = this.getDAO();
		
		try {
			/* ************* SET UP ************ */
			ChatterForum forum = dao.createForum(this.generateCreateArgs(
					this.createTestForum()));
			
			Assert.assertNotNull(forum);
			
			// Attempt to retrieve the created forum
			ChatterForum retForum = dao.retrieveForumById(this.generateRetrieveArgs(
					forum.getForumId()));
			
			// Assert that retrieve worked
			Assert.assertNotNull(retForum);
			Assert.assertEquals(forum.getForumId(), retForum.getForumId());
			Assert.assertEquals(forum.getCreatedBy(), retForum.getCreatedBy());
			Assert.assertEquals(forum.getTitle(), retForum.getTitle());
			
			/* ************** CLEAN UP *************** */
			boolean cleanUpSucceeded = dao.deleteForum(this.generateDeleteArgs(
					retForum.getForumId()));
			Assert.assertTrue(cleanUpSucceeded);
		}
		catch (RequestValidationException rve) {
			System.out.println("Erroneous arguments to DAO method!");
		}
		catch (AmazonClientException ace) {
			System.out.println("Something bad happend on AWS side!");
		}
	}
	
	/**
	 * Tests updating a Chatter Forum instance in the database. Updates
	 * tested include updating the forum title, and adding/removing a
	 * comment id to the forum set of comment ids.
	 * Setup: requires creation of a ChatterForum instance
	 * Tear down: requires deletion of created ChatterForum instance.
	 */
	@Test
	public void testUpdateForum() {
		System.out.println("Forum DAO Test: testUpdateForum()");
		
		ForumDAO dao = this.getDAO();
		
		try {
			/* ************* SET UP ************* */
			ChatterForum forum = dao.createForum(this.generateCreateArgs(
					this.createTestForum()));
			
			Assert.assertNotNull(forum);
			
			// Attempt to update forum title
			forum = dao.updateForum(this.generateUpdateArgs(forum.getForumId()));
			Assert.assertNotNull(forum);
			Assert.assertEquals("I just modified this forum title", forum.getTitle());
			
			// Attempt to add comment to forum
			forum = dao.addCommentToForum(this.generateAddCommentArgs(forum.getForumId()));
			Assert.assertNotNull(forum);
			Assert.assertNotNull(forum.getCommentIds());
			Assert.assertFalse(forum.getCommentIds().isEmpty());
			Assert.assertTrue(forum.getCommentIds().contains("1234-TEST"));
			
			// Attempt to remove comment from forum
			forum = dao.removeCommentFromForum(this.generateRemoveCommentArgs(
					forum.getForumId()));
			Assert.assertNotNull(forum);
			Assert.assertNull(forum.getCommentIds());
			
			/* ********** CLEAN UP ********** */
			boolean cleanupSucceeded = dao.deleteForum(this.generateDeleteArgs(
					forum.getForumId()));
			Assert.assertTrue(cleanupSucceeded);
		}
		catch (RequestValidationException rve) {
			System.out.println("Erroneous arguments to DAO method!");
		}
		catch (AmazonClientException ace) {
			System.out.println("Something bad happend on AWS side!");
		}
	}
	
	/**
	 * Tests querying the Chatter Forum table using created_by attribute.
	 * Setup: requires creation of list of ChatterForum instances to query.
	 * Tear down: requires deletion of created ChatterForum instances.
	 */
	@Test
	public void testQueryByCreator() {
		System.out.println("Forum DAO Test: testQueryByCreator()");
		
		ForumDAO dao = this.getDAO();
		List<String> forumIds = new ArrayList<>();
	
		try {
			/* ********** SET UP ********** */
			List<ChatterForum> forums = this.createTestForums();
			for (ChatterForum forum : forums) {
				forum = dao.createForum(this.generateCreateArgs(forum));
				Assert.assertNotNull(forum);
				Assert.assertNotNull(forum.getForumId());
				forumIds.add(forum.getForumId());
			}
			
			// Attempt to query the ChatterForum table by created_by
			ForumResultPage results = dao.queryByCreator(this.generateQueryByCreatorArgs(
					"dbservice", null, null, null));
			Assert.assertNotNull(results);
			Assert.assertNotNull(results.getPageResults());
			Assert.assertTrue(results.getPageResults().size() == 30);
			Assert.assertTrue(results.getResultCount() == 30);
			Assert.assertNotNull(results.getLastEvaluatedKey());
			Assert.assertTrue(results.isMoreResults());
			Assert.assertNotNull(results.getLastEvaluatedKey().get("forum_id"));
			Assert.assertNotNull(results.getLastEvaluatedKey().get("created_by"));
			Assert.assertNotNull(results.getLastEvaluatedKey().get("time_stamp"));
			
			// Attempt to retrieve the remaining ChatterForums from query
			// Should only be 10 records left to retrieve.
			String lastEvalId = results.getLastEvaluatedKey().get("forum_id").getS();
			String lastEvalCreator = results.getLastEvaluatedKey().get("created_by").getS();
			String lastEvalTS = results.getLastEvaluatedKey().get("time_stamp").getN();
			ForumResultPage remResults = dao.queryByCreator(this.generateQueryByCreatorArgs(
					"dbservice", lastEvalId, lastEvalCreator, lastEvalTS));
			Assert.assertNotNull(remResults);
			Assert.assertNotNull(remResults.getPageResults());
			Assert.assertTrue(remResults.getPageResults().size() >= 10);
			Assert.assertTrue(remResults.getResultCount() >= 10);
			Assert.assertNull(remResults.getLastEvaluatedKey());
			Assert.assertFalse(remResults.isMoreResults());
			
			/* ********** CLEAN UP ********** */
			for (String forumId : forumIds) {
				boolean deleteSucceeded = dao.deleteForum(this.generateDeleteArgs(
						forumId));
				Assert.assertTrue(deleteSucceeded);
			}
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	/**
	 * Tests querying the ChatterForum table using title attribute.
	 * Setup: requires creation of ChatterForum instance.
	 * Tear down: requires deletion of created ChatterForum instance.
	 */
	@Test
	public void testQueryByTitle() {
		System.out.println("Forum DAO Test: testQueryByTitle()");
		
		ForumDAO dao = this.getDAO();
		
		try {
			/* ************* SET UP ************* */
			ChatterForum forum = dao.createForum(this.generateCreateArgs(
					this.createTestForum()));
			
			Assert.assertNotNull(forum);
			
			// Attempt to query ChatterForum table using forum title
			ForumResultPage results = dao.queryByTitle(this.generateQueryByTitleArgs(
					"Just a Test Forum"));
			Assert.assertNotNull(results);
			Assert.assertNotNull(results.getPageResults());
			Assert.assertTrue(results.getPageResults().size() > 0);
			Assert.assertTrue(results.getResultCount() > 0);
			Assert.assertNull(results.getLastEvaluatedKey());
			Assert.assertFalse(results.isMoreResults());
			
			/* ********** CLEAN UP ********** */
			boolean cleanupSucceeded = dao.deleteForum(this.generateDeleteArgs(
					forum.getForumId()));
			Assert.assertTrue(cleanupSucceeded);
		}
		catch (PropertyRetrievalException pre) {
			System.out.println("Failed to retrieve a required service property!");
		}
		catch (RequestValidationException rve) {
			System.out.println("Erroneous arguments to DAO method!");
		}
		catch (AmazonClientException ace) {
			System.out.println("Something bad happend on AWS side!");
		}
	}
}

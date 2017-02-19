package com.chatter.dbservice.unit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.chatter.dbservice.dao.CommentDAO;
import com.chatter.dbservice.dao.impl.CommentDAOImpl;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterComment;
import com.chatter.dbservice.requests.CommentCRUDRequest;
import com.chatter.dbservice.responses.CommentResultPage;
import com.chatter.dbservice.responses.ServicePropsResponse;
import com.chatter.dbservice.util.ops.ChatterCommentOps;

/**
 * ChatterCommentDAOTest
 * @author coreym
 *
 * Unit tests for ChatterComment DAO
 * 
 * Since each method in this test class requires set up
 * and tear down, the set up and tear down is handled by
 * each individual test method. This prevents the tests
 * from being interdependent.
 */
public class ChatterCommentDAOTest {

	/* Globals */
	private CommentDAO dao;
	
	/**
	 * Initialize the DAO under test
	 * @return
	 */
	private CommentDAO getDAO() {
		if (this.dao == null) {
			try {
				this.dao = new CommentDAOImpl();
			}
			catch (PropertyRetrievalException pre) {
				pre.printStackTrace();
			}
		}
		return dao;
	}
	
	/* ********** DAO Argument Creation ********** */
	/**
	 * Generate a request to create and save a new ChatterComment to the
	 * DB.
	 * @param createdBy
	 * @param forumId
	 * @param bucketName
	 * @param fileName
	 * @return
	 */
	private CommentCRUDRequest createCommentRequest(String createdBy, String forumId,
			String bucketName, String fileName) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		request.setOperation(ChatterCommentOps.CREATE);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", createdBy);
		args.put("forumId", forumId);
		args.put("bucketName", bucketName);
		args.put("fileName", fileName);
		request.setArgs(args);
		
		return request;
	}
	
	/**
	 * Generate arguments for retrieve/delete a ChatterComment instance from the DB.
	 * @param commentId
	 * @return
	 */
	private CommentCRUDRequest generateRetrieveDeleteArgs(String commentId, boolean isRetrieve) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		
		if (isRetrieve)
			request.setOperation(ChatterCommentOps.RETRIEVE);
		else
			request.setOperation(ChatterCommentOps.DELETE);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("commentId", commentId);
		request.setArgs(args);
		
		return request;
	}
	
	/**
	 * Generate arguments for adding/removing a reply id to ChatterComment instance.
	 * @param commentId
	 * @return
	 */
	private CommentCRUDRequest generateAddRemoveReplyArgs(String commentId, boolean isAdd) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		
		if (isAdd)
			request.setOperation(ChatterCommentOps.ADD_REPLY);
		else
			request.setOperation(ChatterCommentOps.REMOVE_REPLY);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("commentId", commentId);
		args.put("replyId", "my-reply-test");
		request.setArgs(args);
		
		return request;
	}
	
	/**
	 * Generate arguments for adding/removing a flad id to ChatterComment instance.
	 * @param commentId
	 * @return
	 */
	private CommentCRUDRequest generateAddRemoveFlagArgs(String commentId, boolean isAdd) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		
		if (isAdd)
			request.setOperation(ChatterCommentOps.ADD_FLAG);
		else
			request.setOperation(ChatterCommentOps.REMOVE_FLAG);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("commentId", commentId);
		args.put("flagId", "my-flag-test");
		request.setArgs(args);
		
		return request;
	}
	
	/**
	 * Generate arguments for incrementing concur count on ChatterComment instance.
	 * @param commentId
	 * @return
	 */
	private CommentCRUDRequest generateIncrementConcurArgs(String commentId) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		request.setOperation(ChatterCommentOps.INCREMENT_CONCUR);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("commentId", commentId);
		request.setArgs(args);
		
		return request;
	}
	
	/**
	 * Generate arguments for retrieve/delete a batch of ChatterComment
	 * instances from the DB.
	 * @param commentIds
	 * @param isRetrieve
	 * @return
	 */
	private CommentCRUDRequest generateBatchRetrieveDeleteArgs(List<String> commentIds, 
				boolean isRetrieve) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		
		if (isRetrieve)
			request.setOperation(ChatterCommentOps.BATCH_RETRIEVE);
		else
			request.setOperation(ChatterCommentOps.BATCH_DELETE);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, List<String>> args = new HashMap<>();
		args.put("commentIds", commentIds);
		request.setArgs(args);
		
		return request;
	}
	
	/**
	 * Generate arguments for querying ChatterComment table using
	 * createdBy value.
	 * @param createdBy
	 * @return
	 */
	private CommentCRUDRequest generateQueryByCreatorArgs(String createdBy, String startId,
			String startCreator, String startTS) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		request.setOperation(ChatterCommentOps.QUERY_BY_CREATOR);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", createdBy);
		
		if (startId != null && !startId.isEmpty())
			args.put("exclusiveStartId", startId);
		
		if (startCreator != null && !startCreator.isEmpty())
			args.put("exclusiveStartCreator", startCreator);
		
		if (startTS != null && !startTS.isEmpty())
			args.put("exclusiveStartTS", startTS);
		
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
	 * Generate arguments for querying ChatterComment table using
	 * forumId value.
	 * @param forumId
	 * @return
	 */
	private CommentCRUDRequest generateQueryByForumArgs(String forumId, String startId,
			String startForum) {
		CommentCRUDRequest request = new CommentCRUDRequest();
		request.setOperation(ChatterCommentOps.QUERY_BY_FORUM);
		request.setReqDate(new Date().getTime());
		
		// Map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("forumId", forumId);
		
		if (startId != null && !startId.isEmpty())
			args.put("exclusiveStartId", startId);
		
		if (startForum != null && !startForum.isEmpty())
			args.put("exclusiveStartForum", startForum);
		request.setArgs(args);
		
		return request;
	}
	
	/* ********** TESTS ********** */
	
	/**
	 * Tests pinging the db service from the ChatterComment dao
	 */
	@Test
	public void testPingService() {
		System.out.println("Comment DAO test: testPingService()");
		
		CommentDAO dao = this.getDAO();
		
		try {
			ServicePropsResponse props = dao.getServiceProperties();
			Assert.assertNotNull(props);
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
	}
	
	/**
	 * Tests creating and deleting a ChatterComment instance
	 * from the DB.
	 */
	@Test
	public void testCreateAndDeleteComment() {
		System.out.println("Comment DAO test: testCreateAndDeleteComment()");
		
		CommentDAO dao = this.getDAO();
		
		try {
			ChatterComment comment = dao.createComment(this.createCommentRequest("dbservice", 
					"1234-TEST", "dbservice-test-bucket", "aTest.txt"));
			
			Assert.assertNotNull(comment);
			Assert.assertNotNull(comment.getCommentId());
			Assert.assertFalse(comment.getCommentId().isEmpty());
			Assert.assertNull(comment.getFlagIds());
			Assert.assertNull(comment.getReplyIds());
			
			boolean deleteSucceeded = dao.deleteComment(this.generateRetrieveDeleteArgs(
					comment.getCommentId(), false));
			Assert.assertTrue(deleteSucceeded);
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	/**
	 * Tests retrieving a ChatterComment instance from the DB
	 */
	@Test
	public void testRetrieveComment() {
		System.out.println("Comment DAO test: testRetrieveComment()");
		
		CommentDAO dao = this.getDAO();
		
		try {
			/* ********** SET UP *********** */
			ChatterComment comment = dao.createComment(this.createCommentRequest("dbservice", 
					"1234-TEST", "dbservice-test-bucket", "aTest.txt"));
			Assert.assertNotNull(comment);
			
			// Attempt to retrieve the newly saved comment from DB
			ChatterComment retComment = dao.retrieveComment(this.generateRetrieveDeleteArgs(
					comment.getCommentId(), true));
			Assert.assertNotNull(retComment);
			Assert.assertEquals(comment.getCommentId(), retComment.getCommentId());
			
			/* ********** CLEAN UP ********** */
			boolean deleteSucceeded = dao.deleteComment(this.generateRetrieveDeleteArgs(
					retComment.getCommentId(), false));
			Assert.assertTrue(deleteSucceeded);
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	@Test
	public void testUpdateComment() {
		System.out.println("Comment DAO test: testUpdateComment()");
		
		CommentDAO dao = this.getDAO();
		
		try {
			/* ********** SET UP ********** */
			ChatterComment comment = dao.createComment(this.createCommentRequest("dbservice", 
					"1234-TEST", "dbservice-test-bucket", "aTest.txt"));
			Assert.assertNotNull(comment);
			
			// Attempt to add reply to comment
			comment = dao.updateComment(this.generateAddRemoveReplyArgs(
					comment.getCommentId(), true));
			Assert.assertNotNull(comment);
			Assert.assertNotNull(comment.getReplyIds());
			Assert.assertTrue(comment.getReplyIds().size() > 0);
			
			// Attempt to remove reply from comment
			comment = dao.updateComment(this.generateAddRemoveReplyArgs(
					comment.getCommentId(), false));
			Assert.assertNotNull(comment);
			Assert.assertNull(comment.getReplyIds());
			
			// Attempt to add flag to comment
			comment = dao.updateComment(this.generateAddRemoveFlagArgs(
					comment.getCommentId(), true));
			Assert.assertNotNull(comment);
			Assert.assertNotNull(comment.getFlagIds());
			Assert.assertTrue(comment.getFlagIds().size() > 0);
			
			// Attempt to remove flag from comment
			comment = dao.updateComment(this.generateAddRemoveFlagArgs(
					comment.getCommentId(), false));
			Assert.assertNotNull(comment);
			Assert.assertNull(comment.getFlagIds());
			
			// Attempt to increment concur count for comment
			comment = dao.updateComment(this.generateIncrementConcurArgs(
					comment.getCommentId()));
			Assert.assertNotNull(comment);
			Assert.assertEquals(1, comment.getConcurCnt());
			
			/* ********** CLEAN UP ********** */
			boolean deleteSucceeded = dao.deleteComment(this.generateRetrieveDeleteArgs(
					comment.getCommentId(), false));
			Assert.assertTrue(deleteSucceeded);
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	/**
	 * Tests querying the ChatterComment table using createdBy
	 * attribute value.
	 * SET UP: create and save a list of ChatterComment instances to DB
	 * CLEAN UP: delete the batch of ChatterComment instances created in DB
	 */
	@Test
	public void testQueryByCreator() {
		System.out.println("Comment DAO test: testQueryByCreator()");
		
		CommentDAO dao = this.getDAO();
		
		try {
			/* ********** SET UP ********** */
			List<String> commentIds = new ArrayList<>();
			for (int i=0; i < 40; i++) {
				ChatterComment comment = dao.createComment(this.createCommentRequest(
						"dbservice", "1234-TEST", "dbservice-test-bucket", "aTest.txt"));
				Assert.assertNotNull(comment);
				Assert.assertNotNull(comment.getCommentId());
				Assert.assertFalse(comment.getCommentId().isEmpty());
				commentIds.add(comment.getCommentId());
			}
			Assert.assertEquals(40, commentIds.size());
			
			// Attempt to query ChatterComment table
			CommentResultPage resultPage = dao.queryByCreator(
					this.generateQueryByCreatorArgs("dbservice", null, null, null));
			Assert.assertNotNull(resultPage);
			Assert.assertNotNull(resultPage.getPageResults());
			Assert.assertTrue(resultPage.getPageResults().size() == 30);
			Assert.assertTrue(resultPage.getResultCount() == 30);
			Assert.assertNotNull(resultPage.getLastEvaluatedKey());
			Assert.assertTrue(resultPage.getMoreResults());
			Assert.assertNotNull(resultPage.getLastEvaluatedKey().get("comment_id"));
			Assert.assertNotNull(resultPage.getLastEvaluatedKey().get("created_by"));
			Assert.assertNotNull(resultPage.getLastEvaluatedKey().get("time_stamp"));
			
			// Attempt to retrieve the remaining ChatterComments to satisfy query
			// Should only be 10 instances
			String lastEvalId = resultPage.getLastEvaluatedKey().get("comment_id").getS();
			String lastCreator = resultPage.getLastEvaluatedKey().get("created_by").getS();
			String lastTS = resultPage.getLastEvaluatedKey().get("time_stamp").getN();
			
			CommentResultPage remResults = dao.queryByCreator(
					this.generateQueryByCreatorArgs("dbservice", 
							lastEvalId, lastCreator, lastTS));
			Assert.assertNotNull(remResults);
			Assert.assertNotNull(remResults.getPageResults());
			Assert.assertTrue(remResults.getPageResults().size() == 10);
			Assert.assertTrue(remResults.getResultCount() == 10);
			Assert.assertNull(remResults.getLastEvaluatedKey());
			Assert.assertFalse(remResults.getMoreResults());
			
			/* ********** CLEAN UP ********** */
			List<String> delComments = dao.batchDelete(this.generateBatchRetrieveDeleteArgs(
					commentIds, false));
			Assert.assertTrue(delComments.size() == 40);
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	/**
	 * Tests querying the ChatterComment table by forum id value
	 * SET UP: create and save a list of ChatterComment instances to DB
	 * CLEAN UP: delete list of ChatterComment instances from DB
	 */
	@Test
	public void testQueryByForum() {
		System.out.println("Comment DAO test: testQueryByForum()");
		
		CommentDAO dao = this.getDAO();
		
		try {
			/* ********** SET UP ********** */
			List<String> commentIds = new ArrayList<>();
			for (int i=0; i < 40; i++) {
				ChatterComment comment = dao.createComment(this.createCommentRequest(
						"dbservice", "1234-TEST", "dbservice-test-bucket", "aTest.txt"));
				Assert.assertNotNull(comment);
				Assert.assertNotNull(comment.getCommentId());
				Assert.assertFalse(comment.getCommentId().isEmpty());
				commentIds.add(comment.getCommentId());
			}
			Assert.assertTrue(commentIds.size() == 40);
			
			// Attempt to query the ChatterComment table
			CommentResultPage results = dao.queryByForum(this.generateQueryByForumArgs(
					"1234-TEST", null, null));
			Assert.assertNotNull(results);
			Assert.assertNotNull(results.getPageResults());
			Assert.assertTrue(results.getPageResults().size() == 30);
			Assert.assertTrue(results.getResultCount() == 30);
			Assert.assertNotNull(results.getLastEvaluatedKey());
			Assert.assertTrue(results.getMoreResults());
			Assert.assertNotNull(results.getLastEvaluatedKey().get("comment_id"));
			Assert.assertNotNull(results.getLastEvaluatedKey().get("forum_id"));
			
			// Attempt to retrieve the remaining results to satisfy query
			String lastEvalId = results.getLastEvaluatedKey().get("comment_id").getS();
			String lastForum = results.getLastEvaluatedKey().get("forum_id").getS();
			
			CommentResultPage remResults = dao.queryByForum(
					this.generateQueryByForumArgs("1234-TEST", lastEvalId, lastForum));
			Assert.assertNotNull(remResults);
			Assert.assertNotNull(remResults.getPageResults());
			Assert.assertTrue(remResults.getPageResults().size() == 10);
			Assert.assertTrue(remResults.getResultCount() == 10);
			Assert.assertNull(remResults.getLastEvaluatedKey());
			Assert.assertFalse(remResults.getMoreResults());
			
			/* ********** CLEAN UP ********** */
			List<String> delComments = dao.batchDelete(this.generateBatchRetrieveDeleteArgs(
					commentIds, false));
			Assert.assertTrue(delComments.size() == 40);
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	/**
	 * Tests retrieving a batch of ChatterComment instances from
	 * the DB.
	 * SET UP: create and save a list of ChatterComment instances to DB
	 * CLEAN UP: delete list of ChatterComment instances from DB
	 */
	@Test
	public void testBatchRetrieve() {
		System.out.println("Comment DAO test: testBatchRetrieve()");
		
		CommentDAO dao = this.getDAO();
		
		try {
			/* ********** SET UP ********** */
			List<String> commentIds = new ArrayList<>();
			for (int i=0; i < 40; i++) {
				ChatterComment comment = dao.createComment(this.createCommentRequest(
						"dbservice", "1234-TEST", "dbservice-test-bucket", "aTest.txt"));
				Assert.assertNotNull(comment);
				Assert.assertNotNull(comment.getCommentId());
				Assert.assertFalse(comment.getCommentId().isEmpty());
				commentIds.add(comment.getCommentId());
			}
			Assert.assertEquals(40, commentIds.size());
			
			// Attempt to retrieve batch of ChatterComment instances
			List<ChatterComment> retComments = dao.batchRetrieve(
					this.generateBatchRetrieveDeleteArgs(commentIds, true));
			Assert.assertNotNull(retComments);
			Assert.assertTrue(retComments.size() == 40);
			
			/* ********** CLEAN UP ********** */
			List<String> delComments = dao.batchDelete(this.generateBatchRetrieveDeleteArgs(
					commentIds, false));
			Assert.assertTrue(delComments.size() == commentIds.size());
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (PropertyRetrievalException pre) {
			pre.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
}

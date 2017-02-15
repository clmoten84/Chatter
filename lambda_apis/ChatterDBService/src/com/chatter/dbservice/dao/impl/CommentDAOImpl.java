package com.chatter.dbservice.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import com.chatter.dbservice.dao.CommentDAO;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterComment;
import com.chatter.dbservice.requests.CommentCRUDRequest;
import com.chatter.dbservice.requests.validators.CommentCRUDRequestValidator;
import com.chatter.dbservice.responses.CommentResultPage;
import com.chatter.dbservice.responses.ServicePropsResponse;
import com.chatter.dbservice.util.PropertiesResolver;
import com.chatter.dbservice.util.ops.ChatterCommentOps;

/**
 * CommentDAOImpl
 * @author coreym
 *
 * Implements data access methods for accessing Chatter Comment
 * data in the database.
 */
public class CommentDAOImpl implements CommentDAO{
	
	private AmazonDynamoDBClient dbClient;
	private PropertiesResolver propsResolver;
	private DynamoDBMapper dbMapper;
	
	public CommentDAOImpl() throws PropertyRetrievalException {
		// Initialize properties resolver instance
		this.propsResolver = new PropertiesResolver("service.properties");
		
		// Initialize DynamoDB client instance
		// If the execution environment is local use the 
		// ProfileCredentialsProvider so that the client can
		// find AWS credentials in the local environment. Otherwise
		// use the default provider chain.
		if (this.propsResolver.getProperty("service.env").equalsIgnoreCase("local")) {
			this.dbClient = new AmazonDynamoDBClient(new ProfileCredentialsProvider());
			this.dbClient.setEndpoint(propsResolver.getProperty("aws.dynamodb.endpoint"));
			this.dbMapper = new DynamoDBMapper(dbClient, new ProfileCredentialsProvider());
		}
		else {
			this.dbClient = new AmazonDynamoDBClient();
			this.dbClient.setEndpoint(propsResolver.getProperty("aws.dynamodb.endpoint"));
			this.dbMapper = new DynamoDBMapper(dbClient);
		}
	}

	/**
	 * Create and save a new ChatterComment object to the database.
	 */
	@Override
	public ChatterComment createComment(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException, PropertyRetrievalException {
		
		// Validate incoming request
		CommentCRUDRequestValidator.validateCreateRequest(request);
		
		// Create Chatter Comment object using request parameters
		ChatterComment comment = new ChatterComment();
		comment.setCreatedBy((String) request.getArgs().get("createdBy"));
		comment.setTimeStamp(new Date().getTime());
		comment.setForumId((String) request.getArgs().get("forumId"));
		comment.setReplyIds(null);
		comment.setFlagIds(null);
		comment.setConcurCnt(0);
		comment.setAudioFileLink(dbMapper.createS3Link(
				(String) request.getArgs().get("bucketName"), 
				(String) request.getArgs().get("fileName")));
		
		// Save comment to DB
		dbMapper.save(comment);
		return comment;
	}

	/**
	 * Retrieve a ChatterComment object from the database
	 * using an argument comment id.
	 */
	@Override
	public ChatterComment retrieveComment(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException {
		
		// Validate incoming request
		CommentCRUDRequestValidator.validateRetrieveRequest(request);
		
		// Attempt to retrieve comment from DB
		ChatterComment comment = null;
		comment = dbMapper.load(ChatterComment.class, 
				(String) request.getArgs().get("commentId"));
		return comment;
	}

	/**
	 * Delete an existing ChatterComment object from the database
	 * using an argument comment id.
	 */
	@Override
	public boolean deleteComment(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException {
		
		// Validate incoming request
		CommentCRUDRequestValidator.validateRetrieveRequest(request);
		
		// Attempt to retrieve comment from DB
		ChatterComment comment = null;
		comment = dbMapper.load(ChatterComment.class,
				(String) request.getArgs().get("commentId"));
		
		if (comment != null) {
			dbMapper.delete(comment);
			return true;
		}
		
		return false;
	}

	/**
	 * Query the ChatterComment table using argument
	 * createdBy value and optional time_stamp conditions.
	 */
	@Override
	public CommentResultPage queryByCreator(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException, PropertyRetrievalException {
		
		// Validate incoming request
		CommentCRUDRequestValidator.validateQueryByCreatorRequest(request);
		
		CommentResultPage commentResultPage = null;
		QueryResultPage<ChatterComment> resultPage = null;
		Map<String, AttributeValue> exclusiveStartKey = null;
		Condition sortKeyCond = null;
		
		String createdBy = (String) request.getArgs().get("createdBy");
		String timeStampFrom = (String) request.getArgs().get("timeStampFrom");
		String timeStampTo = (String) request.getArgs().get("timeStampTo");
		String exclusiveStartId = (String) request.getArgs().get("exclusiveStartId");
		String exclusiveStartCreator = (String) request.getArgs().get("exclusiveStartCreator");
		String exclusiveStartTS = (String) request.getArgs().get("exclusiveStartTS");
		
		// Check to see if an exclusive start value was included in request
		if (exclusiveStartId != null && !exclusiveStartId.isEmpty()
				&& exclusiveStartCreator != null && !exclusiveStartCreator.isEmpty()
				&& exclusiveStartTS != null && !exclusiveStartTS.isEmpty()) {
			
			// ID AttributeValue
			AttributeValue idAttVal = new AttributeValue();
			idAttVal.setS(exclusiveStartId);
			
			// Created by AttributeValue
			AttributeValue creatorAttVal = new AttributeValue();
			creatorAttVal.setS(exclusiveStartCreator);
			
			// Time stamp AttributeValue
			AttributeValue tsAttVal = new AttributeValue();
			tsAttVal.setN(exclusiveStartTS);
			
			exclusiveStartKey = new HashMap<>();
			exclusiveStartKey.put("comment_id", idAttVal);
			exclusiveStartKey.put("created_by", creatorAttVal);
			exclusiveStartKey.put("time_stamp", tsAttVal);
		}	
		
		// Check to see if sort key values were included in request
		if (timeStampFrom != null && timeStampTo != null) {
			sortKeyCond = new Condition()
				.withComparisonOperator(ComparisonOperator.BETWEEN.toString())
				.withAttributeValueList(new AttributeValue().withN(timeStampFrom), 
						new AttributeValue().withN(timeStampTo));
		}
		
		ChatterComment comment = new ChatterComment();
		comment.setCreatedBy(createdBy);
		
		// Create query expression and set properties
		DynamoDBQueryExpression<ChatterComment> query = new DynamoDBQueryExpression<>();
		query.setHashKeyValues(comment);
		query.setIndexName("created_by_index");
		query.setLimit(Integer.parseInt(propsResolver.getProperty("queryLimit")));
		query.setConsistentRead(false);
		
		// Set index range key if applicable
		if (sortKeyCond != null) {
			Map<String, Condition> cond = new HashMap<>();
			cond.put("time_stamp", sortKeyCond);
			query.setRangeKeyConditions(cond);
		}
		
		// Set exclusive start key
		if (exclusiveStartKey != null) {
			query.setExclusiveStartKey(exclusiveStartKey);
		}
		
		resultPage = dbMapper.queryPage(ChatterComment.class, query);
		
		// Process result page
		if (resultPage != null) {
			commentResultPage = new CommentResultPage(
					resultPage.getResults(),
					resultPage.getLastEvaluatedKey(),
					resultPage.getCount());
		}
		
		return commentResultPage;
	}

	/**
	 * Query the ChatterComment table using argument
	 * forumId value.
	 */
	@Override
	public CommentResultPage queryByForum(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException, PropertyRetrievalException {
		
		// Validate incoming request
		CommentCRUDRequestValidator.validateQueryByForumRequest(request);
		
		CommentResultPage commentResultPage = null;
		QueryResultPage<ChatterComment> resultPage = null;
		Map<String, AttributeValue> exclusiveStartKey = null;
		
		String forumId = (String) request.getArgs().get("forumId");
		String exclusiveStartId = (String) request.getArgs().get("exclusiveStartId");
		String exclusiveStartForum = (String) request.getArgs().get("exclusiveStartForum");
		
		// Check to see if an exclusive start value was included in request
		if (exclusiveStartId != null && !exclusiveStartId.isEmpty()
				&& exclusiveStartForum != null && !exclusiveStartForum.isEmpty()) {
			
			// ID AttributeValue
			AttributeValue idAttVal = new AttributeValue();
			idAttVal.setS(exclusiveStartId);
			
			// Forum ID AttributeValue
			AttributeValue forumAttVal = new AttributeValue();
			forumAttVal.setS(exclusiveStartForum);
			
			exclusiveStartKey = new HashMap<>();
			exclusiveStartKey.put("comment_id", idAttVal);
			exclusiveStartKey.put("forum_id", forumAttVal);
		}
		
		ChatterComment comment = new ChatterComment();
		comment.setForumId(forumId);
		
		// Create query expression and set properties
		DynamoDBQueryExpression<ChatterComment> query = new DynamoDBQueryExpression<>();
		query.setHashKeyValues(comment);
		query.setIndexName("forum_id_index");
		query.setLimit(Integer.parseInt(propsResolver.getProperty("queryLimit")));
		query.setConsistentRead(false);
		
		// Set exclusive start key
		if (exclusiveStartKey != null) {
			query.setExclusiveStartKey(exclusiveStartKey);
		}
		
		resultPage = dbMapper.queryPage(ChatterComment.class, query);
		
		// Process result page
		if (resultPage != null) {
			commentResultPage = new CommentResultPage(
					resultPage.getResults(),
					resultPage.getLastEvaluatedKey(),
					resultPage.getCount());
		}
		
		return commentResultPage;
	}

	/**
	 * Retrieve a batch of ChatterComment objects from the database
	 * using an argument list of comment ids.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<ChatterComment> batchRetrieve(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException {
		
		// Validate incoming request
		CommentCRUDRequestValidator.validateBatchRetrieveRequest(request);
		
		// Initialize result list
		List<ChatterComment> commentResults = null;
		
		// Create arguments for batch load
		List<String> commentIds = (List<String>) request.getArgs().get("commentIds");
		Map<Class<?>, List<KeyPair>> primaryKeyMap = new HashMap<>();
		primaryKeyMap.put(ChatterComment.class, this.generateHashKeyPairList(commentIds));
		
		// Attempt to retrieve list of comment objects from database
		Map<String, List<Object>> commentData = dbMapper.batchLoad(primaryKeyMap);
		
		if (commentData != null) {
			// Need to cast object to ChatterComment and add
			// to list to return
			commentResults = new ArrayList<>();
			List<Object> objectResults = commentData.get("Chatter_Comment");
			for (Object object : objectResults) {
				ChatterComment comment = (ChatterComment) object;
				commentResults.add(comment);
			}
		}
		
		return commentResults;
	}

	/**
	 * Delete a batch of ChatterComment objects from the database
	 * using an argument list of comment ids.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<String> batchDelete(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException {
		
		// Validate incoming request
		CommentCRUDRequestValidator.validateBatchRetrieveRequest(request);
		
		// Initialize result lists
		List<ChatterComment> commentResults = null;
		List<String> resultCommentIds = null;
		
		// Create arguments for batch load
		List<String> commentIds = (List<String>) request.getArgs().get("commentIds");
		Map<Class<?>, List<KeyPair>> primaryKeyMap = new HashMap<>();
		primaryKeyMap.put(ChatterComment.class, this.generateHashKeyPairList(commentIds));
		
		// Attempt to retrieve list of comment objects from database
		Map<String, List<Object>> commentData = dbMapper.batchLoad(primaryKeyMap);
		
		if (commentData != null) {
			// Need to cast object to ChatterComment and add
			// to list
			commentResults = new ArrayList<>();
			List<Object> objectResults = commentData.get("Chatter_Comment");
			for (Object object : objectResults) {
				ChatterComment comment = (ChatterComment) object;
				commentResults.add(comment);
			}
			
			if (commentResults.size() > 0) {
				dbMapper.batchDelete(commentResults);
				resultCommentIds = new ArrayList<>();
				for (ChatterComment comment : commentResults) {
					resultCommentIds.add(comment.getCommentId());
				}
			}
		}
		
		return resultCommentIds;
	}
	
	/**
	 * Gathers data about this service and returns it in an object wrapper
	 */
	@Override
	public ServicePropsResponse getServiceProperties() throws PropertyRetrievalException {
		return new ServicePropsResponse(
			this.propsResolver.getProperty("service.env"),
			this.propsResolver.getProperty("service.name"),
			this.propsResolver.getProperty("service.description"),
			this.propsResolver.getProperty("service.version"));
	}

	/**
	 * Updates an existing ChatterComment object in the database.
	 * This update method can perform the following updates:
	 * - ADD REPLY ID
	 * - REMOVE REPLY ID
	 * - ADD FLAG ID
	 * - REMOVE FLAG ID
	 * - INCREMENT CONCUR CNT
	 */
	@Override
	public ChatterComment updateComment(CommentCRUDRequest request)
			throws AmazonServiceException, AmazonClientException,
			RequestValidationException {
		
		ChatterComment comment = null;
		
		/* ADD REPLY UPDATE */
		if (request.getOperation().toString().equalsIgnoreCase
				(ChatterCommentOps.ADD_REPLY.toString())) {
			// Validate incoming request
			CommentCRUDRequestValidator.validateCommentReplyRequest(request);
			
			// Add reply id to comment list
			comment = this.addReplyId(request.getArgs());
		}
		
		/* REMOVE REPLY UPDATE */
		else if (request.getOperation().toString().equalsIgnoreCase
				(ChatterCommentOps.REMOVE_REPLY.toString())) {
			// Validate incoming request
			CommentCRUDRequestValidator.validateCommentReplyRequest(request);
			
			// Remove reply id from comment list
			comment = this.removeReplyId(request.getArgs());
		}
		
		/* ADD FLAG UPDATE */
		else if (request.getOperation().toString().equalsIgnoreCase
				(ChatterCommentOps.ADD_FLAG.toString())) {
			// Validate incoming request
			CommentCRUDRequestValidator.validateCommentFlagRequest(request);
			
			// Add flag id to comment list
			comment = this.addFlagId(request.getArgs());
		}
		
		/* REMOVE FLAG UPDATE */
		else if (request.getOperation().toString().equalsIgnoreCase
				(ChatterCommentOps.REMOVE_FLAG.toString())) {
			// Validate incoming request
			CommentCRUDRequestValidator.validateCommentFlagRequest(request);
			
			// Remove flag id from comment list
			comment = this.removeFlagId(request.getArgs());
		}
		
		/* INCREMENT CONCUR_CNT UPDATE */
		else if (request.getOperation().toString().equalsIgnoreCase
				(ChatterCommentOps.INCREMENT_CONCUR.toString())) {
			// Validate incoming request
			CommentCRUDRequestValidator.validateRetrieveRequest(request);
			
			// Increment comment concur cnt
			comment = this.incrementConcurCnt(request.getArgs());
		}
		
		return comment;
	}
	
	/**
	 * Add reply id to comment retrieved from database
	 * @param reqArgs
	 * @return
	 */
	private ChatterComment addReplyId(Map<String, ?> reqArgs) {
		// Attempt to retrieve requested comment from database
		ChatterComment comment = this.dbMapper.load(ChatterComment.class,
				(String) reqArgs.get("commentId"));
		
		if (comment != null) {
			Set<String> replyIds = comment.getReplyIds();
			if (replyIds == null) {
				replyIds = new HashSet<>();
			}
			
			if (replyIds.add((String) reqArgs.get("replyId"))) {
				comment.setReplyIds(replyIds);
				dbMapper.save(comment);
			}
		}
		return comment;
	}
	
	/**
	 * Remove a reply id from comment retrieved from database
	 * @param reqArgs
	 * @return
	 */
	private ChatterComment removeReplyId(Map<String, ?> reqArgs) {
		// Attempt to retrieve requested comment from database
		ChatterComment comment = this.dbMapper.load(ChatterComment.class,
				(String) reqArgs.get("commentId"));
		
		if (comment != null) {
			Set<String> replyIds = comment.getReplyIds();
			if (replyIds != null) {
				if (replyIds.remove((String) reqArgs.get("replyId"))) {
					
					// If resultant replyIds set is now empty, null the
					// comment's reference to it.
					if (replyIds.isEmpty()) {
						comment.setReplyIds(null);
					}
					else {
						comment.setReplyIds(replyIds);
					}
					
					// Save the updated comment object
					this.dbMapper.save(comment);
				}
			}
		}
		
		return comment;
	}
	
	/**
	 * Add a flagId to comment flag id set
	 * @param reqArgs
	 * @return
	 */
	private ChatterComment addFlagId(Map<String, ?> reqArgs) {
		// Attempt to retrieve request comment from database
		ChatterComment comment = this.dbMapper.load(ChatterComment.class,
				(String) reqArgs.get("commentId"));
		
		if (comment != null) {
			Set<String> flagIds = comment.getFlagIds();
			if (flagIds == null) {
				flagIds = new HashSet<>();
			}
			
			if (flagIds.add((String) reqArgs.get("flagId"))) {
				comment.setFlagIds(flagIds);
				this.dbMapper.save(comment);
			}
		}
		return comment;
	}
	
	/**
	 * Remove a flag id from the argument comment's flag id set.
	 * @param reqArgs
	 * @return
	 */
	private ChatterComment removeFlagId(Map<String, ?> reqArgs) {
		// Attempt to retrieve requested comment from database
		ChatterComment comment = this.dbMapper.load(ChatterComment.class,
				(String) reqArgs.get("commentId"));
		
		if (comment != null) {
			Set<String> flagIds = comment.getFlagIds();
			if (flagIds != null) {
				if (flagIds.remove((String) reqArgs.get("flagId"))) {
					// If resultant flag id set is now empty,
					// null out the comment's reference to it
					if (flagIds.isEmpty()) {
						comment.setFlagIds(null);
					}
					else {
						comment.setFlagIds(flagIds);
					}
					
					// Save the updated comment object
					this.dbMapper.save(comment);
				}
			}
		}
		return comment;
	}
	
	/**
	 * Increments the argument comment's concur attribute value by
	 * one.
	 * @param reqArgs
	 * @return
	 */
	private ChatterComment incrementConcurCnt(Map<String, ?> reqArgs) {
		// Attempt to retrieve requested comment from database
		ChatterComment comment = this.dbMapper.load(ChatterComment.class, 
				(String) reqArgs.get("commentId"));
		
		if (comment != null) {
			// Increment comment concur attribute
			comment.setConcurCnt(comment.getConcurCnt() + 1);
			
			// Save updated comment object
			this.dbMapper.save(comment);
		}
		return comment;
	}
	
	/**
	 * Generate a list of KeyPairs from the argument list of
	 * hash keys.
	 * @param hashKeys
	 * @return
	 */
	private List<KeyPair> generateHashKeyPairList(List<String> hashKeys) {
		List<KeyPair> keyPairs = new ArrayList<>();
		for (String hashKey : hashKeys) {
			keyPairs.add(new KeyPair().withHashKey(hashKey));
		}
		return keyPairs;
	}
}

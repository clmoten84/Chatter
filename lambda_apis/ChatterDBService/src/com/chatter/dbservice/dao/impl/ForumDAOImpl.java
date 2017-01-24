package com.chatter.dbservice.dao.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chatter.dbservice.dao.ForumDAO;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterForum;
import com.chatter.dbservice.requests.ForumCRUDRequest;
import com.chatter.dbservice.requests.validators.ForumCRUDRequestValidator;
import com.chatter.dbservice.responses.ForumResultPage;
import com.chatter.dbservice.responses.ServicePropsResponse;
import com.chatter.dbservice.util.PropertiesResolver;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;

/**
 * ForumDAOImpl
 * @author coreym
 *
 * Provides data access methods for Chatter Forum data
 */
public class ForumDAOImpl implements ForumDAO{
	private AmazonDynamoDBClient dbClient;
	private PropertiesResolver propsResolver;
	private DynamoDBMapper dbMapper;
	
	public ForumDAOImpl() throws PropertyRetrievalException{
		//Create DynamoDB client
		dbClient = new AmazonDynamoDBClient();
		
		//Set DB end-point from properties file
		propsResolver = new PropertiesResolver("service.properties");
		dbClient.setEndpoint(propsResolver.getProperty("aws.dynamodb.endpoint"));
		
		//Create DB mapper object
		dbMapper = new DynamoDBMapper(dbClient);
	}

	/**
	 * Create and save a new Forum object to the DB
	 * 
	 * @param Request request object containing data to create Forum
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the Forum object created
	 */
	@Override
	public ChatterForum createForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateCreateForumRequest(req);
		
		//Create ChatterForum object using request parameters
		ChatterForum forum = new ChatterForum();
		forum.setCreatedBy((String) req.getArgs().get("createdBy"));
		forum.setTitle((String) req.getArgs().get("title"));
		forum.setTimeStamp(new Date().getTime());
		forum.setCommentIds(null);
		
		//Save ChatterForum object to DB
		dbMapper.save(forum);
		return forum;
	}
	
	/**
	 * Retrieves a Forum object from DB using object id
	 * 
	 * @param Request request containing data for object retrieval
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the retrieved Forum object
	 */
	@Override
	public ChatterForum retrieveForumById(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateRetrieveForumRequest(req);
		
		//Attempt to retrieve Forum object from DB
		ChatterForum forum = null;
		forum = dbMapper.load(ChatterForum.class, (String) req.getArgs().get("forumId"));
		return forum;
	}
	
	/**
	 * Updates a Forum object in the DB
	 * 
	 * @param Request request containing data for object update
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the updated Forum object
	 */
	@Override
	public ChatterForum updateForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateRetrieveForumRequest(req);
		
		// Attempt to retrieve requested Forum instance from DB
		ChatterForum forum = dbMapper.load(ChatterForum.class, 
				(String) req.getArgs().get("forumId"));
		
		// Attempt to update Forum object if retrieval was successful
		if(forum != null) {
			if (req.getArgs().get("titleUpdate") != null) {
				forum.setTitle((String) req.getArgs().get("titleUpdate"));
			}
			dbMapper.save(forum);
		}
		return forum;
	}
	
	/**
	 * Adds a comment id to the set of comment ids for a Forum object
	 * 
	 * @param Request request containing data for object update
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the updated Forum object
	 */
	@Override
	public ChatterForum addCommentToForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateCommentRequest(req);
		
		// Attempt to retrieve Forum object from DB
		ChatterForum forum = dbMapper.load(ChatterForum.class, 
				(String) req.getArgs().get("forumId"));
		
		// Attempt to add comment to Forum object if retrieval was successful
		if (forum != null) {
			Set<String> commentIds = forum.getCommentIds();
			if (commentIds == null) {
				commentIds = new HashSet<String>();
			}
			commentIds.add((String) req.getArgs().get("commentId"));
			forum.setCommentIds(commentIds);
			dbMapper.save(forum);
		}
		return forum;
	}
	
	/**
	 * Removes a comment id to the set of comment ids for a Forum object
	 * 
	 * @param Request request containing data for object update
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the updated Forum object
	 */
	@Override
	public ChatterForum removeCommentFromForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateCommentRequest(req);
		
		// Attempt to retrieve Forum object from DB
		ChatterForum forum = dbMapper.load(ChatterForum.class,
				(String) req.getArgs().get("forumId"));
		
		// Attempt to add comment to Forum object if retrieval was successful
		if (forum != null) {
			Set<String> commentIds = forum.getCommentIds();
			commentIds.remove((String) req.getArgs().get("commentId"));
			
			// If the resultant list is now empty, null the reference to it
			if(commentIds.isEmpty()) {
				forum.setCommentIds(null);
			}
			else {
				forum.setCommentIds(commentIds);
			}
			
			// Save updated Forum object
			dbMapper.save(forum);
		}
		return forum;
	}
	
	/**
	 * Queries the ChatterForum DB table using the global secondary
	 * index attribute (createdBy). Results are returned in pages of
	 * 20 results per page.
	 * 
	 * @param req the request to process
	 * @return ForumResultPage
	 */
	@Override
	public ForumResultPage queryByCreator(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException,
		PropertyRetrievalException{
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateQueryByCreatorRequest(req);
		
		ForumResultPage forumResultPage = null;
		QueryResultPage<ChatterForum> resultPage = null;
		
		String creator = (String) req.getArgs().get("createdBy");
		//Long timeStampFrom = req.getTimeStampFrom();
		//Long timeStampTo = req.getTimeStampTo();
		String exclusiveStartVal = (String) req.getArgs().get("exclusiveStartVal");
		Map<String, AttributeValue> exclusiveStartKey = null;
		
		if (exclusiveStartVal != null && !exclusiveStartVal.isEmpty()) {
			// Need to create an attribute value and exclusiveStartKey
			AttributeValue attVal = new AttributeValue();
			attVal.setS(exclusiveStartVal);
			
			exclusiveStartKey = new HashMap<>();
			exclusiveStartKey.put("forum_id", attVal);
		}
		
		ChatterForum forum = new ChatterForum();
		forum.setCreatedBy(creator);
		
		//Create query expression and set properties
		DynamoDBQueryExpression<ChatterForum> query = new DynamoDBQueryExpression<>();
		query.setHashKeyValues(forum);
		query.setIndexName("created_by_index");
		query.setLimit(Integer.parseInt(propsResolver.getProperty("queryLimit")));
		query.setConsistentRead(false);
		
		//TODO: Need to set global index range key condition here...
		
		//Set exclusive start key
		if (exclusiveStartKey != null) {
			query.setExclusiveStartKey(exclusiveStartKey);
		}
		
		resultPage = dbMapper.queryPage(ChatterForum.class, query);
		
		if (resultPage != null) {
			forumResultPage = new ForumResultPage(
					resultPage.getResults(),
					resultPage.getLastEvaluatedKey(),
					resultPage.getCount());
		}
		
		return forumResultPage;
	}
	
	/**
	 * Queries ChatterForum DB table using global secondary index
	 * attribute (title). Results are returned in pages of 20 results
	 * per page.
	 * 
	 * @param req the requst to process
	 * @return ForumResultPage
	 */
	@Override
	public ForumResultPage queryByTitle(ForumCRUDRequest req) throws
			RequestValidationException, AmazonServiceException, AmazonClientException,
			PropertyRetrievalException{
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateQueryByTitleRequest(req);
		
		ForumResultPage forumResultPage = null;
		QueryResultPage<ChatterForum> queryResultPage = null;
		String title = (String) req.getArgs().get("title");
		String exclusiveStartVal = (String) req.getArgs().get("exclusiveStartVal");
		Map<String, AttributeValue> exclusiveStartKey = null;
		
		if (exclusiveStartVal != null && !exclusiveStartVal.isEmpty()) {
			// Need to create an attribute value and exclusive start key
			AttributeValue attVal = new AttributeValue();
			attVal.setS(exclusiveStartVal);
			
			exclusiveStartKey = new HashMap<>();
			exclusiveStartKey.put("forumId", attVal);
		}
		
		ChatterForum forum = new ChatterForum();
		forum.setTitle(title);
		
		//Create DB query expression and set properties
		DynamoDBQueryExpression<ChatterForum> query = new DynamoDBQueryExpression<>();
		query.setHashKeyValues(forum);
		query.setIndexName("title_index");
		query.setLimit(Integer.parseInt(propsResolver.getProperty("queryLimit")));
		query.setConsistentRead(false);
		
		//Set exclusive start key
		if (exclusiveStartKey != null) {
			query.setExclusiveStartKey(exclusiveStartKey);
		}
		
		queryResultPage = dbMapper.queryPage(ChatterForum.class, query);
		
		if (queryResultPage != null) {
			forumResultPage = new ForumResultPage(
					queryResultPage.getResults(),
					queryResultPage.getLastEvaluatedKey(),
					queryResultPage.getCount());
		}
		return forumResultPage;
	}
	
	/**
	 * Deletes a ChatterForum instance from the DB table. Forum instance is
	 * retrieved using the argument forumId and then deleted from the DB.
	 * 
	 * @param req the request to process
	 * @return a flag indicating whether the delete operation completed
	 * successfully or not
	 */
	@Override
	public boolean deleteForum(ForumCRUDRequest req) throws
			RequestValidationException, AmazonServiceException, AmazonClientException {
		
		// Validate incoming request
		ForumCRUDRequestValidator.validateRetrieveForumRequest(req);
		
		// Attempt to retrieve requested forum instance to delete
		boolean opSuccess = false;
		ChatterForum forumToDelete = dbMapper.load(ChatterForum.class,
				req.getArgs().get("forumId"));
		
		if (forumToDelete != null) {
			// Attempt to delete forum instance
			dbMapper.delete(forumToDelete);
			opSuccess = true;
		}
		return opSuccess;
	}
	
	/**
	 * Gathers data about this service and returns it in an object wrapper
	 * @return ServicePropsResponse
	 */
	public ServicePropsResponse getServiceProperties() throws PropertyRetrievalException {
		return new ServicePropsResponse(
				this.propsResolver.getProperty("service.env"),
				this.propsResolver.getProperty("service.name"),
				this.propsResolver.getProperty("service.description"), 
				this.propsResolver.getProperty("service.version"));
	}
}

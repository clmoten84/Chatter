package com.chatter.forumservice.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chatter.forumservice.exceptions.RequestValidationException;
import com.chatter.forumservice.model.ChatterForum;
import com.chatter.forumservice.requests.Request;
import com.chatter.forumservice.requests.RequestValidator;
import com.chatter.forumservice.responses.ForumResultPage;
import com.chatter.forumservice.responses.ServicePropsResponse;
import com.chatter.forumservice.util.PropertiesResolver;

/**
 * Implementation of data access interface. Implements methods to
 * access Chatter Forum data in the database.
 * @author coreym
 *
 */
public class ForumDAOImpl implements ForumDAO{
	
	private AmazonDynamoDBClient dbClient;
	private PropertiesResolver propsResolver;
	private DynamoDBMapper dbMapper;
	
	public ForumDAOImpl() {
		//Create DynamoDB client
		ProfileCredentialsProvider credProvider = new ProfileCredentialsProvider();
		dbClient = new AmazonDynamoDBClient(credProvider);
		
		//Set DB end-point from properties file
		propsResolver = new PropertiesResolver("service.properties");
		dbClient.setEndpoint(propsResolver.getProperty("aws.dynamodb.endpoint"));
		
		//Create DB mapper object
		dbMapper = new DynamoDBMapper(dbClient, credProvider);
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
	public ChatterForum createForum(Request req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		if(RequestValidator.validateCreateForumRequest(req)){
			//Create ChatterForum object using request parameters
			ChatterForum forum = new ChatterForum();
			forum.setCreatedBy(req.getArgs().get("createdBy"));
			forum.setTitle(req.getArgs().get("title"));
			forum.setTimeStamp(new Date().getTime());
			forum.setCommentIds(null);
			
			//Save ChatterForum object to DB
			dbMapper.save(forum);
			return forum;
		}
		else {
			throw new RequestValidationException("ERROR: Create Forum request "
					+ "contained invalid or NULL values for required parameters.");
		}
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
	public ChatterForum retrieveForumById(Request req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		if(RequestValidator.validateRetrieveForumRequest(req)) {
			//Attempt to retrieve Forum object from DB
			ChatterForum forum = null;
			forum = dbMapper.load(ChatterForum.class, req.getArgs().get("forumId"));
			return forum;
		}
		else {
			throw new RequestValidationException("ERROR: Retrieve Forum request"
					+ " contained invalid or NULL values for required parameters.");
		}
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
	public ChatterForum updateForum(Request req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ChatterForum forum = null;
		if(RequestValidator.validateRetrieveForumRequest(req)) {
			// Attempt to retrieve Forum object from DB
			forum = dbMapper.load(ChatterForum.class, req.getArgs().get("forumId"));
			
			// Attempt to update Forum object if retrieval was successful
			if(forum != null) {
				if (req.getArgs().get("titleUpdate") != null) {
					forum.setTitle(req.getArgs().get("titleUpdate"));
				}
				dbMapper.save(forum);
			}
		}
		else {
			throw new RequestValidationException("ERROR: Update Forum request"
					+ " contained invalid or NULL values for required parameters.");
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
	public ChatterForum addCommentToForum(Request req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ChatterForum forum = null;
		if (RequestValidator.validateCommentRequest(req)) {
			// Attempt to retrieve Forum object from DB
			forum = dbMapper.load(ChatterForum.class, req.getArgs().get("forumId"));
			
			// Attempt to add comment to Forum object if retrieval was successful
			if (forum != null) {
				Set<String> commentIds = forum.getCommentIds();
				if (commentIds == null) {
					commentIds = new HashSet<String>();
				}
				commentIds.add(req.getArgs().get("commentId"));
				forum.setCommentIds(commentIds);
				dbMapper.save(forum);
			}
		}
		else {
			throw new RequestValidationException("ERROR: Add comment request"
					+ " contained invalid or NULL values for required parameters.");
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
	public ChatterForum removeCommentFromForum(Request req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ChatterForum forum = null;
		if (RequestValidator.validateCommentRequest(req)) {
			// Attempt to retrieve Forum object from DB
			forum = dbMapper.load(ChatterForum.class, req.getArgs().get("forumId"));
			
			// Attempt to add comment to Forum object if retrieval was successful
			if (forum != null) {
				Set<String> commentIds = forum.getCommentIds();
				commentIds.remove(req.getArgs().get("commentId"));
				
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
		}
		else {
			throw new RequestValidationException("ERROR: Remove comment request"
					+ " contained invalid or NULL values for required parameters.");
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
	public ForumResultPage queryByCreator(Request req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ForumResultPage forumResultPage = null;
		if (RequestValidator.validateQueryByCreatorRequest(req)) {
			QueryResultPage<ChatterForum> resultPage = null;
			
			String creator = req.getArgs().get("createdBy");
			//Long timeStampFrom = req.getTimeStampFrom();
			//Long timeStampTo = req.getTimeStampTo();
			String exclusiveStartVal = req.getArgs().get("exclusiveStartVal");
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
		}
		else {
			throw new RequestValidationException("ERROR: Query by creator request"
					+ " contained invalid or NULL values for required parameters.");
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
	public ForumResultPage queryByTitle(Request req) throws
			RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ForumResultPage forumResultPage = null;
		if (RequestValidator.validateQueryByTitleRequest(req)) {
			QueryResultPage<ChatterForum> queryResultPage = null;
			String title = req.getArgs().get("title");
			String exclusiveStartVal = req.getArgs().get("exclusiveStartVal");
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
		}
		else {
			throw new RequestValidationException("ERROR: Query by title request"
					+ " contained invalid or NULL values for required parameters.");
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
	public boolean deleteForum(Request req) throws
			RequestValidationException, AmazonServiceException, AmazonClientException {
		boolean opSuccess = false;
		if (RequestValidator.validateRetrieveForumRequest(req)) {
			// Attempt to retrieve requested forum instance to delete
			ChatterForum forumToDelete = dbMapper.load(ChatterForum.class,
					req.getArgs().get("forumId"));
			
			if (forumToDelete != null) {
				// Attempt to delete forum instance
				dbMapper.delete(forumToDelete);
				opSuccess = true;
			}
		}
		else {
			throw new RequestValidationException("ERROR: Delete comment request"
					+ " contained invalid or NULL values for required parameters.");
		}
		return opSuccess;
	}
	
	/**
	 * Gathers data about this service and returns it in an object wrapper
	 * @return ServicePropsResponse
	 */
	public ServicePropsResponse getServiceProperties() {
		return new ServicePropsResponse(
				this.propsResolver.getProperty("service.env"),
				this.propsResolver.getProperty("service.name"),
				this.propsResolver.getProperty("service.description"), 
				this.propsResolver.getProperty("service.version"));
	}
}

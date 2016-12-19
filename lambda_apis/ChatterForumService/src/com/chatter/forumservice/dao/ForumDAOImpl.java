package com.chatter.forumservice.dao;

import java.util.Date;
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
import com.amazonaws.services.dynamodbv2.datamodeling.QueryResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.chatter.forumservice.exceptions.RequestValidationException;
import com.chatter.forumservice.requests.AddCommentRequest;
import com.chatter.forumservice.requests.CreateForumRequest;
import com.chatter.forumservice.requests.DeleteForumRequest;
import com.chatter.forumservice.requests.QueryByCreatorRequest;
import com.chatter.forumservice.requests.QueryByTitleRequest;
import com.chatter.forumservice.requests.RemoveCommentRequest;
import com.chatter.forumservice.requests.RequestValidator;
import com.chatter.forumservice.requests.RetrieveForumRequest;
import com.chatter.forumservice.requests.UpdateForumRequest;
import com.chatter.forumservice.responses.ForumResultPage;
import com.chatter.forumservice.responses.ServicePropsResponse;
import com.chatter.forumservice.util.PropertiesResolver;
import com.chatter.model.ChatterForum;

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
	 * @param CreateForumRequest request object containing data to create Forum
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the Forum object created
	 */
	@Override
	public ChatterForum createForum(CreateForumRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		if(RequestValidator.validateCreateForumRequest(req)){
			//Create ChatterForum object using request parameters
			ChatterForum forum = new ChatterForum();
			forum.setCreatedBy(req.getCreatedBy());
			forum.setTitle(req.getTitle());
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
	 * @param RetrieveForumRequest request containing data for object retrieval
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the retrieved Forum object
	 */
	@Override
	public ChatterForum retrieveForumById(RetrieveForumRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		if(RequestValidator.validateRetrieveForumRequest(req)) {
			//Attempt to retrieve Forum object from DB
			ChatterForum forum = null;
			forum = dbMapper.load(ChatterForum.class, req.getForumId());
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
	 * @param UpdateForumRequest request containing data for object update
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the updated Forum object
	 */
	@Override
	public ChatterForum updateForum(UpdateForumRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ChatterForum forum = null;
		if(RequestValidator.validateUpdateForumRequest(req)) {
			// Attempt to retrieve Forum object from DB
			forum = dbMapper.load(ChatterForum.class, req.getForumId());
			
			// Attempt to update Forum object if retrieval was successful
			if(forum != null) {
				if (req.getTitleUpdate() != null) {
					forum.setTitle(req.getTitleUpdate());
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
	 * @param AddCommentRequest request containing data for object update
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the updated Forum object
	 */
	@Override
	public ChatterForum addCommentToForum(AddCommentRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ChatterForum forum = null;
		if (RequestValidator.validateAddCommentRequest(req)) {
			// Attempt to retrieve Forum object from DB
			forum = dbMapper.load(ChatterForum.class, req.getForumId());
			
			// Attempt to add comment to Forum object if retrieval was successful
			if (forum != null) {
				Set<String> commentIds = forum.getCommentIds();
				if (commentIds == null) {
					commentIds = new HashSet<String>();
				}
				commentIds.add(req.getCommentId());
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
	 * @param RemoveCommentRequest request containing data for object update
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @return ChatterForum the updated Forum object
	 */
	@Override
	public ChatterForum removeCommentFromForum(RemoveCommentRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ChatterForum forum = null;
		if (RequestValidator.validateRemoveCommentRequest(req)) {
			// Attempt to retrieve Forum object from DB
			forum = dbMapper.load(ChatterForum.class, req.getForumId());
			
			// Attempt to add comment to Forum object if retrieval was successful
			if (forum != null) {
				Set<String> commentIds = forum.getCommentIds();
				commentIds.remove(req.getCommentId());
				
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
	public ForumResultPage queryByCreator(QueryByCreatorRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ForumResultPage forumResultPage = null;
		if (RequestValidator.validateQueryByCreatorRequest(req)) {
			QueryResultPage<ChatterForum> resultPage = null;
			
			String creator = req.getCreatedBy();
			//Long timeStampFrom = req.getTimeStampFrom();
			//Long timeStampTo = req.getTimeStampTo();
			Map<String, AttributeValue> exclusiveStartKey = req.getExclusiveStartKey();
			
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
			
			//If a page of results was successfully retrieved...
			if (resultPage != null && resultPage.getCount() > 0 &&
					resultPage.getCount() < query.getLimit()) {
				/*
				 * This condition indicates that there are no more results
				 * to be retrieved subsequent to this page of results. So
				 * the moreResults flag in the ForumResultPage object is set
				 * to false. This will inform the client that there are no
				 * more results to retrieve for this query and not to make
				 * an unnecessary request to retrieve anymore data.
				 */
				List<ChatterForum> forumResults = resultPage.getResults();
				Map<String, AttributeValue> lastEvaluatedKey = resultPage.getLastEvaluatedKey();
				
				forumResultPage = new ForumResultPage(forumResults, lastEvaluatedKey, 
						resultPage.getCount(), false);
			}
			else if (resultPage != null && resultPage.getCount() == 0) {
				/*
				 * This condition indicates that no results were returned 
				 * from the query. This condition also sets the moreResults
				 * flag in the ForumResultPage object to false.
				 */
				forumResultPage = new ForumResultPage(null, null, resultPage.getCount(),
						false);
			}
			else {
				/*
				 * This condition indicates that more results need to be
				 * retrieved to complete the query, so set the moreResults
				 * flag in the ForumResultPage object to true. This will
				 * inform the client that there are more results to retrieve
				 * that match the query. The client will need to make another
				 * request to retrieve the next page of results.
				 */
				List<ChatterForum> forumResults = resultPage.getResults();
				Map<String, AttributeValue> lastEvaluatedKey = resultPage.getLastEvaluatedKey();
				
				forumResultPage = new ForumResultPage(forumResults, lastEvaluatedKey, 
						resultPage.getCount(), true);
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
	public ForumResultPage queryByTitle(QueryByTitleRequest req) throws
			RequestValidationException, AmazonServiceException, AmazonClientException {
		
		ForumResultPage forumResultPage = null;
		if (RequestValidator.validateQueryByTitleRequest(req)) {
			QueryResultPage<ChatterForum> queryResultPage = null;
			String title = req.getTitle();
			Map<String, AttributeValue> exclusiveStartKey = req.getExclusiveStartKey();
			
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
			
			//If a page of results was successfully retrieved...
			if (queryResultPage != null && queryResultPage.getCount() > 0 &&
					queryResultPage.getCount() < query.getLimit()) {
				/*
				 * This condition indicates that there are no more results
				 * to be retrieved subsequent to this page of results. So
				 * the moreResults flag in the ForumResultPage object is set
				 * to false. This will inform the client that there are no
				 * more results to retrieve for this query and not to make
				 * an unnecessary request to retrieve anymore data.
				 */
				List<ChatterForum> forumResults = queryResultPage.getResults();
				Map<String, AttributeValue> lastEvaluatedKey = queryResultPage.getLastEvaluatedKey();
				
				forumResultPage = new ForumResultPage(forumResults, lastEvaluatedKey, 
						queryResultPage.getCount(), false);
			}
			else if (queryResultPage != null && queryResultPage.getCount() == 0) {
				/*
				 * This condition indicates that no results were returned 
				 * from the query. This condition also sets the moreResults
				 * flag in the ForumResultPage object to false.
				 */
				forumResultPage = new ForumResultPage(null, null, queryResultPage.getCount(),
						false);
			}
			else {
				/*
				 * This condition indicates that more results need to be
				 * retrieved to complete the query, so set the moreResults
				 * flag in the ForumResultPage object to true. This will
				 * inform the client that there are more results to retrieve
				 * that match the query. The client will need to make another
				 * request to retrieve the next page of results.
				 */
				List<ChatterForum> forumResults = queryResultPage.getResults();
				Map<String, AttributeValue> lastEvaluatedKey = queryResultPage.getLastEvaluatedKey();
				
				forumResultPage = new ForumResultPage(forumResults, lastEvaluatedKey, 
						queryResultPage.getCount(), true);
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
	public boolean deleteForum(DeleteForumRequest req) throws
			RequestValidationException, AmazonServiceException, AmazonClientException {
		boolean opSuccess = false;
		if (RequestValidator.validateDeleteForumRequest(req)) {
			// Attempt to retrieve requested forum instance to delete
			ChatterForum forumToDelete = dbMapper.load(ChatterForum.class, req.getForumId());
			
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

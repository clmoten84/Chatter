package com.chatter.forumservice.dao;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.chatter.forumservice.exceptions.RequestValidationException;
import com.chatter.forumservice.requests.AddCommentRequest;
import com.chatter.forumservice.requests.CreateForumRequest;
import com.chatter.forumservice.requests.DeleteForumRequest;
import com.chatter.forumservice.requests.QueryByCreatorRequest;
import com.chatter.forumservice.requests.QueryByTitleRequest;
import com.chatter.forumservice.requests.RequestValidator;
import com.chatter.forumservice.requests.RetrieveForumRequest;
import com.chatter.forumservice.requests.UpdateForumRequest;
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

	@Override
	public ChatterForum createForum(CreateForumRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		if(RequestValidator.validateCreateForumRequest(req)){
			//Create ChatterForum object using request parameters
			ChatterForum forum = new ChatterForum();
			forum.setCreatedBy(req.getCreatedBy());
			forum.setTitle(req.getTitle());
			forum.setTimeStamp(new Date().getTime());
			forum.setCommentIds(new HashSet<String>());
			
			//Save ChatterForum object to DB
			dbMapper.save(forum);
			return forum;
		}
		else {
			throw new RequestValidationException("ERROR: Create Forum request "
					+ "contained invalid or NULL values for required parameters.");
		}
	}
	
	@Override
	public ChatterForum retrieveForumById(RetrieveForumRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
		if(RequestValidator.validateRetrieveForumRequest(req)) {
			
		}
		else {
			throw new IllegalArgumentException("ERROR: Retrieve Froum request");
		}
		return null;
	}
	
	@Override
	public ChatterForum updateForum(UpdateForumRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		return null;
	}
	
	@Override
	public ChatterForum addCommentToForum(AddCommentRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		return null;
	}
	
	@Override
	public List<ChatterForum> queryByCreator(QueryByCreatorRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		return null;
	}
	
	@Override
	public List<ChatterForum> queryByTitle(QueryByTitleRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		return null;
	}
	
	@Override
	public void deleteForum(DeleteForumRequest req) throws
		RequestValidationException, AmazonServiceException, AmazonClientException {
		
	}
}

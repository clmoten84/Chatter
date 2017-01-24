package com.chatter.dbservice.dao;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterForum;
import com.chatter.dbservice.requests.ForumCRUDRequest;
import com.chatter.dbservice.responses.ForumResultPage;
import com.chatter.dbservice.responses.ServicePropsResponse;

/**
 * ForumDAO
 * @author coreym
 *
 * Provides data access methods for Chatter Forum data
 */
public interface ForumDAO {
	/**
	 * Create a Chatter Forum instance and save to the database
	 * 
	 * @param req the request containing all the data necessary to create
	 * a new Chatter Forum
	 * 
	 * @return the Chatter Forum that was created
	 */
	public ChatterForum createForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Retrieve a Chatter Forum instance from the database
	 * 
	 * @param req the request containing all the data necessary to retrieve
	 * a Chatter Forum instance 
	 * 
	 * @return the retrieve Chatter Forum object
	 */
	public ChatterForum retrieveForumById(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Update a Chatter Forum instance in the database
	 * 
	 * @param req the request containing all the data necessary to update
	 * an existing Chatter Forum
	 * 
	 * @return the updated Chatter Forum object
	 */
	public ChatterForum updateForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Update a Chatter Forum instance by adding a new comment id
	 * to the existing list of comment ids in a Chatter Forum
	 * 
	 * @param req the request containing all the data necessary to add
	 * a comment to a Chatter Forum objects comment id list
	 * 
	 * @return the update Chatter Forum object
	 */
	public ChatterForum addCommentToForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Update a Chatter Forum instance by removing an existing comment
	 * id from the existing list of comment ids in a Chatter Forum
	 * 
	 * @param req the request containing all the data necessary to remove
	 * a comment from a Chatter Forum objects comment id list
	 * 
	 * @return the updated Chatter Forum object
	 * 
	 * @throws RequestValidationException
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 */
	public ChatterForum removeCommentFromForum(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Delete a Chatter Forum instance from the database
	 * 
	 * @param req the request containing all the data necessary to delete
	 * a ChatterForum instance
	 * 
	 * @return a flag indicating operation success
	 */
	public boolean deleteForum(ForumCRUDRequest req) throws 
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Query for Chatter Forum instances that were created by the argument
	 * creator
	 * 
	 * @param req the request containing all the data necessary to perform
	 * the query
	 * 
	 * @return a ForumResultPage object 
	 */
	public ForumResultPage queryByCreator(ForumCRUDRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException, PropertyRetrievalException;
	
	/**
	 * Query for Chatter Forum instance with a title matching the argument
	 * title
	 * 
	 * @param req the request containing all the data necessary to perform
	 * the query
	 * 
	 * @return a ForumResultPage object
	 */
	public ForumResultPage queryByTitle(ForumCRUDRequest req) throws 
		RequestValidationException, AmazonServiceException,
		AmazonClientException, PropertyRetrievalException;
	
	/**
	 * Gathers data about this service and returns in an object wrapper
	 * @return ServicePropsResponse
	 */
	public ServicePropsResponse getServiceProperties() throws PropertyRetrievalException;
}

package com.chatter.forumservice.dao;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.chatter.forumservice.exceptions.RequestValidationException;
import com.chatter.forumservice.requests.AddCommentRequest;
import com.chatter.forumservice.requests.CreateForumRequest;
import com.chatter.forumservice.requests.DeleteForumRequest;
import com.chatter.forumservice.requests.QueryByCreatorRequest;
import com.chatter.forumservice.requests.QueryByTitleRequest;
import com.chatter.forumservice.requests.RetrieveForumRequest;
import com.chatter.forumservice.requests.UpdateForumRequest;
import com.chatter.model.ChatterForum;

/**
 * Contains methods for accessing Forum data in the database.
 * @author coreym
 *
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
	public ChatterForum createForum(CreateForumRequest req) throws
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
	public ChatterForum retrieveForumById(RetrieveForumRequest req) throws
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
	public ChatterForum updateForum(UpdateForumRequest req) throws
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
	public ChatterForum addCommentToForum(AddCommentRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Delete a Chatter Forum instance from the database
	 * 
	 * @param req the request containing all the data necessary to delete
	 * a Chatter Forum instance
	 */
	public void deleteForum(DeleteForumRequest req) throws 
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Query for Chatter Forum instances that were created by the argument
	 * creator
	 * 
	 * @param req the request containing all the data necessary to perform
	 * the query
	 * 
	 * @return a list of Chatter Forum objects 
	 */
	public List<ChatterForum> queryByCreator(QueryByCreatorRequest req) throws
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
	
	/**
	 * Query for Chatter Forum instance with a title matching the argument
	 * title
	 * 
	 * @param req the request containing all the data necessary to perform
	 * the query
	 * 
	 * @return a list of Chatter Forum objects
	 */
	public List<ChatterForum> queryByTitle(QueryByTitleRequest req) throws 
		RequestValidationException, AmazonServiceException,
		AmazonClientException;
}

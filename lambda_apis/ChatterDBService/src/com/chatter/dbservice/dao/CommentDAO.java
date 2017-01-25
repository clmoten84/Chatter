package com.chatter.dbservice.dao;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterComment;
import com.chatter.dbservice.requests.CommentCRUDRequest;
import com.chatter.dbservice.responses.CommentResultPage;

/**
 * CommentDAO
 * @author coreym
 *
 * Provides data access methods for CRUDing
 * ChatterComment data.
 */
public interface CommentDAO {

	/**
	 * Create and save a new ChatterComment object to database
	 * 
	 * @param request the request to process
	 * @return the created ChatterComment object
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterComment createComment(CommentCRUDRequest request) throws
		AmazonServiceException, AmazonClientException,
			RequestValidationException;
	
	/**
	 * Retrieve a ChatterComment object from the database using
	 * an argument comment id value.
	 * 
	 * @param request the request to process
	 * @return the retrieved ChatterComment object
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterComment retrieveComment(CommentCRUDRequest request) throws
		AmazonServiceException, AmazonClientException,
			RequestValidationException;
	
	/**
	 * Update an existing ChatterComment object in the database
	 * with new values.
	 * 
	 * @param request the request to process
	 * @return updated ChatterComment object
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterComment updateComment(CommentCRUDRequest request) throws
	AmazonServiceException, AmazonClientException,
		RequestValidationException;
	
	/**
	 * Delete an existing ChatterComment object from the database.
	 * 
	 * @param request the request to process
	 * @return a flag indicating success/failure of the delete operation
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public boolean deleteComment(CommentCRUDRequest request) throws
	AmazonServiceException, AmazonClientException,
		RequestValidationException;
	
	/**
	 * Queries ChatterComment DB table using argument creator value
	 * 
	 * @param request the request to process
	 * @return CommentResultPage object
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 * @throws PropertyRetrievalException
	 */
	public CommentResultPage queryByCreator(CommentCRUDRequest request) throws
		AmazonServiceException, AmazonClientException,
			RequestValidationException, PropertyRetrievalException;
	
	/**
	 * Queries ChatterComment DB table using argument forum id value
	 * 
	 * @param request the request to process
	 * @return CommentResultPage object
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 * @throws PropertyRetrievalException
	 */
	public CommentResultPage queryByForum(CommentCRUDRequest request) throws
	AmazonServiceException, AmazonClientException,
		RequestValidationException, PropertyRetrievalException;
	
	/**
	 * Retrieve a batch of ChatterComment objects from database using
	 * a list of argument comment ids.
	 * 
	 * @param request the request to process
	 * @return a list of ChatterComment object retrieved from database
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public List<ChatterComment> batchRetrieve(CommentCRUDRequest request) throws
		AmazonServiceException, AmazonClientException,
			RequestValidationException;
	
	/**
	 * Delete a batch ChatterComment objects from database
	 * 
	 * @param request the request to process
	 * @return a list of comment ids that were successfully deleted from database
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public List<String> batchDelete(CommentCRUDRequest request) throws
		AmazonServiceException, AmazonClientException,
			RequestValidationException;
}

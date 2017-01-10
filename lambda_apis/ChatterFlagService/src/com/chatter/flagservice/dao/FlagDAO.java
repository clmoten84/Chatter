package com.chatter.flagservice.dao;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.chatter.flagservice.exceptions.PropertyRetrievalException;
import com.chatter.flagservice.exceptions.RequestValidationException;
import com.chatter.flagservice.model.ChatterFlag;
import com.chatter.flagservice.requests.Request;
import com.chatter.flagservice.responses.ServicePropsResponse;

/**
 * FlagDAO
 * @author coreym
 * 
 * Contains methods for accessing Chatter Flag data
 * in the database.
 */
public interface FlagDAO {

	/**
	 * Create and save a new Chatter Flag object to the database.
	 * 
	 * @param req the Request to process
	 * @return ChatterFlag
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterFlag createFlag(Request req) throws
		AmazonServiceException, AmazonClientException, 
		RequestValidationException;
	
	/**
	 * Retrieve a Chatter Flag object from the database by its id.
	 * 
	 * @param req the Request to process
	 * @return ChatterFlag
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterFlag retrieveFlag(Request req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException;
	
	/**
	 * Update an existing Chatter Flag object in the database.
	 * 
	 * @param req the Request to process
	 * @return ChatterFlag
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterFlag updateFlag(Request req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException;
	
	/**
	 * Deletes a Chatter Flag object from the database.
	 * 
	 * @param req the Request to process
	 * @return a boolean flag to indicate success of operation
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public boolean deleteFlag(Request req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException;
	
	/**
	 * Gathers data about this service and returns it in an object
	 * wrapper.
	 * 
	 * @return ServicePropsResponse
	 * @throws PropertyRetrievalException 
	 */
	public ServicePropsResponse getServiceProperties() throws PropertyRetrievalException;
}

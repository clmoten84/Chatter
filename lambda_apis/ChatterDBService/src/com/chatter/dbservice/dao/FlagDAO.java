package com.chatter.dbservice.dao;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterFlag;
import com.chatter.dbservice.requests.FlagCRUDRequest;
import com.chatter.dbservice.responses.ServicePropsResponse;

/**
 * FlagDAO
 * @author coreym
 * 
 * Provides data access methods for Chatter Flag data
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
	public ChatterFlag createFlag(FlagCRUDRequest req) throws
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
	public ChatterFlag retrieveFlag(FlagCRUDRequest req) throws
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
	public ChatterFlag updateFlag(FlagCRUDRequest req) throws
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
	public boolean deleteFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException;
	
	/**
	 * Retrieves a batch of ChatterFlag objects from database
	 * using a list of flag ids.
	 * 
	 * @param req the request to process
	 * @return a list of ChatterFlag objects
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public List<ChatterFlag> batchRetrieveFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException;
	
	/**
	 * Deletes a batch of ChatterFlag objects from database
	 * 
	 * @param req the request to process
	 * @return a list of ChatterFlag ids that were successfully deleted
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public List<String> batchDeleteFlag(FlagCRUDRequest req) throws
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

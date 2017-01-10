package com.chatter.flagservice.dao;

import java.util.Date;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.chatter.flagservice.exceptions.PropertyRetrievalException;
import com.chatter.flagservice.exceptions.RequestValidationException;
import com.chatter.flagservice.model.ChatterFlag;
import com.chatter.flagservice.requests.Request;
import com.chatter.flagservice.requests.RequestValidator;
import com.chatter.flagservice.responses.ServicePropsResponse;
import com.chatter.flagservice.util.PropertiesResolver;

/**
 * FlagDAOImpl
 * @author coreym
 *
 * Implementation of Chatter Flag data access object
 * interface.
 */
public class FlagDAOImpl implements FlagDAO{

	private AmazonDynamoDBClient dbClient;
	private PropertiesResolver propsResolver;
	private DynamoDBMapper dbMapper;
	
	public FlagDAOImpl() throws PropertyRetrievalException{
		// Create DynamoDB client
		dbClient = new AmazonDynamoDBClient();
		
		// Set DB end-point from properties file
		propsResolver = new PropertiesResolver("service.properties");
		dbClient.setEndpoint(propsResolver.getProperty("aws.dynamodb.endpoint"));
		
		// Create DB mapper
		dbMapper = new DynamoDBMapper(dbClient);
	}
	
	/**
	 * Create and save a new Chatter Flag object to the database
	 * 
	 * @param req the Request to process
	 * @return ChatterFlag
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterFlag createFlag(Request req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		if (RequestValidator.validateCreateFlagReqeust(req)) {
			// Create Chatter Flag object using request parameters
			ChatterFlag flag = new ChatterFlag();
			flag.setCreatedBy(req.getArgs().get("createdBy"));
			flag.setForumId(req.getArgs().get("forumId"));
			flag.setCommentId(req.getArgs().get("commentId"));
			flag.setFlagDescription(req.getArgs().get("flagDescription"));
			flag.setTimeStamp(new Date().getTime());
			
			// Save ChatterFlag to DB
			dbMapper.save(flag);
			return flag;
		}
		else {
			throw new RequestValidationException("ERROR: Create Chatter Flag request "
					+ "contained invalid or NULL values for required parameters.");
		}
	}
	
	/**
	 * Retrieve a Chatter Flag object from the database
	 * 
	 * @param req the Request to process
	 * @return ChatterFlag
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterFlag retrieveFlag(Request req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		if (RequestValidator.validateRetrieveFlagRequest(req)) {
			// Attempt to retrieve Chatter Flag object from DB
			ChatterFlag flag = null;
			flag = dbMapper.load(ChatterFlag.class, req.getArgs().get("flagId"));
			return flag;
		}
		else {
			throw new RequestValidationException("ERROR: Retrieve Chatter Flag request "
					+ "contained invalid or NULL values for required parameters.");
		}
	}
	
	/**
	 * Update an existing Chatter Flag object in the database
	 * 
	 * @param req the Request to process
	 * @return ChatterFlag
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public ChatterFlag updateFlag(Request req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		if (RequestValidator.validateRetrieveFlagRequest(req)) {
			ChatterFlag flag = null;
			boolean flagChanged = false;
			
			// Attempt to retrieve requested Chatter Flag object from DB
			flag = dbMapper.load(ChatterFlag.class, req.getArgs().get("flagId"));
			
			// Attempt to update Chatter Flag object
			if (flag != null) {
				String descUpdate = req.getArgs().get("flagDescriptionUpdate");
				if (descUpdate != null && !descUpdate.isEmpty()) {
					flag.setFlagDescription(descUpdate);
					flagChanged = true;
				}
				
				// If any changes were made to the Flag object
				// call to save changes.
				if (flagChanged)
					dbMapper.save(flag);
			}
			
			return flag;
		}
		else {
			throw new RequestValidationException("ERROR: Update Chatter Flag request "
					+ "contained invalid or NULL values for required parameters.");
		}
	}
	
	/**
	 * Delete an existing Chatter Flag object from the database
	 * 
	 * @param req the Request to process
	 * @return boolean flag indicating success of operation
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public boolean deleteFlag(Request req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		if (RequestValidator.validateRetrieveFlagRequest(req)) {
			boolean deleteSuccess = false;
			
			// Attempt to retrieve requested Chatter Flag object from DB
			ChatterFlag flag = dbMapper.load(ChatterFlag.class, 
					req.getArgs().get("flagId"));
			
			// If Chatter Flag successfully retrieved delete it
			if (flag != null) {
				dbMapper.delete(flag);
				deleteSuccess = true;
			}
			
			return deleteSuccess;
		}
		else {
			throw new RequestValidationException("ERROR: Delete Chatter Flag request "
					+ "contained invalid or NULL values for required parameters.");
		}
	}
	
	/**
	 * Gathers data about this service and returns it in an object wrapper
	 * @return ServicePropsResponse
	 */
	public ServicePropsResponse getServiceProperties() throws PropertyRetrievalException{
		return new ServicePropsResponse(
				this.propsResolver.getProperty("service.env"),
				this.propsResolver.getProperty("service.name"),
				this.propsResolver.getProperty("service.description"),
				this.propsResolver.getProperty("service.version"));
	}
}

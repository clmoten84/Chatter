package com.chatter.dbservice.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.KeyPair;
import com.chatter.dbservice.dao.FlagDAO;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterFlag;
import com.chatter.dbservice.requests.FlagCRUDRequest;
import com.chatter.dbservice.requests.validators.FlagCRUDRequestValidator;
import com.chatter.dbservice.responses.ServicePropsResponse;
import com.chatter.dbservice.util.PropertiesResolver;

/**
 * FlagDAOImpl
 * @author coreym
 *
 * Provides data access methods for Chatter Flag
 * data.
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
	public ChatterFlag createFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		//Validate incoming request
		FlagCRUDRequestValidator.validateCreateFlagReqeust(req);
		
		// Create Chatter Flag object using request parameters
		ChatterFlag flag = new ChatterFlag();
		flag.setCreatedBy((String) req.getArgs().get("createdBy"));
		flag.setForumId((String) req.getArgs().get("forumId"));
		flag.setCommentId((String) req.getArgs().get("commentId"));
		flag.setFlagDescription((String) req.getArgs().get("flagDescription"));
		flag.setTimeStamp(new Date().getTime());
		
		// Save ChatterFlag to DB
		dbMapper.save(flag);
		return flag;
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
	public ChatterFlag retrieveFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		// Validate incoming request
		FlagCRUDRequestValidator.validateRetrieveFlagRequest(req);
		
		// Attempt to retrieve Chatter Flag object from DB
		ChatterFlag flag = null;
		flag = dbMapper.load(ChatterFlag.class, (String) req.getArgs().get("flagId"));
		return flag;
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
	public ChatterFlag updateFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		// Validate incoming request
		FlagCRUDRequestValidator.validateRetrieveFlagRequest(req);
		
		ChatterFlag flag = null;
		boolean flagChanged = false;
		
		// Attempt to retrieve requested Chatter Flag object from DB
		flag = dbMapper.load(ChatterFlag.class, (String) req.getArgs().get("flagId"));
		
		// Attempt to update Chatter Flag object
		if (flag != null) {
			String descUpdate = (String) req.getArgs().get("flagDescriptionUpdate");
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
	
	/**
	 * Delete an existing Chatter Flag object from the database
	 * 
	 * @param req the Request to process
	 * @return boolean flag indicating success of operation
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	public boolean deleteFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		//Validate incoming request
		FlagCRUDRequestValidator.validateRetrieveFlagRequest(req);
		
		boolean deleteSuccess = false;
		
		// Attempt to retrieve requested Chatter Flag object from DB
		ChatterFlag flag = dbMapper.load(ChatterFlag.class, 
				(String) req.getArgs().get("flagId"));
		
		// If Chatter Flag successfully retrieved delete it
		if (flag != null) {
			dbMapper.delete(flag);
			deleteSuccess = true;
		}
		
		return deleteSuccess;
	}
	
	/**
	 * Retrieve a batch of Flag objects from the database
	 * 
	 * @param req the Request to process
	 * @return List of ChatterFlag objects
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	@SuppressWarnings("unchecked")
	public List<ChatterFlag> batchRetrieveFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		// Validate incoming request
		FlagCRUDRequestValidator.validateBatchRetrieveFlagRequest(req);
		
		// Initialize list of results
		List<ChatterFlag> flagResults = null;
		
		// Create arguments for batch load function
		List<String> flagIds = (List<String>) req.getArgs().get("flagIds");
		Map<Class<?>, List<KeyPair>> primaryKeyMap = new HashMap<>();
		primaryKeyMap.put(ChatterFlag.class, this.generateHashKeyPairList(flagIds));
		
		// Attempt to retrieve Chatter Flag objects from database
		Map<String, List<Object>> flagData = dbMapper.batchLoad(primaryKeyMap);
		
		if (flagData != null) {
			// Need to cast object to ChatterFlag and add to 
			// list to return.
			flagResults = new ArrayList<>();
			List<Object> objectResults = flagData.get("Chatter_Flag");
			for (Object object : objectResults) {
				ChatterFlag flag = (ChatterFlag) object;
				flagResults.add(flag);
			}
		}
		
		return flagResults;
	}
	
	/**
	 * Delete a batch of Chatter Flag objects from the database
	 * 
	 * @param req the Request to process
	 * @return a list of ChatterFlag ids that were successfully deleted
	 * @throws AmazonServiceException
	 * @throws AmazonClientException
	 * @throws RequestValidationException
	 */
	@SuppressWarnings("unchecked")
	public List<String> batchDeleteFlag(FlagCRUDRequest req) throws
		AmazonServiceException, AmazonClientException,
		RequestValidationException {
		
		// Validate incoming request
		FlagCRUDRequestValidator.validateBatchRetrieveFlagRequest(req);
		
		// Initialize result lists
		List<String> resultFlagIds = null;
		List<ChatterFlag> flagResults = null;
		
		// Create batch load arguments
		List<String> flagIds = (List<String>) req.getArgs().get("flagIds");
		Map<Class<?>, List<KeyPair>> primaryKeyMap = new HashMap<>();
		primaryKeyMap.put(ChatterFlag.class, this.generateHashKeyPairList(flagIds));
		
		// Attempt to retrieve Chatter Flag objects from database
		Map<String, List<Object>> flagData = dbMapper.batchLoad(primaryKeyMap);
		
		if (flagData != null) {
			// Need to cast object to ChatterFlag and add to 
			// list to return.
			flagResults = new ArrayList<>();
			List<Object> objectResults = flagData.get("Chatter_Flag");
			for (Object object : objectResults) {
				ChatterFlag flag = (ChatterFlag) object;
				flagResults.add(flag);
			}
			
			if (flagResults.size() > 0) {
				dbMapper.batchDelete(flagResults);
				resultFlagIds = new ArrayList<>();
				for (ChatterFlag flag : flagResults) {
					resultFlagIds.add(flag.getFlagId());
				}
			}
		}
		
		return resultFlagIds;
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
	
	/**
	 * Generate a list of key pairs from the argument list of
	 * hash keys.
	 * @param hashKeys
	 * @return
	 */
	private List<KeyPair> generateHashKeyPairList(List<String> hashKeys) {
		List<KeyPair> keyPairs = new ArrayList<>();
		for (String hashKey : hashKeys) {
			keyPairs.add(new KeyPair().withHashKey(hashKey));
		}
		
		return keyPairs;
	}
}

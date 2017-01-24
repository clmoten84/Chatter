package com.chatter.dbservice.requests.validators;

import java.util.Set;

import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.requests.FlagCRUDRequest;

/**
 * FlagCRUDRequestValidator
 * @author coreym
 *
 * Provides methods for validating incoming Chatter Flag
 * data requests.
 */
public class FlagCRUDRequestValidator {
	/**
	 * Validate a request to create and save a new Flag object to
	 * the database.
	 * 
	 * @param req the Request to validate
	 * @return
	 */
	public static void validateCreateFlagReqeust(FlagCRUDRequest req)
			throws RequestValidationException {
		boolean validReq = true;
	
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String createdBy = (String) req.getArgs().get("createdBy");
					String forumId = (String) req.getArgs().get("forumId");
					String commentId = (String) req.getArgs().get("commentId");
					String flagDescription = (String) req.getArgs().get("flagDescription");
				
					if (createdBy == null || createdBy.isEmpty())
						validReq = false;
					
					if (forumId == null || forumId.isEmpty())
						validReq = false;
					
					if (commentId == null || commentId.isEmpty())
						validReq = false;
					
					if (flagDescription == null || flagDescription.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Chatter Flag data request "
					+ "contained invalid or NULL values for required parameters.");
				}
			}
			else {
				validReq = false;
			}
		}
		else {
			validReq = false;
		}
		
		if (!validReq)
			throw new RequestValidationException("ERROR: Chatter Flag data request "
					+ "contained invalid or NULL values for required parameters.");
	}
	
	/**
	 * Validate a request to retrieve a Flag object from the database
	 * using an argument flag id.
	 * 
	 * @param req the Request to validate
	 * @return
	 */
	public static void validateRetrieveFlagRequest(FlagCRUDRequest req) 
			throws RequestValidationException {
		boolean validReq = true;
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String flagId = (String) req.getArgs().get("flagId");
					if (flagId == null || flagId.isEmpty())
						validReq = false;
					}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Chatter Flag data request "
							+ "contained invalid or NULL values for required parameters.");
				}
			}
			else {
				validReq = false;
			}
		}
		else {
			validReq = false;
		}
		
		if (!validReq)
			throw new RequestValidationException("ERROR: Chatter Flag data request "
					+ "contained invalid or NULL values for required parameters.");
	}
	
	/**
	 * Validate a request to retrieve a batch of Chatter Flag
	 * objects using a batch of flag id values.
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	@SuppressWarnings("unchecked")
	public static void validateBatchRetrieveFlagRequest(FlagCRUDRequest req)
			throws RequestValidationException {
		
		boolean validReq = true;
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					Set<String> flagIds = (Set<String>) req.getArgs().get("flagIds");
					
					if (flagIds == null || flagIds.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Chatter Flag data request "
							+ "contained invalid or NULL values for required parameters.");
				}
			}
			else {
				validReq = false;
			}
		}
		else {
			validReq = false;
		}
		
		if (!validReq)
			throw new RequestValidationException("ERROR: Chatter Flag data request "
					+ "contained invalid or NULL values for required parameters.");
	}
}

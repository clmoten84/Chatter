package com.chatter.flagservice.requests;

/**
 * RequestValidator
 * @author coreym
 *
 * Contains static methods for validating incoming
 * request data before processing request operation.
 * 
 * validateCreateFlagRequest
 * validateRetrieveFlagRequest
 * validateUpdateFlagRequest
 * validateDeleteRequest
 */
public class RequestValidator {
	
	/**
	 * Validate a request to create and save a new Flag object to
	 * the database.
	 * 
	 * @param req the Request to validate
	 * @return
	 */
	public static boolean validateCreateFlagReqeust(Request req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				if (req.getArgs().get("createdBy") == null)
					validReq = false;
				
				if (req.getArgs().get("forumId") == null)
					validReq = false;
				
				if (req.getArgs().get("commentId") == null)
					validReq = false;
				
				if (req.getArgs().get("flagDescription") == null)
					validReq = false;
			}
			else {
				validReq = false;
			}
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	/**
	 * Validate a request to retrieve a Flag object from the database
	 * using an argument flag id.
	 * 
	 * @param req the Request to validate
	 * @return
	 */
	public static boolean validateRetrieveFlagRequest(Request req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				if (req.getArgs().get("flagId") == null)
					validReq = false;
			}
			else {
				validReq = false;
			}
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	
}

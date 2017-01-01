package com.chatter.forumservice.requests;

/**
 * RequestValidator
 * @author coreym
 *
 * Contains static methods for validation incoming service
 * requests before processing.
 * 
 * validateCreateForumRequest
 * validateRetrieveForumRequest
 * validateUpdateForumRequest
 * validateDeleteForumRequest
 * validateAddCommentRequest
 * validateQueryByCreatorRequest
 * validateQueryByTitleRequest
 */
public class RequestValidator {
	
	/**
	 * Validate a request to create and save a Forum object to the
	 * database.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static boolean validateCreateForumRequest(Request req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				if (req.getArgs().get("createdBy") == null)
					validReq = false;
				
				if (req.getArgs().get("title") == null)
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
	 * Validate a request to retrieve a Forum object from the database.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static boolean validateRetrieveForumRequest(Request req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				if (req.getArgs().get("forumId") == null) 
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
	 * Validate a request to query the Forum table using the 
	 * created_by attribute.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static boolean validateQueryByCreatorRequest(Request req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				if (req.getArgs().get("createdBy") == null)
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
	 * Validate a request to query the Forum table by title attribute.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static boolean validateQueryByTitleRequest(Request req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				if (req.getArgs().get("title") == null)
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
	 * Validate a request to add/remove a comment id to a Forum
	 * object.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static boolean validateCommentRequest(Request req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				if (req.getArgs().get("forumId") == null)
					validReq = false;
				
				if (req.getArgs().get("commentId") == null)
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

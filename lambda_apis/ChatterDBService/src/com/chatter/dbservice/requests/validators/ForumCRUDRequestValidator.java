package com.chatter.dbservice.requests.validators;

import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.requests.ForumCRUDRequest;

/**
 * ForumCRUDRequestValidator
 * @author coreym
 * 
 * Provides methods for validating incoming Chatter Forum
 * data requests.
 */
public class ForumCRUDRequestValidator {
	/**
	 * Validate a request to create and save a Forum object to the
	 * database.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static void validateCreateForumRequest(ForumCRUDRequest req) 
			throws RequestValidationException {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String createdBy = (String) req.getArgs().get("createdBy");
					String title = (String) req.getArgs().get("title");
					
					if (createdBy == null || createdBy.isEmpty())
						validReq = false;
					
					if (title == null || title.isEmpty())
						validReq = false;
					}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Chatter Forum data request "
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
			throw new RequestValidationException("ERROR: Chatter Forum data request "
			+ "contained invalid or NULL values for required parameters.");
	}
	
	/**
	 * Validate a request to retrieve a Forum object from the database.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static void validateRetrieveForumRequest(ForumCRUDRequest req) 
			throws RequestValidationException {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
				String forumId = (String) req.getArgs().get("forumId");
				
				if (forumId == null || forumId.isEmpty()) 
					validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Create Chatter Flag request "
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
			throw new RequestValidationException("ERROR: Create Chatter Flag request "
			+ "contained invalid or NULL values for required parameters.");
	}
	
	/**
	 * Validate a request to query the Forum table using the 
	 * created_by attribute.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static void validateQueryByCreatorRequest(ForumCRUDRequest req) 
			throws RequestValidationException {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String createdBy = (String) req.getArgs().get("createdBy");
					
					if (createdBy == null || createdBy.isEmpty())
						validReq = false;
					}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Create Chatter Flag request "
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
			throw new RequestValidationException("ERROR: Create Chatter Flag request "
			+ "contained invalid or NULL values for required parameters.");
	}
	
	/**
	 * Validate a request to query the Forum table by title attribute.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static void validateQueryByTitleRequest(ForumCRUDRequest req) 
			throws RequestValidationException {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String title = (String) req.getArgs().get("title");
					
					if (title == null || title.isEmpty())
						validReq = false;
					}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Create Chatter Flag request "
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
			throw new RequestValidationException("ERROR: Create Chatter Flag request "
			+ "contained invalid or NULL values for required parameters.");
	}
	
	/**
	 * Validate a request to add/remove a comment id to a Forum
	 * object.
	 * 
	 * @param req the request to validate
	 * @return
	 */
	public static void validateCommentRequest(ForumCRUDRequest req) 
			throws RequestValidationException {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String forumId = (String) req.getArgs().get("forumId");
					String commentId = (String) req.getArgs().get("commentId");
					
					if (forumId == null || forumId.isEmpty())
						validReq = false;
					
					if (commentId == null || commentId.isEmpty())
						validReq = false;
					}
				catch (ClassCastException cce) {
					throw new RequestValidationException("ERROR: Create Chatter Flag request "
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
			throw new RequestValidationException("ERROR: Create Chatter Flag request "
			+ "contained invalid or NULL values for required parameters.");
	}
}

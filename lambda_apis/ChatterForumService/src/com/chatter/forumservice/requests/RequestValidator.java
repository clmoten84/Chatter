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
	 * Validate a request to create and save a Forum instance to the
	 * database
	 * @param req the request object to validate
	 * @return
	 */
	public static boolean validateCreateForumRequest(CreateForumRequest req) {
		boolean validReq = true;
		
		if(req != null) {
			if(req.getCreatedBy() == null || req.getCreatedBy().isEmpty())
				validReq = false;
			if(req.getTitle() == null || req.getTitle().isEmpty())
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	/**
	 * Validate a request to retrieve a Forum instance from the database
	 * @param req the request object to validate
	 * @return
	 */
	public static boolean validateRetrieveForumRequest(RetrieveForumRequest req) {
		boolean validReq = true;
		
		if(req != null) {
			if(req.getForumId() == null || req.getForumId().isEmpty())
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	/**
	 * Validate a request to update a Forum instance that exists in the database
	 * @param req the request object to validate
	 * @return
	 */
	public static boolean validateUpdateForumRequest(UpdateForumRequest req) {
		boolean validReq = true;
		
		if(req != null) {
			if(req.getForumId() == null || req.getForumId().isEmpty())
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	/**
	 * Validate a request to delete a forum instance that exists in the database
	 * @param req the request object to validate
	 * @return
	 */
	public static boolean validateDeleteForumRequest(DeleteForumRequest req) {
		boolean validReq = true;
		
		if(req != null) {
			if(req.getForumId() == null || req.getForumId().isEmpty())
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	/**
	 * Validate a request to query Forum table using created_by attribute
	 * @param req the request object to validate
	 * @return
	 */
	public static boolean validateQueryByCreatorRequest(QueryByCreatorRequest req) {
		boolean validReq = true;
		
		if(req != null) {
			if(req.getCreatedBy() == null || req.getCreatedBy().isEmpty())
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	/**
	 * Validate a request to query Forum table using title attribute
	 * @param req the request object to validate
	 * @return
	 */
	public static boolean validateQueryByTitleRequest(QueryByTitleRequest req) {
		boolean validReq = true;
		
		if(req != null) {
			if(req.getTitle() == null || req.getTitle().isEmpty())
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
	
	/**
	 * Validate a request to add a comment id to Forum object
	 * @param req the request to validate
	 * @return 
	 */
	public static boolean validateAddCommentRequest(AddCommentRequest req) {
		boolean validReq = true;
		
		if (req != null) {
			if (req.getForumId() == null || req.getForumId().isEmpty())
				validReq = false;
			
			if (req.getCommentId() == null || req.getCommentId().isEmpty())
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		return validReq;
	}
}

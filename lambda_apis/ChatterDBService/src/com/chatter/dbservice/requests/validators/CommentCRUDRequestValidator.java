package com.chatter.dbservice.requests.validators;

import java.util.List;

import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.requests.CommentCRUDRequest;

/**
 * CommentCRUDRequestValidator
 * @author coreym
 *
 * Provides methods for validating incoming request
 * related to CRUDing ChatterComment data.
 */
public class CommentCRUDRequestValidator {

	/**
	 * Validates an incoming request to create and save
	 * a new Chatter comment object to the database.
	 * 
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	public static void validateCreateRequest(CommentCRUDRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String createdBy = (String) req.getArgs().get("createdBy");
					String forumId = (String) req.getArgs().get("forumId");
					String bucketName = (String) req.getArgs().get("bucketName");
					String fileName = (String) req.getArgs().get("fileName");
					
					if (createdBy == null || createdBy.isEmpty())
						validReq = false;
					if (forumId == null || forumId.isEmpty())
						validReq = false;
					if (bucketName == null || bucketName.isEmpty())
						validReq = false;
					if (fileName == null || fileName.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter Comment request ERROR: "
							+ "request data failed to cast to proper type.");
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
			throw new RequestValidationException("Chatter Comment request ERROR: "
					+ "incoming request contained NULL or invalid values for required "
					+ "parameters.");
	}
	
	/**
	 * Validates a request to retrieve a Chatter comment object
	 * from the database using argument comment id.
	 * 
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	public static void validateRetrieveRequest(CommentCRUDRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String commentId = (String) req.getArgs().get("commentId");
					
					if (commentId == null || commentId.isEmpty()) 
						validReq = false;
					
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter Comment request ERROR: "
						+ "request data failed to cast to proper type.");
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
			throw new RequestValidationException("Chatter Comment request ERROR: "
				+ "incoming request contained NULL or invalid values for "
				+ "required parameters.");
	}
	
	/**
	 * Validates a request to add a reply to an existing Chatter comment
	 * object in the database.
	 * 
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	public static void validateCommentReplyRequest(CommentCRUDRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String commentId = (String) req.getArgs().get("commentId");
					String replyId = (String) req.getArgs().get("replyId");
					
					if (commentId == null || commentId.isEmpty())
						validReq = false;
					if (replyId == null || replyId.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter Comment request ERROR: "
						+ "request data failed to cast to proper type.");
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
			throw new RequestValidationException("Chatter Comment request ERROR: "
				+ "incoming request contained NULL or invalid values for "
				+ "required parameters.");
	}
	
	/**
	 * Validate request to add a new flag id to an existing Chatter comment
	 * object.
	 * 
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	public static void validateCommentFlagRequest(CommentCRUDRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String commentId = (String) req.getArgs().get("commentId");
					String flagId = (String) req.getArgs().get("flagId");
					
					if (commentId == null || commentId.isEmpty())
						validReq = false;
					if (flagId == null || flagId.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter Comment request ERROR: "
						+ "request data failed to cast to proper type.");
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
			throw new RequestValidationException("Chatter Comment request ERROR: "
				+ "incoming request contained NULL or invalid values for "
				+ "required parameters.");
	}
	
	/**
	 * Validate a request to retrieve a batch of Chatter comments from the 
	 * database.
	 * 
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	@SuppressWarnings("unchecked")
	public static void validateBatchRetrieveRequest(CommentCRUDRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					List<String> commentIds = (List<String>) req.getArgs().get("commentIds");
					
					if (commentIds == null || commentIds.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter Comment request ERROR: "
						+ "request data failed to cast to proper type.");
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
			throw new RequestValidationException("Chatter Comment request ERROR: "
				+ "incoming request contained NULL or invalid values for "
				+ "required parameters.");
	}
	
	/**
	 * Validate a request to query the Chatter comment table using
	 * an argument createdBy value.
	 * 
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	public static void validateQueryByCreatorRequest(CommentCRUDRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String createdBy = (String) req.getArgs().get("createdBy");
					
					if (createdBy == null || createdBy.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter Comment request ERROR: "
							+ "request data failed to cast to proper type.");
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
			throw new RequestValidationException("Chatter Comment request ERROR: "
				+ "incoming request contained NULL or invalid values for "
				+ "required parameters.");
	}
	
	/**
	 * Validate request to query the Chatter Comment table using argument
	 * forum id value.
	 * 
	 * @param req the request to validate
	 * @throws RequestValidationException
	 */
	public static void validateQueryByForumRequest(CommentCRUDRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String forumId = (String) req.getArgs().get("forumId");
					
					if (forumId == null || forumId.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter Comment request ERROR: "
							+ "request data failed to cast to proper type.");
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
			throw new RequestValidationException("Chatter Comment request ERROR: "
				+ "incoming request contained NULL or invalid values for "
				+ "required parameters.");
	}
}

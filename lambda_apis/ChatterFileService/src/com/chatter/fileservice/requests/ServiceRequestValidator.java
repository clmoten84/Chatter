package com.chatter.fileservice.requests;

import com.chatter.fileservice.exceptions.RequestValidationException;

/**
 * ServiceRequestValidator
 * @author coreym
 *
 * Contains methods for validating incoming request data for 
 * service operations.
 */
public class ServiceRequestValidator {
	
	/**
	 * Validate a request to create and save a new file to S3.
	 * Requests to create and save a new file must contain some
	 * required metadata:
	 * - createdBy
	 * - bucketName
	 * - keyName
	 * - dateCreated (this is the date the comment was created)
	 * 
	 * @param req
	 * @throws RequestValidationException
	 */
	public static void validateCreateCommentRequest(ServiceRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			// Check map of request arguments
			if (req.getArgs() != null) {
				try {
					String createdBy = (String) req.getArgs().get("createdBy");
					String bucketName = (String) req.getArgs().get("bucketName");
					String keyName = (String) req.getArgs().get("keyName");
					String dateCreated = (String) req.getArgs().get("dateCreated");
					
					if (createdBy == null || createdBy.isEmpty())
						validReq = false;
					if (bucketName == null || bucketName.isEmpty())
						validReq = false;
					if (keyName == null || keyName.isEmpty())
						validReq = false;
					if (dateCreated == null || dateCreated.isEmpty())
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter File Service request "
							+ "ERROR: request data failed to cast to proper type.");
				}
			}
			else {
				validReq = false;
			}
			
			// Check for file byte array
			if (req.getFileData() == null || req.getFileData().length == 0)
				validReq = false;
		}
		else {
			validReq = false;
		}
		
		if (!validReq) {
			throw new RequestValidationException("Chatter File Service request ERROR: "
					+ "incoming request data contained NULL or invalid values for required "
					+ "parameters.");
		}
	}
	
	/**
	 * Validates a request to delete an existing file from S3.
	 * Requests to delete an existing file must contain some required
	 * metadata:
	 * - bucketName
	 * - keyName
	 * @param req
	 * @throws RequestValidationException
	 */
	public static void validateDeleteRequest(ServiceRequest req) throws
		RequestValidationException {
		
		boolean validReq = true;
		
		if (req != null) {
			if (req.getArgs() != null) {
				try {
					String bucketName = (String) req.getArgs().get("bucketName");
					String keyName = (String) req.getArgs().get("keyName");
					
					if (bucketName == null || bucketName.isEmpty())
						validReq = false;
					if (keyName == null || keyName.isEmpty()) 
						validReq = false;
				}
				catch (ClassCastException cce) {
					throw new RequestValidationException("Chatter File Service request "
							+ "ERROR: request data failed to cast to proper type.");
				}
			}
			else {
				validReq = false;
			}
		}
		else {
			validReq = false;
		}
		
		if (!validReq) {
			throw new RequestValidationException("Chatter File Service request ERROR: "
					+ "incoming request data contained NULL or invalid values for required "
					+ "parameters.");
		}
	}
}

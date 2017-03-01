package com.chatter.fileservice.handlers;

import java.io.ByteArrayInputStream;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.chatter.fileservice.exceptions.MissingOperationException;
import com.chatter.fileservice.exceptions.PropertyRetrievalException;
import com.chatter.fileservice.exceptions.RequestValidationException;
import com.chatter.fileservice.requests.ServiceRequest;
import com.chatter.fileservice.requests.ServiceRequestValidator;
import com.chatter.fileservice.responses.FileMetadata;
import com.chatter.fileservice.responses.ServiceProps;
import com.chatter.fileservice.responses.ServiceResponse;
import com.chatter.fileservice.util.PropertiesResolver;
import com.chatter.fileservice.util.ServiceMessages;
import com.chatter.fileservice.util.ServiceOps;

public class ChatterFileAPIRequestHandler implements 
	RequestHandler<ServiceRequest, ServiceResponse<? extends Object>> {
	
    @Override
    public ServiceResponse<? extends Object> handleRequest(ServiceRequest input, 
    		Context context) {

    	// Retrieve service logger
    	LambdaLogger logger = context.getLogger();
    	
    	if (input != null) {
    		// Define service response to be called on error
    		ServiceResponse<Void> response;
    		
    		try {
    			// Retrieve operation in request
    			ServiceOps operation = input.getOperation();
    			if (operation != null) {
    				switch (operation) {
    				case SAVE_FILE:
    					return this.saveFile(input, context);
    				case DELETE_FILE:
    					return this.deleteFile(input, context);
    				case SERVICE_INFO:
    					return this.getServiceInfo(input, context);
    				case PING:
    					return this.pingService();
    				default:
    					throw new UnsupportedOperationException("ERROR Chatter File "
    							+ "Service: request contained unsupported operation - "
    							+ operation.toString());
    				}
    			}
    			else {
    				throw new MissingOperationException("ERROR Chatter File Service: "
    						+ "request is missing required operation value!");
    			}
    		}
    		catch (MissingOperationException moe) {
    			response = new ServiceResponse<>();
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setMessage(ServiceMessages.MISSING_OPERATION.toString());
    			response.setExceptionThrown(true);
    			
    			String exMsg = this.compileExceptionMessage(moe);
    			response.setExceptionMessage(exMsg);
    			logger.log(exMsg);
    			return response;
    		}
    		catch (UnsupportedOperationException uoe) {
    			response = new ServiceResponse<>();
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setMessage(ServiceMessages.UNSUPPORTED_OPERATION.toString());
    			response.setExceptionThrown(true);
    			
    			String exMsg = this.compileExceptionMessage(uoe);
    			response.setExceptionMessage(exMsg);
    			logger.log(exMsg);
    			return response;
    		}
    		catch (PropertyRetrievalException pre) {
    			response = new ServiceResponse<>();
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setMessage(ServiceMessages.PROPERTY_RETRIEVAL_FAILURE.toString());
    			response.setExceptionThrown(true);
    			
    			String exMsg = this.compileExceptionMessage(pre);
    			response.setExceptionMessage(exMsg);
    			logger.log(exMsg);
    			return response;
    		}
    		catch (RequestValidationException rve) {
    			response = new ServiceResponse<>();
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setMessage(ServiceMessages.INVALID_REQUEST.toString());
    			response.setExceptionThrown(true);
    			
    			String exMsg = this.compileExceptionMessage(rve);
    			response.setExceptionMessage(exMsg);
    			logger.log(exMsg);
    			return response;
    		}
    		catch (AmazonClientException ace) {
    			response = new ServiceResponse<>();
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setMessage(ServiceMessages.OPERATION_FAILURE.toString());
    			response.setExceptionThrown(true);
    			
    			String exMsg = this.compileExceptionMessage(ace);
    			response.setExceptionMessage(exMsg);
    			logger.log(exMsg);
    			return response;
    		}
    	}
    	
    	// If request input is NULL return NULL response
        return null;
    }

    /**
     * Save a file object to file storage
     * @param req
     * @param ctx
     * @return
     * @throws RequestValidationException
     */
    private ServiceResponse<FileMetadata> saveFile(ServiceRequest req, Context ctx) 
    	throws RequestValidationException, PropertyRetrievalException {
    	
    	// Ensure request passes service validation
    	ServiceRequestValidator.validateCreateCommentRequest(req);
    	
    	// Log request data
    	ctx.getLogger().log(req.toString());
    	
    	// Create S3 client object
    	AmazonS3Client s3Client = this.getStorageClient();
    	ServiceResponse<FileMetadata> response = new ServiceResponse<>();
    	
    	// Generate metadata object from arguments in request
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.addUserMetadata("createdBy", (String) req.getArgs().get("createdBy"));
    	metadata.addUserMetadata("bucketName", (String) req.getArgs().get("bucketName"));
    	metadata.addUserMetadata("keyName", (String) req.getArgs().get("keyName"));
    	metadata.addUserMetadata("dateCreated", (String) req.getArgs().get("dateCreated"));

    	if (req.getArgs().containsKey("commentId")) {
    		metadata.addUserMetadata("commentId", (String) req.getArgs().get("commentId"));
    	}
    	
    	if (req.getArgs().containsKey("forumId")) {
    		metadata.addUserMetadata("forumId", (String) req.getArgs().get("forumId"));
    	}
    	
    	// Create put object request and execute operation
    	PutObjectRequest putReq = new PutObjectRequest(
			(String) req.getArgs().get("bucketName"),
			(String) req.getArgs().get("keyName"),
			new ByteArrayInputStream(req.getFileData()),
			metadata
    	);
    	
    	PutObjectResult result = s3Client.putObject(putReq);
    	
    	if (result != null) {
    		// Create FileMetadata response object
    		FileMetadata resultMeta = new FileMetadata();
    		
    		if (result.getMetadata() != null) {
    			resultMeta.seteTag(result.getETag());
    			resultMeta.setContentLength(result.getMetadata().getContentLength());
    			resultMeta.setContentType(result.getMetadata().getContentType());
    			resultMeta.setUserMetadata(result.getMetadata().getUserMetadata());
    		}
    		
    		response.setPayload(resultMeta);
    		response.setStatus(true);
    		response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    		response.setExceptionThrown(false);
    		response.setExceptionMessage(null);
    	}
    	else {
    		response.setPayload(null);
    		response.setStatus(false);
    		response.setMessage(ServiceMessages.OPERATION_FAILURE.toString());
    		response.setExceptionThrown(false);
    		response.setExceptionMessage(null);
    	}
    	
    	return response;
    }
    
    /**
     * Delete a file object from file storage
     * @param req
     * @param ctx
     * @return
     * @throws RequestValidationException
     */
    private ServiceResponse<Void> deleteFile(ServiceRequest req, Context ctx) 
    	throws RequestValidationException, PropertyRetrievalException { 
    	
    	// Validate request
    	ServiceRequestValidator.validateDeleteRequest(req);
    	
    	// Log request data
    	ctx.getLogger().log(req.toString());
    	
    	// Create S3 client object
    	AmazonS3Client s3Client = this.getStorageClient();
    	ServiceResponse<Void> response = new ServiceResponse<>();
    	
    	// Generate delete request and execute operation
    	DeleteObjectRequest delReq = new DeleteObjectRequest(
    		(String) req.getArgs().get("bucketName"),
    		(String) req.getArgs().get("keyName")
    	);
    	s3Client.deleteObject(delReq);
    	
    	response.setPayload(null);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Retrieves service information and returns in response
     * @param req
     * @param ctx
     * @return
     * @throws PropertyRetrievalException
     */
    private ServiceResponse<ServiceProps> getServiceInfo(ServiceRequest req,
    		Context ctx) throws PropertyRetrievalException {
    	ServiceResponse<ServiceProps> response = new ServiceResponse<>();
    	
    	// Log request
    	ctx.getLogger().log(req.toString());
    	
    	// Retrieve service properties
    	PropertiesResolver propsResolver = new PropertiesResolver("service.properties");
    	String env = propsResolver.getProperty("service.env");
    	String name = propsResolver.getProperty("service.name");
    	String description = propsResolver.getProperty("service.description");
    	String version = propsResolver.getProperty("service.version");
    	
    	response.setPayload(new ServiceProps(env, name, description, version));
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Pings service with empty request
     * @return
     */
    private ServiceResponse<String> pingService() {
    	ServiceResponse<String> response = new ServiceResponse<>();
    	response.setPayload("Ping received!");
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Create and return Amazon S3 client object
     * @return
     * @throws PropertyRetrievalException
     */
    private AmazonS3Client getStorageClient() throws PropertyRetrievalException {
    	// Need to retrieve environment variable from properties file
    	PropertiesResolver propsResolver = new PropertiesResolver("service.properties");
    	String env = propsResolver.getProperty("service.env");
    	AmazonS3Client s3Client;
    	
    	// Instantiate S3 client object
    	if (env.equalsIgnoreCase("local")) {
    		return new AmazonS3Client(new ProfileCredentialsProvider());
    	}
    	else {
    		return new AmazonS3Client(new EnvironmentVariableCredentialsProvider());
    	}
    }
    
    /**
     * Compiles an exception message to add to response when
     * an exception is encountered in the service handler. The
     * exception message details the exception in a log friendly
     * fashion.
     * 
     * @param ex the Exception that was caught
     * @return
     */
    private String compileExceptionMessage(Exception ex) {
    	StringBuilder builder = new StringBuilder();
    	builder.append("***** An Exception was caught in service handler *****");
    	builder.append("\nException message: ").append(ex.getMessage());
    	builder.append("\nException Details: ");
    	
    	for(StackTraceElement element : ex.getStackTrace()) {
    		builder.append("\n\t").append(element.toString());
    	}
    	return builder.toString();
    }
}

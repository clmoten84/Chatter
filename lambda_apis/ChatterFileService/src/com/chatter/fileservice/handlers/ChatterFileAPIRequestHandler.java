package com.chatter.fileservice.handlers;

import com.amazonaws.AmazonClientException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chatter.fileservice.exceptions.MissingOperationException;
import com.chatter.fileservice.exceptions.PropertyRetrievalException;
import com.chatter.fileservice.exceptions.RequestValidationException;
import com.chatter.fileservice.fao.FileAccessObject;
import com.chatter.fileservice.fao.FileAccessObjectImpl;
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
	
	private FileAccessObject fao;
	
    @Override
    public ServiceResponse<? extends Object> handleRequest(ServiceRequest input, 
    		Context context) {

    	// Retrieve service logger
    	LambdaLogger logger = context.getLogger();
    	
    	if (input != null) {
    		// Define service response to be called on error
    		ServiceResponse<Void> response;
    		
    		try {
    			// Initialize fao object
    			fao = new FileAccessObjectImpl();
    			
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
    	throws RequestValidationException, PropertyRetrievalException,
    		AmazonClientException {
    	
    	// Ensure request passes service validation
    	ServiceRequestValidator.validateCreateCommentRequest(req);
    	
    	// Log request data
    	ctx.getLogger().log(req.toString());
    	
    	// Execute save operation
    	FileMetadata fileMetadata = this.fao.saveFile(req);
    	
    	// Generate response and return
    	ServiceResponse<FileMetadata> response = new ServiceResponse<>();
    	
    	if (fileMetadata != null) {
    		response.setPayload(fileMetadata);
    		response.setStatus(true);
    		response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    		response.setExceptionThrown(false);
    		response.setExceptionMessage(null);
    	}
    	else {
    		response.setPayload(null);
    		response.setStatus(true);
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
    	throws RequestValidationException, PropertyRetrievalException,
    		AmazonClientException { 
    	
    	// Validate request
    	ServiceRequestValidator.validateDeleteRequest(req);
    	
    	// Log request data
    	ctx.getLogger().log(req.toString());
    	
    	// Execute delete operation
    	ServiceResponse<Void> response = new ServiceResponse<>();
    	this.fao.deleteFile(req);
    	
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

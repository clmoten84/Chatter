package com.chatter.dbservice.handlers;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chatter.dbservice.dao.FlagDAO;
import com.chatter.dbservice.dao.impl.FlagDAOImpl;
import com.chatter.dbservice.exceptions.MissingOperationException;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.exceptions.UnsupportedOperationException;
import com.chatter.dbservice.requests.FlagCRUDRequest;
import com.chatter.dbservice.responses.ServiceResponse;
import com.chatter.dbservice.util.ServiceMessages;
import com.chatter.dbservice.util.ops.ChatterFlagOps;
import com.chatter.dbservice.model.ChatterFlag;
import com.chatter.dbservice.responses.ServicePropsResponse;

public class ChatterFlagAPIRequestHandler implements 
		RequestHandler<FlagCRUDRequest, ServiceResponse<? extends Object>> {
	
	// Data access object
	FlagDAO dao;

	/**
	 * Handles incoming requests to Lambda service
	 */
    @Override
    public ServiceResponse<? extends Object> handleRequest(FlagCRUDRequest input, 
    		Context context) {
    	
    	// Retrieve logger
    	LambdaLogger logger = context.getLogger();
    	
    	if (input != null) {
    		// Define response to be returned on error conditions
    		ServiceResponse<Void> response = new ServiceResponse<>();
    		
    		try {
    			// Initialize DAO
    			dao = new FlagDAOImpl();
    			
    			ChatterFlagOps op = input.getOperation();
    			if (op != null) {
    				
    				switch (op) {
    				case CREATE:
    					return this.createFlag(input, context);
    				case RETRIEVE:
    					return this.retrieveFlag(input, context);
    				case UPDATE:
    					return this.updateFlag(input, context);
    				case DELETE:
    					return this.deleteFlag(input, context);
    				case BATCH_RETRIEVE:
    					return this.batchRetrieveFlag(input, context);
    				case BATCH_DELETE:
    					return this.batchDeleteFlag(input, context);
    				case PING:
    					return this.pingService();
    				case SERVICE_INFO:
    					return this.getServiceInfo(input, context);
    				default:
    					throw new UnsupportedOperationException("ERROR: Chatter Flag data "
    							+ "request contains unsupported operation: " + op.toString());
    				}
    			}
    			else {
    				throw new MissingOperationException("ERROR: Chatter Flag data request "
    						+ "is missing a required operation attribute!");
    			}
    		}
    		catch (MissingOperationException moe) {
    			// Set error response and return
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setExceptionThrown(true);
    			response.setMessage(ServiceMessages.MISSING_OPERATION.toString());
    			
    			String exceptionMessage = this.compileExceptionMessage(moe);
    			response.setExceptionMessage(exceptionMessage);
    			logger.log(exceptionMessage);
    			return response;
    		}
    		catch (UnsupportedOperationException uoe) {
    			// Set error response and return
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setExceptionThrown(true);
    			response.setMessage(ServiceMessages.UNSUPPORTED_OPERATION.toString());
    			
    			String exceptionMessage = this.compileExceptionMessage(uoe);
    			response.setExceptionMessage(exceptionMessage);
    			logger.log(exceptionMessage);
    			return response;
    		}
    		catch (RequestValidationException rve) {
    			// Set error response and return
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setExceptionThrown(true);
    			response.setMessage(ServiceMessages.INVALID_REQUEST.toString());
    			
    			String exceptionMessage = this.compileExceptionMessage(rve);
    			response.setExceptionMessage(exceptionMessage);
    			logger.log(exceptionMessage);
    			return response;
    		}
    		catch (PropertyRetrievalException pre) {
    			// Set error response and return
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setExceptionThrown(true);
    			response.setMessage(ServiceMessages.PROPERTY_RETRIEVAL_FAILURE.toString());
    			
    			String exceptionMessage = this.compileExceptionMessage(pre);
    			response.setExceptionMessage(exceptionMessage);
    			logger.log(exceptionMessage);
    			return response;
    		}
    		catch (AmazonClientException ace) {
    			// Set error response and return
    			response.setPayload(null);
    			response.setStatus(false);
    			response.setExceptionThrown(true);
    			response.setMessage(ServiceMessages.OPERATION_FAILURE.toString());
    			
    			String exceptionMessage = this.compileExceptionMessage(ace);
    			response.setExceptionMessage(exceptionMessage);
    			logger.log(exceptionMessage);
    			return response;
    		}
    	}

    	// Returns null response if incoming request is null
        return null;
    }
    
    /**
     * Create and save a new ChatterFlag object to the database.
     * @param input the request to process
     * @param context the request context
     * @return ServiceResponse containing the created ChatterFlag object
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterFlag> createFlag(FlagCRUDRequest request, 
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterFlag> response = new ServiceResponse<>();
    	
    	// Log request data
    	context.getLogger().log(request.toString());
    	
    	ChatterFlag flag = dao.createFlag(request);
    	response.setPayload(flag);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }

    /**
     * Retrieve a ChatterFlag object from the database using
     * flag id.
     * 
     * @param input the request to process
     * @param context the request context
     * @return ServiceResponse
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterFlag> retrieveFlag(FlagCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterFlag> response = new ServiceResponse<>();
    	
    	// Log request data
    	context.getLogger().log(request.toString());
    	
    	ChatterFlag flag = dao.retrieveFlag(request);
    	response.setPayload(flag);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Update an existing ChatterFlag object with new data.
     * @param input the request to process
     * @param context the request context
     * @return ServiceResponse
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterFlag> updateFlag(FlagCRUDRequest request, 
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterFlag> response = new ServiceResponse<>();
    	
    	// Log request data
    	context.getLogger().log(request.toString());
    	
    	ChatterFlag flag = dao.updateFlag(request);
    	response.setPayload(flag);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Delete a ChatterFlag object from the database
     * @param input the request to process
     * @param context the request context
     * @return ServiceResponse
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<Void> deleteFlag(FlagCRUDRequest request, 
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<Void> response = new ServiceResponse<>();
    	
    	// Log request data
    	context.getLogger().log(request.toString());
    	
    	boolean opSuccess = dao.deleteFlag(request);
    	response.setPayload(null);
    	response.setStatus(opSuccess);
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	
    	if (opSuccess)
    		response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	else
    		response.setMessage(ServiceMessages.OPERATION_FAILURE.toString());
    	return response;
    }
    
    /**
     * Retrieve a batch of ChatterFlag objects from the database
     * using a list of flag id values.
     * @param request the request to process
     * @param context the request context
     * @return ServiceResponse
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<List<ChatterFlag>> batchRetrieveFlag(FlagCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<List<ChatterFlag>> response = new ServiceResponse<>();
    	
    	// Log request data
    	context.getLogger().log(request.toString());
    	
    	List<ChatterFlag> flags = dao.batchRetrieveFlag(request);
    	response.setPayload(flags);
    	response.setStatus(true);
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	return response;
    }
    
    /**
     * Delete a batch of ChatterFlag objects from the database
     * @param request the request to process
     * @param context the request context
     * @return ServiceResponse
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<List<String>> batchDeleteFlag(FlagCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<List<String>> response = new ServiceResponse<>();
    	
    	// Log request data
    	context.getLogger().log(request.toString());
    	
    	List<String> deletedFlags = dao.batchDeleteFlag(request);
    	response.setPayload(deletedFlags);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Ping the service to ensure availability. Response includes
     * service property information.
     * @param input the request to process
     * @param context the request context
     * @return FlagServiceResponse
     * @throws PropertyRetrievalException
     */
    public ServiceResponse<ServicePropsResponse> getServiceInfo(FlagCRUDRequest request, 
    		Context context) throws PropertyRetrievalException {
    	
    	ServiceResponse<ServicePropsResponse> response = new ServiceResponse<>();
    	
    	// Log request info to logger
    	context.getLogger().log(request.toString());
    	
    	response.setPayload(dao.getServiceProperties());
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Pings service with empty request.
     * @return
     */
    public ServiceResponse<String> pingService() {
    	ServiceResponse<String> response = new ServiceResponse<>();
    	response.setPayload("Ping received!");
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Compiles an exception message to add to the response
     * when an exception is encountered in the service handler.
     * The exception message details the exception in a log
     * friendly fashion.
     * 
     * @param ex the exception that was caught
     * @return a log friendly exception message
     */
    private String compileExceptionMessage(Exception ex) {
    	StringBuilder builder = new StringBuilder();
    	builder.append("***** An exception was caught in service handler *****");
    	builder.append("\nException message: ").append(ex.getMessage());
    	builder.append("\nException details: ");
    	
    	for (StackTraceElement elem : ex.getStackTrace()) {
    		builder.append("\n\t").append(elem.toString());
    	}
    	
    	return builder.toString();
    }
}

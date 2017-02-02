package com.chatter.dbservice.handlers;

import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chatter.dbservice.dao.CommentDAO;
import com.chatter.dbservice.dao.impl.CommentDAOImpl;
import com.chatter.dbservice.exceptions.MissingOperationException;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.exceptions.UnsupportedOperationException;
import com.chatter.dbservice.model.ChatterComment;
import com.chatter.dbservice.requests.CommentCRUDRequest;
import com.chatter.dbservice.responses.CommentResultPage;
import com.chatter.dbservice.responses.ServicePropsResponse;
import com.chatter.dbservice.responses.ServiceResponse;
import com.chatter.dbservice.util.ServiceMessages;
import com.chatter.dbservice.util.ops.ChatterCommentOps;

public class ChatterCommentDBRequestHandler implements 
	RequestHandler<CommentCRUDRequest, ServiceResponse<? extends Object>> {

	// Data access object
	CommentDAO dao;
	
	/**
	 * Handles incoming Chatter Comment data requests
	 */
    @Override
    public ServiceResponse<? extends Object> handleRequest(CommentCRUDRequest input, 
    		Context context) {
        
    	// Retrieve service logger
    	LambdaLogger logger = context.getLogger();

    	if (input != null) {
    		// Define response to be returned on error conditions
    		ServiceResponse<Void> response;
    		
    		try {
    			// Initialize DAO
    			dao = new CommentDAOImpl();
    			
    			// Retrieve request operation from incoming request
    			ChatterCommentOps op = input.getOperation();
    			if (op != null) {
    				switch(op) {
    					case CREATE:
    						return this.createComment(input, context);
    					case RETRIEVE:
    						return this.retrieveComment(input, context);
						case ADD_FLAG:
							return this.updateComment(input, context);
						case ADD_REPLY:
							return this.updateComment(input, context);
						case BATCH_DELETE:
							return this.batchDelete(input, context);
						case BATCH_RETRIEVE:
							return this.batchRetrieve(input, context);
						case DELETE:
							return this.deleteComment(input, context);
						case INCREMENT_CONCUR:
							return this.updateComment(input, context);
						case PING:
							return this.pingService(input, context);
						case QUERY_BY_CREATOR:
							return this.queryByCreator(input, context);
						case QUERY_BY_FORUM:
							return this.queryByForum(input, context);
						case REMOVE_FLAG:
							return this.updateComment(input, context);
						case REMOVE_REPLY:
							return this.updateComment(input, context);
						default:
							throw new UnsupportedOperationException("ERROR: Chatter Comment "
									+ "data request contained an unsupported operation: "
									+ op.toString());
    				}
    			}
    			else {
    				throw new MissingOperationException("ERROR: Chatter Comment data"
    						+ " request is missing a required operation value!");
    			}
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
    	
    	// If the request input is NULL return NULL response
        return null;
    }
    
    /**
     * Create and save a new ChatterComment object to the database
     * @param request the request to process
     * @param context request context
     * @return
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterComment> createComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterComment> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ChatterComment comment = this.dao.createComment(request);
    	response.setPayload(comment);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Retrieve a ChatterComment object from database using
     * argument comment id value.
     * 
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterComment> retrieveComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterComment> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(this.dao.retrieveComment(request));
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Updates an existing ChatterComment object in database. The operation
     * value contained in the request determines what data is updated in the
     * ChatterComment object.
     * 
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterComment> updateComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterComment> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(this.dao.updateComment(request));
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Delete an existing ChatterComment object from database
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<Void> deleteComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<Void> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	if (this.dao.deleteComment(request)) {
    		response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	}
    	else {
    		response.setMessage(ServiceMessages.OPERATION_FAILURE.toString());
    	}
    	
    	response.setPayload(null);
    	response.setStatus(true);
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Query the ChatterComment table using created_by value
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws RequestValidationException
     * @throws PropertyRetrievalException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<CommentResultPage> queryByCreator(CommentCRUDRequest request, 
    		Context context) throws RequestValidationException, PropertyRetrievalException,
    			AmazonServiceException, AmazonClientException {
    	
    	ServiceResponse<CommentResultPage> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(this.dao.queryByCreator(request));
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Query the ChatterComment table using forum_id value
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws RequestValidationException
     * @throws PropertyRetrievalException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<CommentResultPage> queryByForum(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, PropertyRetrievalException,
    			AmazonServiceException, AmazonClientException {
    	
    	ServiceResponse<CommentResultPage> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(this.dao.queryByForum(request));
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Retrieve a batch of ChatterComment objects from database using
     * a list of argument comment id values
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<List<ChatterComment>> batchRetrieve(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<List<ChatterComment>> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(this.dao.batchRetrieve(request));
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Delete a batch of ChatterComment objects from database
     * using a list of comment id values
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<List<String>> batchDelete(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<List<String>> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(this.dao.batchDelete(request));
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Pings service and retrieves service metadata
     * @param request the request to process
     * @param context the request context
     * @return
     * @throws PropertyRetrievalException
     */
    public ServiceResponse<ServicePropsResponse> pingService(CommentCRUDRequest request,
    		Context context) throws PropertyRetrievalException {
    	
    	ServiceResponse<ServicePropsResponse> response = new ServiceResponse<>();
    	
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(this.dao.getServiceProperties());
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

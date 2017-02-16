package com.chatter.dbservice.handlers;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chatter.dbservice.dao.ForumDAO;
import com.chatter.dbservice.dao.impl.ForumDAOImpl;
import com.chatter.dbservice.exceptions.MissingOperationException;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.exceptions.UnsupportedOperationException;
import com.chatter.dbservice.requests.ForumCRUDRequest;
import com.chatter.dbservice.responses.ServiceResponse;
import com.chatter.dbservice.util.ops.ChatterForumOps;
import com.chatter.dbservice.model.ChatterForum;
import com.chatter.dbservice.util.ServiceMessages;
import com.chatter.dbservice.responses.ForumResultPage;
import com.chatter.dbservice.responses.ServicePropsResponse;

public class ChatterForumAPIRequestHandler implements 
		RequestHandler<ForumCRUDRequest, ServiceResponse<? extends Object>> {

	// Data access object
	ForumDAO dao;
	
    @Override
    public ServiceResponse<? extends Object> handleRequest(ForumCRUDRequest input,
    		Context context) {
    	
    	// Retrieve service logger
    	LambdaLogger logger = context.getLogger();

    	if (input != null) {
    		
    		// Define response to be returned on error conditions
    		ServiceResponse<Void> response = new ServiceResponse<>();
    		
    		try {
    			// Initialize DAO
    			dao = new ForumDAOImpl();
    			
    			ChatterForumOps op = input.getOperation();
    			if (op != null) {
    				switch(op) {
					case ADD_COMMENT:
						return this.addCommentToForum(input, context);
					case CREATE:
						return this.createForum(input, context);
					case DELETE:
						return this.deleteForum(input, context);
					case QUERY_BY_CREATOR:
						return this.queryByCreator(input, context);
					case QUERY_BY_ID:
						return this.retrieveForumById(input, context);
					case QUERY_BY_TITLE:
						return this.queryByTitle(input, context);
					case REMOVE_COMMENT:
						return this.removeCommentFromForum(input, context);
					case UPDATE:
						return this.updateForum(input, context);
					case PING:
						return this.pingService(input, context);
					default:
						throw new UnsupportedOperationException("ERROR: Chatter Forum data "
								+ "request contains unsupported operation: " + op.toString());
    				}
    			}
    			else {
    				throw new MissingOperationException("ERROR: Chatter Forum data request "
    						+ "is missing a required operation attribute!");
    			}
    		}
    		catch (MissingOperationException moe) {
    			//Set error response and return
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.MISSING_OPERATION.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(moe);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
    		}
    		catch (UnsupportedOperationException uoe) {
    			// Set error response and return
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.UNSUPPORTED_OPERATION.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(uoe);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
    		}
    		catch (RequestValidationException rve) {
    			// Set error response and return
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.INVALID_REQUEST.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(rve);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
    		}
    		catch (PropertyRetrievalException pre) {
    			// Set error response and return
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.PROPERTY_RETRIEVAL_FAILURE.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(pre);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
    		}
    		catch (AmazonClientException ace) {
    			// Set error response and return
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.OPERATION_FAILURE.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(ace);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
    		}
    	}
        
    	// If incoming request is NULL the return NULL response
        return null;
    }
    
    /**
     * Creates and saves a Chatter forum object in the DB
     * @param input the request to process
     * @param context the service context
     * @return a ForumServiceResponse containing the created Chatter forum object
     * 
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterForum> createForum(ForumCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterForum> response = new ServiceResponse<>();
    	
    	//Log request info to context logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
		ChatterForum forum = dao.createForum(request);
		response.setPayload(forum);
		response.setStatus(true);
		response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
		response.setExceptionThrown(false);
		response.setExceptionMessage(null);
    	
    	return response;
    }
    
    /**
     * Retrieves a Chatter forum object from the DB using the object's id
     * @param input the request to process
     * @param context lambda context object
     * @return ForumServiceResponse
     * 
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterForum> retrieveForumById(ForumCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterForum> response = new ServiceResponse<>();
    	
    	//Log request info to context logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ChatterForum forum = dao.retrieveForumById(request);
    	response.setPayload(forum);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }

    /**
     * Updates a ChatterForum object
     * @param input the request to process
     * @param context lambda context object
     * @return ForumServiceResponse
     * 
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterForum> updateForum(ForumCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterForum> response = new ServiceResponse<>();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ChatterForum forum = dao.updateForum(request);
    	response.setPayload(forum);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Updates a ChatterForum object by adding a new comment id to the 
     * set of comment ids associated with forum.
     * 
     * @param input the request to process
     * @param context lambda context object
     * @return ForumServiceResponse
     * 
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterForum> addCommentToForum(ForumCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
		ServiceResponse<ChatterForum> response = new ServiceResponse<>();
		
		// Log request info to lambda logger
		LambdaLogger logger = context.getLogger();
		logger.log(request.toString());
		
		ChatterForum forum = dao.addCommentToForum(request);
		response.setPayload(forum);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
	}
    
    /**
     * Updates a ChatterForum object by removing a comment id from the 
     * set of comment ids associated with forum.
     * 
     * @param input the request to process
     * @param context lambda context object
     * @return ForumServiceResponse
     * 
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<ChatterForum> removeCommentFromForum(ForumCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<ChatterForum> response = new ServiceResponse<>();
		
		// Log request info to lambda logger
		LambdaLogger logger = context.getLogger();
		logger.log(request.toString());
		
		ChatterForum forum = dao.removeCommentFromForum(request);
		response.setPayload(forum);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Queries the ChatterForum DB table using the global secondary 
     * index (createdBy). Results are returned in pages of 20. The
     * result page also contains the last key evaluated so that subsequent
     * results can be retrieved in another request.
     * 
     * @param input the request to process
     * @param context lambda context object
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     * @throws PropertyRetrievalException 
     */
    public ServiceResponse<ForumResultPage> queryByCreator(ForumCRUDRequest request,
    	Context context) throws RequestValidationException, AmazonServiceException,
    		AmazonClientException, PropertyRetrievalException {
    	
    	ServiceResponse<ForumResultPage> response = new ServiceResponse<>();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ForumResultPage forumResultPage = dao.queryByCreator(request);
    	response.setPayload(forumResultPage);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
	}
    
    /**
     * Queries the ChatterForum DB table using the global secondary
     * index (title). Results are returned in pages of 20. The result
     * page also contains the last key evaluated so that subsequent
     * results can be retrieved in another request.
     * 
     * @param input the request to process
     * @param context lambda context object
     * @return ForumServiceResponse
     * 
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     * @throws PropertyRetrievalException 
     */
    public ServiceResponse<ForumResultPage> queryByTitle(ForumCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException, PropertyRetrievalException {
    	
    	ServiceResponse<ForumResultPage> response = new ServiceResponse<>();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ForumResultPage forumResultPage = dao.queryByTitle(request);
    	response.setPayload(forumResultPage);
    	response.setStatus(true);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	return response;
    }
    
    /**
     * Deletes the requested ChatterForum instance from the DB. 
     * 
     * @param input the request to process
     * @param context lambda context object
     * @return ForumServiceResponse
     * 
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public ServiceResponse<Void> deleteForum(ForumCRUDRequest request, 
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	
    	ServiceResponse<Void> response = new ServiceResponse<>();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	boolean opSuccess = dao.deleteForum(request);
    	response.setPayload(null);
    	response.setStatus(true);
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	
    	if(opSuccess) 
    		response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	else
    		response.setMessage(ServiceMessages.OPERATION_FAILURE.toString());
    	return response;
    }
    
    /**
     * Pings the service to ensure availability. Response includes
     * service information.
     * @param input the request to process
     * @param context the request context
     * @return a ForumServiceResponse
     * @throws PropertyRetrievalException 
     */
    public ServiceResponse<ServicePropsResponse> pingService(ForumCRUDRequest request, 
    		Context context) throws PropertyRetrievalException {
    	
    	ServiceResponse<ServicePropsResponse> response = new ServiceResponse<>();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(dao.getServiceProperties());
    	response.setStatus(true);
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	return response;
    }
    
    /**
     * Compiles an exception message to add to response
 	 * when an exception is encountered in the service
 	 * handler. The exception message details the exception
 	 * in a log friendly fashion.
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

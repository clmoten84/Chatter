package com.chatter.forumservice.handler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chatter.forumservice.dao.ForumDAO;
import com.chatter.forumservice.dao.ForumDAOImpl;
import com.chatter.forumservice.exceptions.MissingOperationException;
import com.chatter.forumservice.exceptions.RequestValidationException;
import com.chatter.forumservice.model.ChatterForum;
import com.chatter.forumservice.requests.ForumServiceRequest;
import com.chatter.forumservice.requests.Request;
import com.chatter.forumservice.responses.ForumResultPage;
import com.chatter.forumservice.responses.ForumServiceResponse;
import com.chatter.forumservice.responses.ServicePropsResponse;
import com.chatter.forumservice.util.ServiceMessages;
import com.chatter.forumservice.util.ServiceOperations;

public class ChatterForumServiceRequestHandler implements RequestHandler<ForumServiceRequest<Request>, 
	ForumServiceResponse<? extends Object>> {
	
	private ForumDAO forumDao = new ForumDAOImpl();
	
    @Override
    public ForumServiceResponse<? extends Object> handleRequest(ForumServiceRequest<Request> input, Context context) {
    	LambdaLogger logger = context.getLogger();
    	
        if(input != null) {
        	Request reqData = input.getData();
        	
        	//Define the response to be returned upon error conditions
        	ForumServiceResponse<Void> response = new ForumServiceResponse<>();
        	
        	try {
        		ServiceOperations operation = reqData.getOperation();
        		if (operation != null) {
	        		switch(operation) {
	        			case CREATE:
	        				return this.createForum(input, context);
	        			case QUERY_BY_ID:
	        				return this.retrieveForumById(input, context);
	        			case QUERY_BY_CREATOR:
	        				return this.queryByCreator(input, context);
	        			case QUERY_BY_TITLE:
	        				return this.queryByTitle(input, context);
	        			case UPDATE:
	        				return this.updateForum(input, context);
	        			case ADD_COMMENT:
	        				return this.addCommentToForum(input, context);
	        			case REMOVE_COMMENT:
	        				return this.removeCommentFromForum(input, context);
	        			case DELETE:
	        				return this.deleteForum(input, context);
	        			case PING:
	        				return this.pingService(input, context);
	        			default:
	        				throw new UnsupportedOperationException("ERROR! The service "
	        						+ "does not support operation: " + operation.toString());
	        		}
        		}
        		else {
        			throw new MissingOperationException("ERROR! The incoming request "
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
        		response.setMessage(ServiceMessages.OPERATION_FAILED.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(rve);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
        	}
        	catch (AmazonServiceException ase) {
        		// Set error response and return
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.OPERATION_FAILED.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(ase);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
        	}
        	catch (AmazonClientException ace) {
        		// Set error response and return
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.OPERATION_FAILED.toString());
        		response.setExceptionThrown(true);
        		
        		String exceptionMessage = this.compileExceptionMessage(ace);
        		response.setExceptionMessage(exceptionMessage);
        		logger.log(exceptionMessage);
        		return response;
        	}
        }
        
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
    public ForumServiceResponse<ChatterForum> createForum(ForumServiceRequest<Request> input,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	ForumServiceResponse<ChatterForum> response = new ForumServiceResponse<>();
    	Request request = input.getData();
    	
    	//Log request info to context logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
		ChatterForum forum = forumDao.createForum(request);
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
    public ForumServiceResponse<ChatterForum> retrieveForumById(ForumServiceRequest<Request> input,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	ForumServiceResponse<ChatterForum> response = new ForumServiceResponse<>();
    	Request request = input.getData();
    	
    	//Log request info to context logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ChatterForum forum = forumDao.retrieveForumById(request);
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
    public ForumServiceResponse<ChatterForum> updateForum(ForumServiceRequest<Request> input,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	ForumServiceResponse<ChatterForum> response = new ForumServiceResponse<>();
    	Request request = input.getData();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ChatterForum forum = forumDao.updateForum(request);
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
    public ForumServiceResponse<ChatterForum> addCommentToForum(ForumServiceRequest<Request> input,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
		ForumServiceResponse<ChatterForum> response = new ForumServiceResponse<>();
		Request request = input.getData();
		
		// Log request info to lambda logger
		LambdaLogger logger = context.getLogger();
		logger.log(request.toString());
		
		ChatterForum forum = forumDao.addCommentToForum(request);
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
    public ForumServiceResponse<ChatterForum> removeCommentFromForum(ForumServiceRequest<Request>
    		input, Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	ForumServiceResponse<ChatterForum> response = new ForumServiceResponse<>();
		Request request = input.getData();
		
		// Log request info to lambda logger
		LambdaLogger logger = context.getLogger();
		logger.log(request.toString());
		
		ChatterForum forum = forumDao.removeCommentFromForum(request);
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
     */
    public ForumServiceResponse<ForumResultPage> queryByCreator(ForumServiceRequest<Request> input,
    	Context context) throws RequestValidationException, AmazonServiceException,
    		AmazonClientException {
    	ForumServiceResponse<ForumResultPage> response = new ForumServiceResponse<>();
    	Request request = input.getData();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ForumResultPage forumResultPage = forumDao.queryByCreator(request);
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
     */
    public ForumServiceResponse<ForumResultPage> queryByTitle(ForumServiceRequest<Request> input,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	ForumServiceResponse<ForumResultPage> response = new ForumServiceResponse<>();
    	Request request = input.getData();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	ForumResultPage forumResultPage = forumDao.queryByTitle(request);
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
    public ForumServiceResponse<Void> deleteForum(ForumServiceRequest<Request> input, 
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	ForumServiceResponse<Void> response = new ForumServiceResponse<>();
    	Request request = input.getData();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	boolean opSuccess = forumDao.deleteForum(request);
    	response.setPayload(null);
    	response.setStatus(true);
    	response.setExceptionThrown(false);
    	response.setExceptionMessage(null);
    	
    	if(opSuccess) 
    		response.setMessage(ServiceMessages.OPERATION_SUCCESS.toString());
    	else
    		response.setMessage(ServiceMessages.OPERATION_FAILED.toString());
    	return response;
    }
    
    /**
     * Pings the service to ensure availability. Response includes
     * service information.
     * @param input the request to process
     * @param context the request context
     * @return a ForumServiceResponse
     */
    public ForumServiceResponse<ServicePropsResponse> pingService(ForumServiceRequest<Request> input, 
    		Context context) {
    	ForumServiceResponse<ServicePropsResponse> response = new ForumServiceResponse<>();
    	Request request = input.getData();
    	
    	// Log request info to lambda logger
    	LambdaLogger logger = context.getLogger();
    	logger.log(request.toString());
    	
    	response.setPayload(forumDao.getServiceProperties());
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
    	builder.append("Exception message: ").append(ex.getMessage());
    	builder.append("Exception Details: ");
    	
    	for(StackTraceElement element : ex.getStackTrace()) {
    		builder.append("\n").append(element.toString());
    	}
    	return builder.toString();
    }
}
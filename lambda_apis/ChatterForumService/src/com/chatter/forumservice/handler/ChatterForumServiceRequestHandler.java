package com.chatter.forumservice.handler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chatter.forumservice.dao.ForumDAO;
import com.chatter.forumservice.dao.ForumDAOImpl;
import com.chatter.forumservice.exceptions.RequestValidationException;
import com.chatter.forumservice.requests.CreateForumRequest;
import com.chatter.forumservice.requests.ForumServiceRequest;
import com.chatter.forumservice.requests.Request;
import com.chatter.forumservice.responses.ForumServiceResponse;
import com.chatter.forumservice.util.ServiceMessages;
import com.chatter.forumservice.util.ServiceOperations;
import com.chatter.model.ChatterForum;

public class ChatterForumServiceRequestHandler implements RequestHandler<Object, Object> {
	
	private ForumDAO forumDao = new ForumDAOImpl();
	
    @Override
    public ForumServiceResponse<? extends Object> handleRequest(Object input, Context context) {
        if(input != null) {
        	@SuppressWarnings("unchecked")
        	ForumServiceRequest<Request> req = (ForumServiceRequest<Request>) input;
        	Request reqData = req.getData();
        	ServiceOperations operation = reqData.getOperation();
        	
        	try {
        		switch(operation) {
        			case CREATE:
        				return this.createForum(req, context);
        			case QUERY_BY_ID:
        				break;
        			case QUERY_BY_CREATOR:
        				break;
        			case QUERY_BY_TITLE:
        				break;
        			case UPDATE:
        				break;
        			case ADD_COMMENT:
        				break;
        			case DELETE:
        				break;
        			default:
        				throw new UnsupportedOperationException("ERROR! The service "
        						+ "does not support operation: " + operation.toString());
        		}
        	}
        	catch (UnsupportedOperationException uoe) {
        		ForumServiceResponse<Void> response = new ForumServiceResponse<>();
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.UNSUPPORTED_OPERATION.toString());
        		response.setExceptionThrown(true);
        		response.setExceptionMessage(uoe.getMessage());
        	}
        	catch (RequestValidationException rve) {
        		ForumServiceResponse<Void> response = new ForumServiceResponse<>();
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.OPERATION_FAILED.toString());
        		response.setExceptionThrown(true);
        		response.setExceptionMessage(this.compileExceptionMessage(rve));
        	}
        	catch (AmazonServiceException ase) {
        		ForumServiceResponse<Void> response = new ForumServiceResponse<>();
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.OPERATION_FAILED.toString());
        		response.setExceptionThrown(true);
        		response.setExceptionMessage(this.compileExceptionMessage(ase));
        	}
        	catch (AmazonClientException ace) {
        		ForumServiceResponse<Void> response = new ForumServiceResponse<>();
        		response.setPayload(null);
        		response.setStatus(false);
        		response.setMessage(ServiceMessages.OPERATION_FAILED.toString());
        		response.setExceptionThrown(true);
        		response.setExceptionMessage(this.compileExceptionMessage(ace));
        	}
        }
        
        return null;
    }
    
    public ForumServiceResponse<ChatterForum> createForum(ForumServiceRequest<Request> input,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	ForumServiceResponse<ChatterForum> response = new ForumServiceResponse<>();
    	CreateForumRequest request = (CreateForumRequest) input.getData();
    	
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
     * Compiles an exception message to add to response
 	 * when an exception is encountered in the service
 	 * handler. The exception message details the exception
 	 * in a log friendly fashion.
     * @param ex the Exception that was caught
     * @return
     */
    private String compileExceptionMessage(Exception ex) {
    	StringBuilder builder = new StringBuilder();
    	builder.append("An Exception was caught in service handler:");
    	
    	for(StackTraceElement element : ex.getStackTrace()) {
    		builder.append("\n").append(element.toString());
    	}
    	return builder.toString();
    }
}
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
    			
    		}
    		catch (MissingOperationException moe) {
    			
    		}
    		catch (UnsupportedOperationException uoe) {
    			
    		}
    		catch (RequestValidationException rve) {
    			
    		}
    		catch (AmazonClientException ace) {
    			
    		}
    	}
    	
    	// If the request input is NULL return NULL response
        return null;
    }
    
    public ServiceResponse<ChatterComment> createComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<ChatterComment> retrieveComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<ChatterComment> updateComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<Void> deleteComment(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<CommentResultPage> queryByCreator(CommentCRUDRequest request, 
    		Context context) throws RequestValidationException, PropertyRetrievalException,
    			AmazonServiceException, AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<CommentResultPage> queryByForum(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, PropertyRetrievalException,
    			AmazonServiceException, AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<List<ChatterComment>> batchRetrieve(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<List<String>> batchDelete(CommentCRUDRequest request,
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	// TODO: implement logic
    	return null;
    }
    
    public ServiceResponse<ServicePropsResponse> pingService(CommentCRUDRequest request,
    		Context context) throws PropertyRetrievalException {
    	// TODO: implement logic
    	return null;
    }
}

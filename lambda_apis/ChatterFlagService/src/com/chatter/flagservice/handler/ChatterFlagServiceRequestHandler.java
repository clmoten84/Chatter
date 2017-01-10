package com.chatter.flagservice.handler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.chatter.flagservice.dao.FlagDAO;
import com.chatter.flagservice.dao.FlagDAOImpl;
import com.chatter.flagservice.exceptions.MissingOperationException;
import com.chatter.flagservice.exceptions.PropertyRetrievalException;
import com.chatter.flagservice.exceptions.RequestValidationException;
import com.chatter.flagservice.model.ChatterFlag;
import com.chatter.flagservice.requests.FlagServiceRequest;
import com.chatter.flagservice.requests.Request;
import com.chatter.flagservice.responses.FlagServiceResponse;
import com.chatter.flagservice.responses.ServicePropsResponse;
import com.chatter.flagservice.util.ServiceMessages;
import com.chatter.flagservice.util.ServiceOperations;

public class ChatterFlagServiceRequestHandler implements 
RequestHandler<FlagServiceRequest<Request>, FlagServiceResponse<? extends Object>> {
	
	FlagDAO dao = null;
	
    @Override
    public FlagServiceResponse<? extends Object> handleRequest(FlagServiceRequest<Request>input, 
    		Context context) {
    	// Retrieve logger
    	LambdaLogger log = context.getLogger();
    	
    	if (input != null) {
    		Request reqData = input.getData();
    		
    		// Define the response to be returned on error conditions
    		FlagServiceResponse<Void> response = new FlagServiceResponse<>();
    		
    		try {
    			// Initialize FlagDAO
    			dao = new FlagDAOImpl();
    			
    			ServiceOperations op = reqData.getOperation();
    			if (op != null) {
    				switch (op) {
    					case CREATE:
    						return this.createFlag(input, context);
    					case RETRIEVE:
    						break;
    					case UPDATE:
    						break;
    					case DELETE:
    						break;
    					case PING:
    						return this.pingService(input, context);
						default:
							throw new UnsupportedOperationException("ERROR: The service "
									+ "does not support operation: " + op.toString());
    				}
    			}
    			else {
    				throw new MissingOperationException("ERROR: The incoming request "
    						+ "is missing a required operation attribute!");
    			}
    		}
    		catch (MissingOperationException moe) {
    			
    		}
    		catch (UnsupportedOperationException uoe) {
    			
    		}
    		catch (RequestValidationException rve) {
    			
    		}
    		catch (PropertyRetrievalException pre) {
    			
    		}
    		catch (AmazonServiceException ase) {
    			
    		}
    		catch (AmazonClientException ace) {
    			
    		}
    	}

        return null;
    }
    
    /**
     * Create and save a new ChatterFlag object to the database.
     * @param input the request to process
     * @param context the request context
     * @return FlagServiceResponse containing the created ChatterFlag object
     * @throws RequestValidationException
     * @throws AmazonServiceException
     * @throws AmazonClientException
     */
    public FlagServiceResponse<ChatterFlag> createFlag(FlagServiceRequest<Request> input, 
    		Context context) throws RequestValidationException, AmazonServiceException,
    			AmazonClientException {
    	FlagServiceResponse<ChatterFlag> response = new FlagServiceResponse<>();
    	Request request = input.getData();
    	
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
     * Ping the service to ensure availability. Response includes
     * service property information.
     * @param input the request to process
     * @param context the request context
     * @return FlagServiceResponse
     * @throws PropertyRetrievalException
     */
    public FlagServiceResponse<ServicePropsResponse> pingService(FlagServiceRequest<Request>
    		input, Context context) throws PropertyRetrievalException{
    	FlagServiceResponse<ServicePropsResponse> response = new FlagServiceResponse<>();
    	Request request = input.getData();
    	
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
    	builder.append("Exception message: ").append(ex.getMessage());
    	builder.append("Exception details: ");
    	
    	for (StackTraceElement elem : ex.getStackTrace()) {
    		builder.append("\n").append(elem.toString());
    	}
    	
    	return builder.toString();
    }

}

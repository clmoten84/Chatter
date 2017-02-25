package com.chatter.dbservice.handlers;

import com.amazonaws.services.dynamodbv2.model.StreamRecord;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent.DynamodbStreamRecord;


/**
 * ChatterForumDBEventHandler
 * @author coreym
 *
 * Handles events found on the ChatterForum
 * DB stream.
 */
public class ChatterForumDBEventHandler implements
	RequestHandler<DynamodbEvent, String>{

	@Override
	public String handleRequest(DynamodbEvent event, Context ctx) {
		
		// Get logger from context
		LambdaLogger logger = ctx.getLogger();
		
		// Process event
		if (event != null) {
			// Get properties from the event object
			for (DynamodbStreamRecord rec : event.getRecords()) {
				String eventId = rec.getEventID();
				String awsRegion = rec.getAwsRegion();
				String eventName = rec.getEventName();
				StreamRecord eventRecord = rec.getDynamodb();
				
				// To process properly, need to know the name of the
				// table affected, the item's id, the type of operation
				// that triggered the event (i.e. INSERT, MODIFY, or
				// REMOVE
			}
		}
		
		return null;
	}

}

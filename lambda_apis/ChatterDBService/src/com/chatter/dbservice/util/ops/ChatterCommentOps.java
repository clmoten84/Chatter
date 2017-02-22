package com.chatter.dbservice.util.ops;

/**
 * ChatterCommentOps
 * @author coreym
 *
 * Types of DB CRUD operations that can be performed on 
 * ChatterComment data.
 */
public enum ChatterCommentOps {
	CREATE, RETRIEVE, DELETE, QUERY_BY_CREATOR,
	QUERY_BY_FORUM, BATCH_RETRIEVE, BATCH_DELETE, ADD_REPLY,
	ADD_FLAG, REMOVE_REPLY, REMOVE_FLAG, INCREMENT_CONCUR, PING,
	SERVICE_INFO
}

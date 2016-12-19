package com.chatter.forumservice.util;

/**
 * ServiceOperation
 * @author coreym
 * 
 * Possible operations that this service can perform:
 * 
 * CREATE
 * QUERY_BY_ID
 * QUERY_BY_CREATOR
 * QUERY_BY_TITLE
 * UPDATE
 * DELETE
 * ADD_COMMENT
 * REMOVE_COMMENT
 * PING
 */
public enum ServiceOperations {
	CREATE, QUERY_BY_ID, QUERY_BY_CREATOR,
	QUERY_BY_TITLE, UPDATE, DELETE, ADD_COMMENT,
	REMOVE_COMMENT, PING
}

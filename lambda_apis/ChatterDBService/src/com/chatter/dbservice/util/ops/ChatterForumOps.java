package com.chatter.dbservice.util.ops;

/**
 * ChatterForumOps
 * @author coreym
 *
 * Types of DB CRUD operations that can be performed on 
 * ChatterForum data.
 */
public enum ChatterForumOps {
	CREATE, QUERY_BY_ID, QUERY_BY_CREATOR,
	QUERY_BY_TITLE, UPDATE, DELETE, ADD_COMMENT,
	REMOVE_COMMENT, PING
}

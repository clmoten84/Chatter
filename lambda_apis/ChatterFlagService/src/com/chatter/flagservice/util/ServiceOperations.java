package com.chatter.flagservice.util;

/**
 * ServiceOperations
 * @author coreym
 *
 * Enumeration of all possible service operations.
 * 
 * CREATE - create and save a Flag object
 * RETRIEVE - retrieve a Flag object by id
 * UPDATE - update a Flag object by id
 * DELETE - delete a Flag object by id
 * PING - ping service for availability
 */
public enum ServiceOperations {
	CREATE, RETRIEVE, UPDATE, DELETE, PING
}

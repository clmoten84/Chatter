package com.chatter.fileservice.fao;

import com.amazonaws.AmazonClientException;
import com.chatter.fileservice.requests.ServiceRequest;
import com.chatter.fileservice.responses.FileMetadata;

public interface FileAccessObject {

	/**
	 * Save a file object to file storage
	 * @param req
	 * @return
	 */
	public FileMetadata saveFile(ServiceRequest req) throws AmazonClientException;
	
	/**
	 * Delete a file object from file storage
	 * @param req
	 */
	public void deleteFile(ServiceRequest req) throws AmazonClientException;
}

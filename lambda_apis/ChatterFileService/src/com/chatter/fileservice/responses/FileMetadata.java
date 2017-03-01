package com.chatter.fileservice.responses;

import java.util.Map;

/**
 * FileMetadata
 * @author coreym
 *
 * Contains metadata that is returned when new files are saved to
 * S3.
 */
public class FileMetadata {

	// Typical metadata
	private String eTag;
	private Long contentLength;
	private String contentType;
	
	// Custom user defined metadata
	private Map<String, String> userMetadata;
	
	public FileMetadata() { }

	public String geteTag() {
		return eTag;
	}

	public void seteTag(String eTag) {
		this.eTag = eTag;
	}

	public Long getContentLength() {
		return contentLength;
	}

	public void setContentLength(Long contentLength) {
		this.contentLength = contentLength;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	public Map<String, String> getUserMetadata() {
		return userMetadata;
	}
	
	public void setUserMetadata(Map<String, String> userMetadata) {
		this.userMetadata = userMetadata;
	}
}

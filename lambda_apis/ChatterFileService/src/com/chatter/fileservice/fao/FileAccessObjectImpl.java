package com.chatter.fileservice.fao;

import java.io.ByteArrayInputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.chatter.fileservice.exceptions.PropertyRetrievalException;
import com.chatter.fileservice.requests.ServiceRequest;
import com.chatter.fileservice.responses.FileMetadata;
import com.chatter.fileservice.util.PropertiesResolver;

public class FileAccessObjectImpl implements FileAccessObject{
	
	private PropertiesResolver propsResolver;
	private AmazonS3Client s3Client;
	
	public FileAccessObjectImpl() throws PropertyRetrievalException {
		// Initialize props resolver
		propsResolver = new PropertiesResolver("service.properties");
		String env = propsResolver.getProperty("service.env");
		
		// Instantiate S3 client object
		if (env.equalsIgnoreCase("local")) {
			this.s3Client = new AmazonS3Client(new ProfileCredentialsProvider());
		}
		else {
			this.s3Client = new AmazonS3Client(new EnvironmentVariableCredentialsProvider());
		}
	}

	/**
	 * Save a new file object to S3
	 */
	@Override
	public FileMetadata saveFile(ServiceRequest req)
			throws AmazonClientException {
		
		FileMetadata fileMetadata = null;
		
		// Generate metadata object from arguments in request
    	ObjectMetadata metadata = new ObjectMetadata();
    	metadata.addUserMetadata("createdBy", (String) req.getArgs().get("createdBy"));
    	metadata.addUserMetadata("bucketName", (String) req.getArgs().get("bucketName"));
    	metadata.addUserMetadata("keyName", (String) req.getArgs().get("keyName"));
    	metadata.addUserMetadata("dateCreated", (String) req.getArgs().get("dateCreated"));
    	metadata.setContentLength(Long.valueOf(req.getFileData().length));
    	
    	// Need to also set content md5 with base64 encoded raw MD5 byte array
    	byte[] resultByte = DigestUtils.md5(req.getFileData());
    	String md5 = new String(Base64.encodeBase64(resultByte));
    	metadata.setContentMD5(md5);

    	if (req.getArgs().containsKey("commentId")) {
    		metadata.addUserMetadata("commentId", (String) req.getArgs().get("commentId"));
    	}
    	
    	if (req.getArgs().containsKey("forumId")) {
    		metadata.addUserMetadata("forumId", (String) req.getArgs().get("forumId"));
    	}
    	
    	PutObjectResult result = this.s3Client.putObject(
    			(String) req.getArgs().get("bucketName"),
    			(String) req.getArgs().get("keyName"),
    			new ByteArrayInputStream(req.getFileData()),
    			metadata);
    	
    	if (result != null) {
    		fileMetadata = new FileMetadata();
    		
    		if (result.getMetadata() != null) {
    			fileMetadata.seteTag(result.getETag());
    			fileMetadata.setContentLength(result.getMetadata().getContentLength());
    			fileMetadata.setContentType(result.getMetadata().getContentType());
    			fileMetadata.setUserMetadata(result.getMetadata().getUserMetadata());
    		}
    	}
    	
    	return fileMetadata;
	}

	/**
	 * Delete a file object from S3
	 */
	@Override
	public void deleteFile(ServiceRequest req) throws AmazonClientException {
		// Generate delete request and execute operation
    	DeleteObjectRequest delReq = new DeleteObjectRequest(
    		(String) req.getArgs().get("bucketName"),
    		(String) req.getArgs().get("keyName")
    	);
    	s3Client.deleteObject(delReq);
	}
}

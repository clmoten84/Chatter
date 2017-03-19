package com.chatter.fileservice.integration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.Assert;

import com.amazonaws.services.lambda.runtime.Context;
import com.chatter.fileservice.handlers.ChatterFileAPIRequestHandler;
import com.chatter.fileservice.requests.ServiceRequest;
import com.chatter.fileservice.responses.FileMetadata;
import com.chatter.fileservice.responses.ServiceProps;
import com.chatter.fileservice.responses.ServiceResponse;
import com.chatter.fileservice.util.ServiceMessages;
import com.chatter.fileservice.util.ServiceOps;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class APIRequestHandlerTest {
	
	/**
	 * TestFile
	 * @author coreym
	 *
	 * Class for encapsulating data for creating a temporary file
	 * for use during service testing.
	 */
	private class TestFile {
		private byte[] fileBytes;
		private String fileName;
		
		public byte[] getFileBytes() {
			return fileBytes;
		}
		
		public void setFileBytes(byte[] fileBytes) {
			this.fileBytes = fileBytes;
		}
		
		public String getFileName() {
			return fileName;
		}
		
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
	}

	/* Globals */
    private ChatterFileAPIRequestHandler handler = new ChatterFileAPIRequestHandler();
    private Context ctx = this.createContext();
    
    /**
     * Initializes test lambda context
     * @return
     */
    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("Chatter File API handler");
        return ctx;
    }
    
    /**
     * Generates sample ping service request
     * @return
     */
    private ServiceRequest generateSamplePingRequest() {
    	ServiceRequest request = new ServiceRequest();
    	request.setOperation(ServiceOps.PING);
    	request.setReqDate(new Date().getTime());
    	return request;
    }
    
    /**
     * Generates sample service info request
     * @return
     */
    private ServiceRequest generateSampleServiceInfoRequest() {
    	ServiceRequest request = new ServiceRequest();
    	request.setOperation(ServiceOps.SERVICE_INFO);
    	request.setReqDate(new Date().getTime());
    	return request;
    }
    
    /**
     * Generates sample save file request
     * @return
     */
    private ServiceRequest generateSampleSaveRequest(TestFile testFile) {
    	// Map of arguments for save request
    	Map<String, String> args = new HashMap<>();
    	args.put("createdBy", "FileServiceTest");
    	args.put("bucketName", "chatter-test-data");
    	args.put("keyName", testFile.getFileName());
    	args.put("dateCreated", new Date().toString());
    	
    	ServiceRequest request = new ServiceRequest();
    	request.setOperation(ServiceOps.SAVE_FILE);
    	request.setReqDate(new Date().getTime());
    	request.setArgs(args);
    	request.setFileData(testFile.getFileBytes());
    	return request;
    }
    
    /**
     * Generates sample delete file request
     * @return
     */
    private ServiceRequest generateSampleDeleteRequest(TestFile testFile) {
    	// Map of arguments for delete request
    	Map<String, String> args = new HashMap<>();
    	args.put("bucketName", "chatter-test-data");
    	args.put("keyName", testFile.getFileName());
    	
    	ServiceRequest request = new ServiceRequest();
    	request.setOperation(ServiceOps.DELETE_FILE);
    	request.setReqDate(new Date().getTime());
    	request.setArgs(args);
    	return request;
    }
    
    /**
     * Generates a test file to use for uploading purposes.
     * Saves the file to the project resources directory.
     * @return
     */
    private TestFile generateTestFile() {
    	String prefix = "chatter_test";
    	String suffix = ".txt";
    	TestFile testFile = null;
    	
    	try {
    		// Create temp file
    		File file = File.createTempFile(prefix, suffix);
    		file.deleteOnExit();
    		
    		// Write 10 lines of text to temp file
    		FileOutputStream fos = new FileOutputStream(file);
    		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
    		for (int i = 0; i < 10; i++) {
    			bw.write("This is just a test file being uploaded to AWS S3!");
    			bw.newLine();
    		}
    		bw.close();
    		System.out.println("Test file successfully created: " + file.getName());
    		
    		testFile = new TestFile();
    		testFile.setFileBytes(Files.readAllBytes(file.toPath()));
    		testFile.setFileName(file.getName());
    	}
    	catch (IOException ioe) {
    		System.out.println("An error occurred while during test file creation!");
    		ioe.printStackTrace();
    	}
    	
    	return testFile;
    }
    
    /* ************************** TESTS ******************************* */
    
    @Test
    public void testPingRequest() {
    	ServiceRequest pingReq = this.generateSamplePingRequest();
    	
    	@SuppressWarnings("unchecked")
		ServiceResponse<String> props = (ServiceResponse<String>) 
    			this.handler.handleRequest(pingReq, this.ctx);
    	
    	Assert.assertTrue(props.getStatus());
    	Assert.assertTrue(props.getMessage().equalsIgnoreCase(
    			ServiceMessages.OPERATION_SUCCESS.toString()));
    	Assert.assertFalse(props.getExceptionThrown());
    	Assert.assertNull(props.getExceptionMessage());
    	Assert.assertTrue(props.getPayload().equalsIgnoreCase("Ping received!"));
    }
    
    @Test
    public void testGetServiceInfo() {
    	ServiceRequest infoReq = this.generateSampleServiceInfoRequest();
    	
    	@SuppressWarnings("unchecked")
    	ServiceResponse<ServiceProps> resp = (ServiceResponse<ServiceProps>)
    			this.handler.handleRequest(infoReq,  this.ctx);
    	
    	Assert.assertTrue(resp.getStatus());
    	Assert.assertTrue(resp.getMessage().equalsIgnoreCase(
    			ServiceMessages.OPERATION_SUCCESS.toString()));
    	Assert.assertFalse(resp.getExceptionThrown());
    	Assert.assertNull(resp.getExceptionMessage());
    	Assert.assertNotNull(resp.getPayload());
    }
    
    @Test
    public void testSaveAndDeleteFile() {
    	TestFile testFile = this.generateTestFile();
    	ServiceRequest saveFileReq = this.generateSampleSaveRequest(testFile);
    	ServiceRequest deleteFileReq = this.generateSampleDeleteRequest(testFile);
    	
    	// Execute save file operation
    	@SuppressWarnings("unchecked")
    	ServiceResponse<FileMetadata> saveResp = (ServiceResponse<FileMetadata>)
    		this.handler.handleRequest(saveFileReq, this.ctx);
    	
    	// Save file response assertions
    	Assert.assertTrue(saveResp.getStatus());
    	Assert.assertTrue(saveResp.getMessage().equalsIgnoreCase(
    			ServiceMessages.OPERATION_SUCCESS.toString()));
    	Assert.assertFalse(saveResp.getExceptionThrown());
    	Assert.assertNull(saveResp.getExceptionMessage());
    	Assert.assertNotNull(saveResp.getPayload());
    	
    	FileMetadata metadata = saveResp.getPayload();
    	if (metadata != null) {
    		Map<String, String> userMetadata = metadata.getUserMetadata();
    		Assert.assertTrue(userMetadata.get("createdBy").equals("FileServiceTest"));
    		Assert.assertTrue(userMetadata.get("bucketName").equals("chatter-test-data"));
    		Assert.assertTrue(userMetadata.get("keyName").equals(testFile.getFileName()));
    		Assert.assertNotNull(userMetadata.get("dateCreated"));
    	}
    	
    	// Execute delete file operation
    	@SuppressWarnings("unchecked")
    	ServiceResponse<Void> deleteResp = (ServiceResponse<Void>)
    		this.handler.handleRequest(deleteFileReq, this.ctx);
    	
    	// Delete file response assertions
    	Assert.assertTrue(deleteResp.getStatus());
    	Assert.assertTrue(deleteResp.getMessage().equals(
    			ServiceMessages.OPERATION_SUCCESS.toString()));
    	Assert.assertFalse(deleteResp.getExceptionThrown());
    	Assert.assertNull(deleteResp.getExceptionMessage());
    }
}

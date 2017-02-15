package com.chatter.dbservice.unit;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import com.amazonaws.AmazonClientException;
import com.chatter.dbservice.dao.FlagDAO;
import com.chatter.dbservice.dao.impl.FlagDAOImpl;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.exceptions.RequestValidationException;
import com.chatter.dbservice.model.ChatterFlag;
import com.chatter.dbservice.requests.FlagCRUDRequest;
import com.chatter.dbservice.responses.ServicePropsResponse;
import com.chatter.dbservice.util.ops.ChatterFlagOps;

/**
 * ChatterFlagDAOTest
 * 
 * Since each method in this test class requires different
 * set up and tear down. The set up and tear down is
 * handled by each individual test method. This prevents
 * tests from being interdependent.
 * @author coreym
 *
 * Unit tests for Chatter Flag DAO
 */
public class ChatterFlagDAOTest {

	/* Globals */
	private FlagDAO dao;
	
	/**
	 * Initialize the DAO to be tested
	 * @return FlagDAO
	 */
	private FlagDAO getDAO() {
		if (this.dao == null) {
			try {
				this.dao = new FlagDAOImpl();
			}
			catch (PropertyRetrievalException pre) {
				System.out.println("Could not retrieve necessary"
						+ " properties to generate Forum DAO!");
			}
		}
		
		return this.dao;
	}
	
	/**
	 * Initialize a test Chatter Flag instance
	 * @return ChatterFlag
	 */
	private ChatterFlag createTestFlag() {
		ChatterFlag flag = new ChatterFlag();
		flag.setCreatedBy("dbservice");
		flag.setFlagDescription("This is a test Chatter Flag!");
		flag.setForumId("1234-TEST");
		flag.setCommentId("54321-SHITTY");
		flag.setTimeStamp(new Date().getTime());
		return flag;
	}
	
	/**
	 * Initializes a list of 20 ChatterFlag objects
	 * @return
	 */
	private List<ChatterFlag> createTestFlags() {
		List<ChatterFlag> flags = new ArrayList<>();
		for (int i = 0; i < 20; i++) {
			ChatterFlag flag = new ChatterFlag();
			flag.setCreatedBy("dbservice");
			flag.setFlagDescription(("This is test flag #" + i));
			flag.setForumId("4321-TEST");
			flag.setCommentId("1234-TEST");
			flag.setTimeStamp(new Date().getTime());
			flags.add(flag);
		}
		return flags;
	}
	
	/* ******************** DAO Argument Creation ******************** */
	
	/**
	 * Generate arguments for creating and saving a new ChatterFlag instance
	 * to the database.
	 * @param flagIn
	 * @return
	 */
	private FlagCRUDRequest generateCreateArgs(ChatterFlag flagIn) {
		// Initialize request object
		FlagCRUDRequest request = new FlagCRUDRequest();
		request.setOperation(ChatterFlagOps.CREATE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("createdBy", flagIn.getCreatedBy());
		args.put("forumId", flagIn.getForumId());
		args.put("commentId", flagIn.getCommentId());
		args.put("flagDescription", flagIn.getFlagDescription());
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate arguments for retrieving a ChatterFlag instance from the
	 * database.
	 * @param flagId
	 * @return
	 */
	private FlagCRUDRequest generateRetrieveArgs(String flagId) {
		// Initialize request object
		FlagCRUDRequest request = new FlagCRUDRequest();
		request.setOperation(ChatterFlagOps.RETRIEVE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("flagId", flagId);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate arguments for updating a ChatterFlag instance in the
	 * database.
	 * @param flagId
	 * @return
	 */
	private FlagCRUDRequest generateUpdateArgs(String flagId) {
		// Initialize request object
		FlagCRUDRequest request = new FlagCRUDRequest();
		request.setOperation(ChatterFlagOps.UPDATE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("flagId", flagId);
		args.put("flagDescriptionUpdate", "Updated Chatter Flag description!");
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate arguments for deleting a ChatterFlag instance from
	 * the database.
	 * @param flagId
	 * @return
	 */
	private FlagCRUDRequest generateDeleteArgs(String flagId) {
		// Initialize request object
		FlagCRUDRequest request = new FlagCRUDRequest();
		request.setOperation(ChatterFlagOps.DELETE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, String> args = new HashMap<>();
		args.put("flagId", flagId);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate arguments for retrieving a batch of ChatterFlag instances
	 * from the database.
	 * @param flagIds
	 * @return
	 */
	private FlagCRUDRequest generateBatchRetrieveArgs(List<String> flagIds) {
		// Initialize request object
		FlagCRUDRequest request = new FlagCRUDRequest();
		request.setOperation(ChatterFlagOps.BATCH_RETRIEVE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, List<String>> args = new HashMap<>();
		args.put("flagIds", flagIds);
		
		request.setArgs(args);
		return request;
	}
	
	/**
	 * Generate arguments for deleting a batch of ChatterFlag instances
	 * from the database.
	 * @param flagIds
	 * @return
	 */
	private FlagCRUDRequest generateBatchDeleteArgs(List<String> flagIds) {
		// Initialize request object
		FlagCRUDRequest request = new FlagCRUDRequest();
		request.setOperation(ChatterFlagOps.BATCH_DELETE);
		request.setReqDate(new Date().getTime());
		
		// Generate map of custom args
		Map<String, List<String>> args = new HashMap<>();
		args.put("flagIds", flagIds);
		
		request.setArgs(args);
		return request;
	}
	
	/* ******************** TESTS ******************** */
	
	/**
	 * Tests pinging the service from ChatterFlag service code
	 */
	@Test
	public void testPingService() {
		System.out.println("Flag DAO Test: testPingService()");
		
		FlagDAO dao = this.getDAO();
		
		try {
			ServicePropsResponse props = dao.getServiceProperties();
			
			// Assertions
			Assert.assertNotNull(props);
		}
		catch (PropertyRetrievalException pre) {
			System.out.println("An exception occurred while trying to retrieve "
					+ "service properties.");
		}
	}
	
	/**
	 * Tests creating and saving a new ChatterFlag instance to the database,
	 * and then deleting the created ChatterFlag instance.
	 */
	@Test
	public void testCreateAndDeleteFlag() {
		System.out.println("Flag DAO Test: testCreateAndDeleteFlag()");
		
		FlagDAO dao = this.getDAO();

			try {
				// Create and save a new ChatterFlag instance to DB
				ChatterFlag flag = dao.createFlag(this.generateCreateArgs(
						this.createTestFlag()));
				
				Assert.assertNotNull(flag);
				Assert.assertNotNull(flag.getFlagId());
				Assert.assertEquals("dbservice", flag.getCreatedBy());
				Assert.assertEquals("1234-TEST", flag.getForumId());
				Assert.assertEquals("54321-SHITTY", flag.getCommentId());
				
				// Delete created ChatterFlag instance from DB
				boolean deleteSucceeded = dao.deleteFlag(this.generateDeleteArgs(
						flag.getFlagId()));
				
				Assert.assertTrue(deleteSucceeded);
			}
			catch (RequestValidationException rve) {
				rve.printStackTrace();
			}
			catch (AmazonClientException ace) {
				ace.printStackTrace();
			}
	}
	
	/**
	 * Tests retrieving an existing ChatterFlag instance from DB.
	 * SETUP: create and save a new ChatterFlag instance to DB
	 * CLEAN UP: delete created ChatterFlag instance from DB
	 */
	@Test
	public void testRetrieveFlag() {
		System.out.println("Flag DAO Test: testRetrieveFlag()");
		
		FlagDAO dao = this.getDAO();
		
		try {
			/* ********** SET UP ********** */
			ChatterFlag flag = dao.createFlag(this.generateCreateArgs(
					this.createTestFlag()));
			
			Assert.assertNotNull(flag);
			
			// Attempt to retrieve Flag from DB
			ChatterFlag retFlag = dao.retrieveFlag(this.generateRetrieveArgs(flag.getFlagId()));
			
			Assert.assertNotNull(retFlag);
			Assert.assertEquals(flag.getFlagId(), retFlag.getFlagId());
			Assert.assertEquals(flag.getCreatedBy(), retFlag.getCreatedBy());
			Assert.assertEquals(flag.getForumId(), retFlag.getForumId());
			Assert.assertEquals(flag.getCommentId(), retFlag.getCommentId());
			
			/* ********** CLEAN UP ********** */
			boolean deleteSucceeded = dao.deleteFlag(this.generateDeleteArgs(flag.getFlagId()));
			Assert.assertTrue(deleteSucceeded);
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	/**
	 * Tests updating an existing ChatterFlag instance from DB
	 * SETUP: create and save a new ChatterFlag instance in the DB
	 * CLEAN UP: delete the ChatterFlag instance created during setup
	 */
	@Test
	public void testUpdateFlag() {
		System.out.println("Flag DAO Test: testUpdateFlag()");
		
		FlagDAO dao = this.getDAO();
		
		try {
			/* ********** SET UP ********** */
			ChatterFlag flag = dao.createFlag(this.generateCreateArgs(this.createTestFlag()));
			Assert.assertNotNull(flag);
			
			// Attempt to update Flag in DB
			flag = dao.updateFlag(this.generateUpdateArgs(flag.getFlagId()));
			Assert.assertNotNull(flag);
			Assert.assertEquals("Updated Chatter Flag description!", flag.getFlagDescription());
			
			/* ********** CLEAN UP ********** */
			boolean deleteSucceeded = dao.deleteFlag(this.generateDeleteArgs(flag.getFlagId()));
			Assert.assertTrue(deleteSucceeded);
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
	
	/**
	 * Tests retrieving and deleting a batch of ChatterFlag objects from DB
	 * SET UP: create and save a list of ChatterFlag instances to the DB
	 * CLEAN UP: delete the created list of ChatterFlags from the DB
	 */
	@Test
	public void testBatchRetrieveAndDeleteFlags() {
		System.out.println("Flag DAO Test: testBatchRetrieveAndDeleteFlags()");
		
		FlagDAO dao = this.getDAO();
		List<String> flagIds = new ArrayList<>();
		
		try {
			/* ********** SET UP ********** */
			List<ChatterFlag> flags = this.createTestFlags();
			for (ChatterFlag flag : flags) {
				flag = dao.createFlag(this.generateCreateArgs(flag));
				Assert.assertNotNull(flag);
				Assert.assertNotNull(flag.getFlagId());
				flagIds.add(flag.getFlagId());
			}
			
			// Attempt to retrieve batch of Chatter Flags
			List<ChatterFlag> retFlags = dao.batchRetrieveFlag(
					this.generateBatchRetrieveArgs(flagIds));
			Assert.assertNotNull(retFlags);
			Assert.assertEquals(20, retFlags.size());
			
			/* ********** CLEAN UP ********** */
			List<String> delFlags = dao.batchDeleteFlag(this.generateBatchDeleteArgs(flagIds));
			Assert.assertNotNull(delFlags);
			Assert.assertEquals(20, delFlags.size());
		}
		catch (RequestValidationException rve) {
			rve.printStackTrace();
		}
		catch (AmazonClientException ace) {
			ace.printStackTrace();
		}
	}
}

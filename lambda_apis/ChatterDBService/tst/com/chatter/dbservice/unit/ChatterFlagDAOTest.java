package com.chatter.dbservice.unit;

import java.util.Date;

import com.chatter.dbservice.dao.FlagDAO;
import com.chatter.dbservice.dao.impl.FlagDAOImpl;
import com.chatter.dbservice.exceptions.PropertyRetrievalException;
import com.chatter.dbservice.model.ChatterFlag;

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
		flag.setCreatedBy("cmoten");
		flag.setFlagDescription("This is a test Chatter Flag!");
		flag.setForumId("1234-TEST");
		flag.setCommentId("54321-SHITTY");
		flag.setTimeStamp(new Date().getTime());
		return flag;
	}
	
	/* ******************** DAO Argument Creation ******************** */
}

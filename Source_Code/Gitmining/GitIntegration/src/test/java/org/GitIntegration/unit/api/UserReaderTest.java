package org.GitIntegration.unit.api;

import edu.nju.git.PO.UserBriefPO;
import edu.nju.git.data.api.centralization.UserMapReader;
import junit.framework.TestCase;

public class UserReaderTest extends TestCase {

	private UserMapReader reader ;
	
	protected void setUp() throws Exception {
		super.setUp();
		reader = new UserMapReader("huerlisi");
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testGetUserBreifPO()
	{
		UserBriefPO po = reader.getBriefPO();
		assertTrue(po!=null);
	//	System.out.println(po.toString());
	}
}

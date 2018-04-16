import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Contains tests for the WarehouseManage class.
 */
public class WarehouseManagerTest {
	
	
	
	@Before
	public void setUp()  {
		
	}
	
	@After
	public void tearDown()  {
		
	}
	
	@Test
	public void getFreeSequencerTest()  {
		
	}
	
	@Test
	public void translationTableTest() {
		String testDataPath = "translation.csv";
		String translationpath = "/Users/liamscott/Documents/CS/Group/group_0391/project/translation.csv";
		String[] arguments = {translationpath, testDataPath};
		WarehouseManager wm = new WarehouseManager(arguments);
		String[] testKey = {"Blue","SES"};
		Sku[] expectedResult = {new Sku("37"), new Sku("38")};
//		SKU[] actualResult =
		
	}
	
	@Test
	public void requestItemTest()  {
		
	}
	
	@Test
	public void mainTest()  {
		String testDataPath = "translation.csv";
		String translationpath = "/Users/liamscott/Documents/CS/Group/group_0391/project/translation.csv";
		String[] arguments = {translationpath, testDataPath};
		WarehouseManager wm = new WarehouseManager(arguments);
		//TODO: Test the success of the file path with a get on the translation table
	}
	
	@Test
	public void mainTestInvalidArgs()  {
		String testDataPath = "this is not a valid file path";
		String translationpath = "/Users/liamscott/Documents/CS/Group/group_0391/project/translation.csv";
		String[] arguments = {translationpath, testDataPath};
		WarehouseManager wm = new WarehouseManager(arguments);
		//TODO: Need to assert that the error is handled properly.
	}
	
}
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the SKUMap class.
 */
public class SKUMapTest {
	@Before
	public void setUp() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void putAndRetrieveTest() throws Exception {
		SkuMap testMap = new SkuMap();
		String[] testKey = {"White", "S"};
		Sku frontSKU = new Sku("1");
		Sku rearSKU = new Sku("2");
		Sku[] testValues = {frontSKU, rearSKU};
		testMap.put(testKey, testValues);
		Sku[] testResult = testMap.get(testKey);
		Sku[] expectedResult = {frontSKU, rearSKU};
		
		assertEquals(expectedResult, testResult);
	}
	
}
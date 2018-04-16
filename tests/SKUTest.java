import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
/**
 * Test suite for the SKUTest class.
 */
public class SKUTest {
	@Before
	public void setUp() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void toStringTest() throws Exception {
		Sku testSKU = new Sku("12");
		String expectedResult = "12";
		assertEquals(expectedResult, testSKU.toString());
	}
	
}
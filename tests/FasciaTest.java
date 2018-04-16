import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the FasciaTest class.
 */
public class FasciaTest {
	@Before
	public void setUp() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void getSKUTest() throws Exception {
		
	}
	
	@Test
	public void isEqualTestTwoEqual() throws Exception {
		Sku testSKU = new Sku("58");
		Fascia fasciaOne = new Fascia(testSKU, false, "Grey", "S" );
		Fascia fasciaTwo = new Fascia(testSKU, false, "Grey", "S");
		assert fasciaOne.equals(fasciaTwo);
	}
	
}
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the Shelf class.
 */
public class ShelfTest {

	@Test
	public void getShelfSKUTest() throws Exception {
		Fascia fascia = new Fascia(new Sku("12"), false, "White", "S");
		Shelf testShelf = new Shelf(new Sku("12"), fascia, 30);
		Sku expectedResult = new Sku("12");
		assertEquals(expectedResult, testShelf.getShelfSku());
	}
	
	@Test
	public void getRemainingFasciaTest() throws Exception {
		Fascia fascia = new Fascia(new Sku("12"), false, "White", "S");
		Shelf testShelf = new Shelf(new Sku("12"), fascia, 30);
		assertEquals(30, testShelf.getRemainingFascia());
	}
	
	@Test
	public void getFasciaTest() throws Exception {
		Fascia fascia = new Fascia(new Sku("12"), false, "White", "S");
		Shelf testShelf = new Shelf(new Sku("12"), fascia, 30);
		assertEquals(fascia, testShelf.getFascia());
	}
	
	@Test
	public void doResupplyTest() throws Exception {
		Fascia fascia = new Fascia(new Sku("12"), false, "White", "S");
		Shelf testShelf = new Shelf(new Sku("12"), fascia, 15);
		testShelf.doResupply();
		assertEquals(testShelf.getRemainingFascia(), 30);
	}
	
}
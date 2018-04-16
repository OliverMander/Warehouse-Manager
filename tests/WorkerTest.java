import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test suite for the Worker class.
 */
public class WorkerTest {
	@Before
	public void setUp() throws Exception {
		
	}
	
	@After
	public void tearDown() throws Exception {
		
	}
	
	@Test
	public void getWorkerNameTest() {
		String[] testArgs = {"translation.csv","traversal_table.csv"};
		WarehouseManager wm = new WarehouseManager(testArgs);
		WarehousePicking wp = new WarehousePicking();
		Picker testWorker = new Picker("Marcus", wm, wp);
		assertEquals("Marcus", testWorker.getWorkerName());
	}
	
}
import java.util.logging.Level;

public class Replenisher extends Worker {

  private WarehouseManager warehouseManager;


  /**
   * The constructor for a Replenisher.
   * 
   * @param name the name of this Replenisher.
   * @paramr
   */
  public Replenisher(String name, WarehouseManager warehouseManager) {
    super(name, warehouseManager);
    setJobTitle("Replenisher");


  }

  @Override
  public void Working() {
    if (this.getJobTitle().equals("Replenisher")) {
      String zone = getCurrentWorkingLocation()[0];
      int aisleLocation = Integer.parseInt(getCurrentWorkingLocation()[1]);
      int rackLocation = Integer.parseInt(getCurrentWorkingLocation()[2]);
      int levelLocation = Integer.parseInt(getCurrentWorkingLocation()[3]);
      this.getWarehouseManager().getItemStorage().resupplyItem(zone, aisleLocation, rackLocation,
          levelLocation);
    }
  }

  @Override
  public void getNextWorkingOrder() {
    

  }

  @Override
  public void preparingProductForNextStage() {
    getWarehouseManager().getLogger().log(Level.INFO,
        getJobTitle() + " " + getName() + " finish replenishing at loacation of "
            + getCurrentWorkingLocation()[0] + " " + this.getCurrentWorkingLocation()[1] + " "
            + getCurrentWorkingLocation()[2] + " " + getCurrentWorkingLocation()[3]);

  }

  @Override
  public void emergencyCase(OrdersPerProcess ordersPerProcess, int ProductIndex) {
    warehouseManager.getLogger().log(Level.INFO, "No more Item in the reserve room");

  }



}

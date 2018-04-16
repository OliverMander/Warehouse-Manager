import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

/**
 * Picker class responsible for navigation the warehouse and actually putting Fascia from the Shelf
 * unto their fork lift and delivering that Fascia to the staging area.
 */
public class Picker extends Worker {

  /**
   * The amount of Fascia a Picker can fit on their fork lift.
   */
  private int forkliftContentSize = 8;

  private int maxLocation = 7;

  /**
   * The contents of the Picker's fork lift.
   */
  ArrayDeque<Fascia> forkliftContents = new ArrayDeque<>();

  /**
   * The warehouseManager, call helper methods in WarehouseManager.
   */
  private WarehouseManager warehouseManager;

  private int fasciaPerOrder = 8;


  /**
   * Manages the Picker's generic path optimization algorithm.
   */
  private WarehousePicking warehousePicking;

  /**
   * A LinkedList of Strings representing locations in the warehouse.
   */
  List<String> listOfLocations = new LinkedList<>();

  /**
   * An integer representing the Picker's current location in its order list.
   */
  private int nextLocation;

  /**
   * Constructor for the Picker class. Initializes all of the class' member fields.
   * 
   * @param name the worker's name
   * @param wareHousePicking a copy of the main instance of the wareHousePicking class.
   */
  public Picker(String name, WarehouseManager warehouseManager, WarehousePicking wareHousePicking) {
    super(name, warehouseManager);
    this.warehouseManager = warehouseManager;
    this.warehousePicking = wareHousePicking;
    nextLocation = 0;
    this.setJobTitle("Picker");
  }


  public boolean isProductDamaged(Fascia fascia) {
    return fascia.isDamaged();
  }



  /**
   * From the current working order list
   * 
   * @return its corresponding SKU number in a well-ordered list Even index for front SKU and odd
   *         index for rear SKU.
   */
  public List<Integer> getSkuFromWorkingOrderList() {

    List<Integer> listOfSku = new LinkedList<>();
    Order[] workingOrders = this.getCurrentWorkingOrderList().getAllOrders();
    for (Order order : workingOrders) {
      Integer frontsku = Integer.parseInt(order.getFrontSku().toString());
      Integer rearsku = Integer.parseInt(order.getRearSku().toString());
      listOfSku.add(frontsku);
      listOfSku.add(rearsku);
    }
    return listOfSku;
  }

  /**
   * Take a list of SKU numbers, look at the traversal table to find the corresponding location. If
   * there exists optimized path, take the optimized path. Otherwise, take Fascia one by one
   * according to the order of incoming order.
   * 
   * @param listOfSku The list of SKU objects that the Picker is going to pick
   * @return list of locations corresponding to SKU number
   */
  public List<String> findRoute(List<Integer> listOfSku) {

    List<String> listOfLocation = new LinkedList<>();

    if (WarehousePicking.optimize(listOfSku) == null) {

      HashMap<Integer, String> traversalTable = warehousePicking.getTraversalTable();

      for (int key : listOfSku) {
        String location = (String) traversalTable.get(key) + ", " + key;
        listOfLocation.add(location);
      }
    } else {
      this.listOfLocations = WarehousePicking.optimize(listOfSku);
      return WarehousePicking.optimize(listOfSku);
    }
    this.listOfLocations = listOfLocation;
    return listOfLocation;
  }



  /**
   * Retrieves an item from ItemStorage and places it on the worker's forklift.
   * 
   * @param location: the location of the Fascia in the warehouse.
   */
  // void pickProduct() {
  public void Working() {


    if (this.getCurrentWorkingOrderList() != null) {
      if (nextLocation < 8) {
      String location = this.getNextLocation();
      String[] position = location.split(",");
      int aisle = Integer.parseInt(position[1]);
      int rackLocation = Integer.parseInt(position[2]);
      int rackLevel = Integer.parseInt(position[3]);
      boolean donePicking = false;
      while (!donePicking) {
        Fascia fascia =
            this.warehouseManager.getProduct(position[0], aisle, rackLocation, rackLevel);
        if (!isProductDamaged(fascia)) {
          forkliftContents.add(fascia);
          String checkSkuNum = position[4].replaceAll(" ", "");
          if (!(scanSingleProduct(fascia, checkSkuNum))) {
            warehouseManager.getLogger().log(Level.INFO,
                "Accidentally picked the wrong item, the Sku number should be " + position[4]);
            forkliftContents.pollLast();
          } else {
            warehouseManager.getLogger().log(Level.INFO,
                "Picker " + this.getName() + " picked Fascia: "
                    + forkliftContents.getLast().getColour() + ","
                    + forkliftContents.peek().getModel() + "("
                    + forkliftContents.getLast().getSku().isFront() + ")" + " at location: "
                    + location);

            break;
          }
        }
        System.out.println(nextLocation);
      }
      }
    } else {
      warehouseManager.getLogger().log(Level.INFO, "No Orders to Pick");
    }
  }



  public void getNextWorkingOrder() {
    boolean unassignedOrders = false;
    // if (getCurrentWorkingOrderList() != null) {
    // unassignedOrders = true;
    // }
    if (isAvailable()) {
      for (int i = 0; i < this.warehouseManager.getOrderList().size(); i++) {
        if (this.warehouseManager.getOrderList().get(i).getProcessingProgress()
            .equals("Not Picked")) {
          this.setCurrentWorkingOrderList(this.warehouseManager.getOrderList().get(i));
          unassignedOrders = true;
          break;
        }
      }
      if (!unassignedOrders) {
        warehouseManager.getLogger().log(Level.INFO,
            "There is no order for " + this.getName() + " to pick.");
      } else {
        this.getCurrentWorkingOrderList().setProcessingProgress("Picking");
        warehouseManager.getLogger().log(Level.INFO,
            "Picker " + this.getName() + " is ready to work.");

        List<Integer> skuNumbers = this.getSkuFromWorkingOrderList();
        this.findRoute(skuNumbers);
      }

    } else {
      warehouseManager.getLogger().log(Level.INFO,
          getJobTitle() + " " + getName() + ": ' I'm busy right now; not ready for new thing ' ");
    }


  }



  /**
   * Gets the location of the next item in the Picker's list of location.
   * 
   * @return a String representation of the item's location.
   */
  String getNextLocation() {
    String currentLocation = listOfLocations.get(nextLocation);
    nextLocation++;
    return currentLocation;

  }

  /**
   * Adds the current Picker's entire forkLiftContents to the sequencingManager's StagingArea and
   * clears the contents of forkliftContents.
   */
  public void preparingProductForNextStage() {


    if (this.nextLocation > 7) {
      Fascia[] tempFascia = new Fascia[fasciaPerOrder];
      int tempFasciaSize = forkliftContents.size();
      for (int i = 0; i < tempFasciaSize; i++) {
        tempFascia[i] = forkliftContents.pollFirst();
      }
      this.getWarehouseManager().getStagingArea().add(tempFascia);


      this.getCurrentWorkingOrderList().setProcessingProgress("Done Picking");
      this.setCurrentWorkingOrderList(null);
      this.nextLocation = 0;
      warehouseManager.getLogger().log(Level.INFO,
          "Picker " + getName() + " has gone to Marshaling Area.");
    } else {
      warehouseManager.getLogger().log(Level.INFO,
          "Not enough product for " + getName() + " to bring to Marshaling");
    }
  }

  public void emergencyCase(OrdersPerProcess ordersPerProcess, int positionInSequencedFasciaList) {
    this.setCurrentWorkingOrderList(ordersPerProcess);
    this.getNextWorkingOrder();
    while (this.nextLocation < maxLocation) {
      this.Working();
    }
    this.preparingProductForNextStage();
    warehouseManager.emergencyCase(ordersPerProcess, positionInSequencedFasciaList);

  }



}

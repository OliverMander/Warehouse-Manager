import java.util.logging.Level;

/**
 * Performs final check of item ordering on Pallets and loads the Truck.
 */
public class Loader extends Worker {

  private Truck truckToLoad;

  private int fasciasPerOrder;
  private final int fasciaPerPallet = 4;
  /**
   * The number of pallets that Sequencer sequences too.
   */
  private final int numOfPallets = 2;
  /**
   * The array index of the pallet that represents the front Fasica.
   */
  private final int frontPalletIndex = 0;
  /**
   * The array index of the pallet that represents the rear Fascia.
   */
  private final int rearPalletIndex = 1;

  /**
   * The constructor for Loader.
   * 
   * @param name the Loader's name.
   * @param truck the Truck that this Loader is currently loading.
   */
  public Loader(String name, WarehouseManager warehouseManager) {
    super(name, warehouseManager);
    setJobTitle("Loader");
  }

  /**
   * Getter for truckToLoad.
   * 
   * @return The Truck that this Loader is currently loading.
   */
  public Truck getTruckToLoad() {
    return getWarehouseManager().getAvailableTruck();
  }

  /**
   * Setter for truckToLoad.
   * 
   * @param tempTruck The Truck that this Loader is currently loading.
   */
  public void setTruckToLoad(Truck tempTruck) {
    truckToLoad = tempTruck;
  }

  /**
   * Loads a Truck and returns the SKU numbers associated with the Fascia that its loading.
   * 
   * @param fascia the Fascia that will be loaded onto the Truck.
   * @param order the ProcessingOrder that the Fascia will be compared against to ensure its
   *        correct.
   * @return SKU[], the SKU numbers of the Fascia that have been loaded on the Truck.
   */
  public Sku[] loadTruck(Fascia[][] fascia, OrdersPerProcess order) {
    int currentOrderId = order.getProcessingId();
    Sku[] skus = null;
    if (truckToLoad == null || truckToLoad.getIsFull()) {
      setTruckToLoad(getTruckToLoad());
    }



    if (currentOrderId == truckToLoad.getNextOrderToLoad()) {
     
      Pallet readyToLoadPallet = placingItemintoPallet();
      skus = getScannedNumber();
      truckToLoad.addFasciaToTruck(readyToLoadPallet);
      truckToLoad.incrementNextOrder();
      getWarehouseManager().getLogger().log(Level.INFO, "Loader " + this.getName()
          + " has just loaded order with id: " + order.getProcessingId() + ".");
      order.setProcessingProgress("Loaded");

    } else {
      getWarehouseManager().getLogger().log(Level.INFO,
          this.getName() + " is waiting for ProcessingOrder: " + truckToLoad.getNextOrderToLoad()
              + " before" + " starting to load.");
    }
    return skus;
  }



  public Fascia[][] getNextWorkingProduct() {
    
    if (this.getWarehouseManager().getSequencedFascia().size() > 0 && this.getCurrentWorkingProductList() == null) {
      Fascia[][] tempFascia = new Fascia[fasciaPerPallet][numOfPallets];
      Pallet tempPallet = this.getWarehouseManager().getSequencedFascia().get(0);
      Fascia[] frontFascia = tempPallet.getFrontFascias();
      Fascia[] rearFascia = tempPallet.getRearFascias();
      for (int i = 0; i < frontFascia.length; i++) {
        tempPallet.getContent();
        tempFascia[i][0] = frontFascia[i];
        
      }
      for (int i = 0; i < rearFascia.length; i++) {

        tempFascia[i][1] = rearFascia[i];
        
      }
      setCurrentWorkingProductList(tempFascia);
      this.getWarehouseManager().getSequencedFascia().remove(0);
      return tempFascia;
    }
    
    return this.getCurrentWorkingProductList();
  }



  /**
   * Returns the next ProcessingOrder in orderList which is scheduled to be Loaded unto a Truck.
   * 
   * @return ProcessingOrder, the ProcessingOrder with ProcessingProgress equal to "Done
   *         Sequencing".
   */
  public void getNextWorkingOrder() {
    if (isAvailable()) {
      for (int i = 0; i < this.getWarehouseManager().getOrderList().size(); i++) {
        if (this.getWarehouseManager().getOrderList().get(i).getProcessingProgress()
            .equals("Done Sequencing")) {
          setCurrentWorkingOrderList(this.getWarehouseManager().getOrderList().get(i));
           break;

        }
      }
      if (getCurrentWorkingOrderList() != null) {
        getWarehouseManager().getLogger().log(Level.INFO, getJobTitle() + " " + getName() + " ready to loading");
      } else {
        getWarehouseManager().getLogger().log(Level.INFO, getJobTitle() + " " + getName() + " is available but there is no sequenced items to load right now");
      }
      
      
    } else {
      getWarehouseManager().getLogger().log(Level.INFO, getJobTitle() + " " + getName() + ": I'm too busy; not ready");
    }

  }







  public void Working() {
    
    if (getCurrentWorkingOrderList() != null) {
    Fascia[][] tempFascia = getNextWorkingProduct();
    OrdersPerProcess order = getCurrentWorkingOrderList();
    if (tempFascia != null) {
      if (!getTruckToLoad().getIsFull()) {
        loadTruck(tempFascia, order);
      } else {
        Truck tempTruck = new Truck();
        getWarehouseManager().addTruck(tempTruck);
        setTruckToLoad(tempTruck);
        loadTruck(tempFascia, order);
      }
      setCurrentWorkingProductList(null);
      setCurrentWorkingOrderList(null);
      
    } else {
      getWarehouseManager().getLogger().log(Level.INFO, "There was no Fascia to load onto a Truck");
    }
   } else {
     getWarehouseManager().getLogger().log(Level.INFO, getJobTitle() + " " + getName() + " is not ready yet");
   }

  }

  public void preparingProductForNextStage() {
      this.setCurrentWorkingProductList(null);
      this.setCurrentWorkingOrderList(null);
    
  }
    

  @Override
  public void emergencyCase(OrdersPerProcess ordersPerProcess, int ProductIndex) {
    boolean emergency = false;
    
    
    
  }

  

}

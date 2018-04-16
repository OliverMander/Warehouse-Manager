
import java.util.logging.Level;

/**
 * The abstract Worker class, gives all workers a name.
 */
public abstract class Worker {

  private String name;



  @SuppressWarnings("unused")
  private boolean isAvailable;


  private Fascia[][] currentWorkingProductList;

  private OrdersPerProcess currentWorkingOrderList;

  private Pallet frontPallet;

  private Pallet rearPallet;

  private String[] currentWorkingLocation = new String[4];;

  public String[] getCurrentWorkingLocation() {
    return currentWorkingLocation;
  }

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



  public Fascia[][] getCurrentWorkingProductList() {
    return currentWorkingProductList;
  }

  public void setCurrentWorkingProductList(Fascia[][] currentWorkingProductList) {
    this.currentWorkingProductList = currentWorkingProductList;
  }

  private String jobTitle;

  private WarehouseManager warehouseManager;

  public Worker(String name, WarehouseManager warehouseManager) {
    this.name = name;
    this.isAvailable = true;
    this.setWarehouseManager(warehouseManager);

  }

  public String getName() {
    return name;
  }



  public boolean isAvailable() {
    return (this.currentWorkingOrderList == null);
  }

  public void setAvailable(boolean isAvailable) {
    this.isAvailable = isAvailable;
  }


  public OrdersPerProcess getCurrentWorkingOrderList() {
    return currentWorkingOrderList;
  }

  public void setCurrentWorkingOrderList(OrdersPerProcess currentWorkingOrderList) {
    this.currentWorkingOrderList = currentWorkingOrderList;
  }

  public String getJobTitle() {
    return jobTitle;
  }

  public void setJobTitle(String jobTitle) {
    this.jobTitle = jobTitle;
  }

  

  public abstract void preparingProductForNextStage();

  public abstract void Working();

  public abstract void getNextWorkingOrder();
  
  public abstract void emergencyCase(OrdersPerProcess ordersPerProcess, int ProductIndex);
  
  /**
   * Check if items at the current location needs resupply
   * 
   * @param zoneIdentifier : Zone "A"; Zone "B"
   * @param aisle : int represents aisle
   * @param rackLocation : int represents rack
   * @param rackLevel : int represents level of a rack
   * @return true when the remaining number of item is less than five.
   */
  public boolean needsResupply(String zoneIdentifier, int aisle, int rackLocation, int rackLevel) {
    return this.warehouseManager.getItemStorage().needsResupply(zoneIdentifier, aisle, rackLocation,
        rackLevel);
  }
  

  public void setCurrentWorkingLocation(String zone, String aisle, String rack, String level) {
    this.currentWorkingLocation[0] = zone;
    this.currentWorkingLocation[1] = aisle;
    this.currentWorkingLocation[2] = rack;
    this.currentWorkingLocation[3] = level;
  }



  /**
   * Scans the Fascia that a worker is currently working on and returns their SKUs.
   * 
   * @param fascia The Fasica that are going to be scanned.
   */
  public Sku[] getScannedNumber() {

    if (currentWorkingProductList != null) {
      Fascia[][] fascia = currentWorkingProductList;

      Sku[] tempSkus = new Sku[fascia.length * fascia[0].length];

      for (int i = 0; i < fascia[0].length; i++) {
        for (int j = 0; j < fascia.length; j++) {

          tempSkus[i + j] = fascia[j][i].getSku();
          warehouseManager.getLogger().log(Level.INFO,
              getJobTitle() + " " + getName() + " scans the product with sku number " + tempSkus[i + j]);
        }
      }
      return tempSkus;
    } else {
      warehouseManager.getLogger().log(Level.INFO, "No Product for " + getName() + " to scan");
      return null;
    }
  }
  


  public boolean scanSingleProduct(Fascia fascia, String skuNum) {
    Sku tempSkus = new Sku(fascia.getSku().getSku());
    warehouseManager.getLogger().log(Level.INFO,
        getJobTitle() + " " + getName() + " scans " + tempSkus);
    return (tempSkus.getSku().equals(skuNum)); 
  }
  

  /**
   * calling worker to scanning all sequenced products.
   * If one is not matches; throw this one and calling a picker to picker new one.
   */
  public void rescans() {
    boolean repick = false;
    if (currentWorkingProductList != null) {
      warehouseManager.getLogger().log(Level.INFO, "too busy, not available for scanning");
    } else if (getWarehouseManager().getSequencedFascia().size() == 0) {
      warehouseManager.getLogger().log(Level.INFO,
          "No Sequenced Product for " + getJobTitle() + " " + getName() + " to scan");
    } else {
      for (int j=0; j < warehouseManager.getSequencedFascia().size(); j++) {
        Pallet pallet = warehouseManager.getSequencedFascia().get(j);
        for (int i = 0; i < fasciaPerPallet; i++) {          
          if (! (pallet.getFrontFascias()[i].getSku().getSku().equals(pallet.getCheckOrder()
              .getOrder(i).getFrontSku().getSku())
              || pallet.getFrontFascias()[i].getSku().getSku().equals(pallet.getCheckOrder()
                  .getOrder(i).getRearSku().getSku()))) {    
            // call a picker to re-picke; 
            OrdersPerProcess ordersPerProcess = pallet.getCheckOrder();
            int processingId = ordersPerProcess.getProcessingId();
            int positionInSequencedFasciaList = j;
            this.emergencyCase(ordersPerProcess, positionInSequencedFasciaList);
            ordersPerProcess.setProcessingProgress("Not Picked");
            getWarehouseManager().getLogger().log(Level.INFO,
                "Found an unmatched Product of " + ordersPerProcess.getProcessingId() + " at position "
                    + positionInSequencedFasciaList + " of sequenced Fascia List" + ", Calling a picker to repick");
            warehouseManager.emergencyCase(ordersPerProcess, positionInSequencedFasciaList);
            warehouseManager.getSequencedFascia().remove(j);
            repick = true;
            break;
          }
        }

      }
      if (! repick) {
        getWarehouseManager().getLogger().log(Level.INFO, jobTitle + " " + name + ": All Products are well sorted; no need to re-pick");
      }
    }
  }
  
  public Pallet placingItemintoPallet() {
    if (getCurrentWorkingProductList() != null) {
      Fascia[] frontFascia = new Fascia[fasciaPerPallet];

      for (int i = 0; i < getCurrentWorkingProductList().length; i++) {
        frontFascia[i] = getCurrentWorkingProductList()[i][0];
      }
      Fascia[] rearFascia = new Fascia[fasciaPerPallet];
      for (int i = 0; i < getCurrentWorkingProductList().length; i++) {
        rearFascia[i] = getCurrentWorkingProductList()[i][1];
      }

      Pallet donePallet = new Pallet(frontFascia, rearFascia, getCurrentWorkingOrderList());
      donePallet.composePallet();
      
      return donePallet;
    } else {
      return null;
    }

  }
  




  public WarehouseManager getWarehouseManager() {
    return warehouseManager;
  }

  public void setWarehouseManager(WarehouseManager warehouseManager) {
    this.warehouseManager = warehouseManager;
  }

  public Pallet getRearPallet() {
    return rearPallet;
  }

  public void setRearPallet(Pallet rearPallet) {
    this.rearPallet = rearPallet;
  }



  public Pallet getFrontPallet() {
    return frontPallet;
  }

  public void setFrontPallet(Pallet frontPallet) {
    this.frontPallet = frontPallet;
  }



}

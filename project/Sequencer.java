import java.util.logging.Level;

/**
 * Arranges items on delivery pallets and ensures proper item order.
 */
public class Sequencer extends Worker {

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
   * Constructor for the Sequencer class.
   * 
   * @param name the name of the sequencer
   */
  public Sequencer(String name, WarehouseManager warehouseManager) {
    super(name, warehouseManager);
    setJobTitle("Sequencer");
  }


  /**
   * Gets the next Array of Fascia from the staging area and removes them from the staging area.
   */
  public Fascia[] getNextWorkingProduct() {
    if (this.getWarehouseManager().getStagingArea().size() > 0) {
      Fascia[] tempFascia = new Fascia[fasciaPerPallet];
      tempFascia = this.getWarehouseManager().getStagingArea().get(0);
      this.getWarehouseManager().getStagingArea().remove(0);
      return tempFascia;
    } else {
      getWarehouseManager().getLogger().log(Level.INFO,
          "There is no products to sequence," + " nothing was sent to a Sequencer.");
      return null;
    }
  }



  /**
   * Sets in motion the Sequencing of Fascia based on the orders.
   * 
   * @param order The Orders that the Fascia is sequenced according too.
   * @return Fascia[][] The sequenced array of Fascia if the order is correct, returns null if the
   *         Orders were incorrect or if there is no available Sequencers.
   */
  public void Working() {
    
    if (getCurrentWorkingOrderList() != null) {
    OrdersPerProcess order = getCurrentWorkingOrderList();
    Fascia[] fascia = this.getNextWorkingProduct();
    Fascia[][] sequencedFascia = null;
    
    if (fascia != null) {

      for (int i = 0; i < fascia.length; i++) {
        
        if (fascia[i] == null) {
          getWarehouseManager().getLogger().log(Level.INFO,
              "The order with id: " + order.getProcessingId()
                  + " did not have the correct number of Fascia, "
                  + "discared the Fascia and sent for new ones.");
          order.setProcessingProgress("Not Picked");
          

        }
      }
      sequencedFascia = sequenceFascia(fascia, order);
      this.setCurrentWorkingProductList(sequencedFascia);


      if (visuallyInspectFascia(sequencedFascia) == false) {
        discardFascia(sequencedFascia);
        
      }

      Sku[] skuNumbers = getScannedNumber();

      for (int i = 0; i < sequencedFascia.length; i++) {
        if (skuNumbers[i] != order.getOrder(i).getFrontSku()) {
          discardFascia(sequencedFascia);
          
        }
      }
      for (int i = 0; i < sequencedFascia.length; i++) {
        if (skuNumbers[i + sequencedFascia.length] != order.getOrder(i).getRearSku()) {
          discardFascia(sequencedFascia);
          
        }
      }
    }

    if (sequencedFascia != null) {
      order.setProcessingProgress("Done Sequencing");
      getWarehouseManager().getLogger().log(Level.INFO, "Sequencer " + getName()
          + " has just sequenced an order with id: " + order.getProcessingId() + ".");

    } else {
      if (order != null) {
        order.setProcessingProgress("Not Picked");
        getWarehouseManager().getLogger().log(Level.INFO,
            "Sequencer " + getName() + " had a problem with an order with id: "
                + order.getProcessingId() + " and has sent for new Fascia.");
        
        emergencyCase(order, getWarehouseManager().getSequencedFascia().size() - 1);
      }
    }
   } else {
     getWarehouseManager().getLogger().log(Level.INFO, "I am not ready for sequncing");
   }
  }



  /**
   * Sequences and sorts the Fascia into the right order as determined by the order in which the
   * eight Orders for the Fascia came in.
   *
   * @param fascia The single dimensional array of Fascia that need to be sequenced.
   * @param order The single dimensional array of Orders in the order they were made, sequencing
   *        happens according to the order of these Orders.
   * @return Returns a two dimensional array of Fascia which represents the two different pallets of
   *         sequenced front and rear Fascias.
   */
  public Fascia[][] sequenceFascia(Fascia[] fascia, OrdersPerProcess order) {
    Fascia[][] sequencedFascia = new Fascia[fasciaPerPallet][numOfPallets];
    for (int i = 0; i < order.getLength(); i++) {
 
      Order currentOrder = order.getOrder(i);
      Sku currentFrontSku = currentOrder.getFrontSku();
      Sku currentRearSku = currentOrder.getRearSku();

      for (int j = 0; j < fascia.length; j++) {
        if (currentFrontSku.equals(fascia[j].getSku())) {
          sequencedFascia[i][frontPalletIndex] = fascia[j];
        }
        if (currentRearSku.equals(fascia[j].getSku())) {
          sequencedFascia[i][rearPalletIndex] = fascia[j];
        }
      }
    }


    return sequencedFascia;
  }

  /**
   * Visually inspect the Fascia to make sure they are in the right order.
   * 
   * @param fascia A two dimensional array of sequenced Fascia to be visually inspected.
   * @return boolean, returns true if the two pallets of Fascia pass visual inspection (they are
   *         both the same colour), otherwise returns false.
   */

  public boolean visuallyInspectFascia(Fascia[][] fascia) {

    for (int i = 0; i < fasciaPerPallet; i++) {
      if (!fascia[i][frontPalletIndex].getColour().equals(fascia[i][rearPalletIndex].getColour())) {
        return false;
      }
    }
    return true;
  }


  /**
   * Discards the current set of Fascia, must be because one of the pickers picked the wrong Fascia.
   * 
   * @param fascia The array of Fascia that need to be discarded.
   */
  public void discardFascia(Fascia[][] fascia) {
    fascia = null;
  }



  /**
   * Returns the next ProcessingOrder in orderList which is scheduled to be Sequenced.
   * 
   * @return ProcessingOrder, the ProcessingOrder with ProcessingProgress equal to "Done Picking"
   */
  public void getNextWorkingOrder() {
    if (isAvailable()) {
      for (int i = 0; i < this.getWarehouseManager().getOrderList().size(); i++) {
        if (this.getWarehouseManager().getOrderList().get(i).getProcessingProgress()
            .equals("Done Picking")) {
          setCurrentWorkingOrderList(this.getWarehouseManager().getOrderList().get(i));
          break;
        }
      }
      if (getCurrentWorkingOrderList() != null) {
        getWarehouseManager().getLogger().log(Level.INFO, getJobTitle() + " " + getName() + " ready to sequencing");
      } else {
        getWarehouseManager().getLogger().log(Level.INFO, getJobTitle() + " " + getName() + " is available but there is nothing to sequence right now");
      }
      
      
    } else {
      getWarehouseManager().getLogger().log(Level.INFO, getJobTitle() + " " + getName() + ": I'm too busy; not ready");
    }

  }
  


  public void preparingProductForNextStage() {
    Pallet sequencedPallet = placingItemintoPallet();
    if (sequencedPallet!= null) {
      

      this.getWarehouseManager().getSequencedFascia().add(sequencedPallet);
      getWarehouseManager().getLogger().log(Level.INFO, "Sequencing finished; it's time to load");
    }
    
    this.setCurrentWorkingProductList(null);
    this.setCurrentWorkingOrderList(null);
  }


  @Override
  public void emergencyCase(OrdersPerProcess ordersPerProcess, int ProductIndex) {
    setCurrentWorkingOrderList(ordersPerProcess);
    this.Working();
    Pallet sequencedPallet = placingItemintoPallet();
    this.getWarehouseManager().getSequencedFascia().add(ProductIndex, sequencedPallet);
    getWarehouseManager().emergencyCase(ordersPerProcess, ProductIndex);
    

  }



}


import java.util.ArrayList;
import java.util.HashMap;

/**
 * Logs and fulfills item re-supplies.
 */
public class ReserveRoom {
  /**
   * An ArrayList storing a log of all filled re-supply requests.
   */
  private ArrayList<Integer> resupplyLog;
  
  private ArrayList<Fascia> reserveContents;



  /**
   * A lookup table mapping car colour and model to a list of SKUs representing
   * the front and back Fascia, respectively.
   */
  HashMap<String[], Sku[]> translationTable;
  //TODO: Create resupply object that tracks SKU and amount resupplied.

  /**
   * Notifies the ItemStorage class of whether a resupply request will be filled.
   * @return true if the resupply request should be filled, false otherwise.
   */
  boolean fillResupplyRequest() {
    //TODO: Implement fillResupplyRequest
    return true;
  }

  /**
   * Begins the process of unloading the incoming fascia,
   * checking their validity, and adding them to the reserve room.
   * @param receivedFascia the fascia being unloaded from the incoming truck.
   */
  void unloadAndResupply(Fascia[][] receivedFascia) {
    if (checkColourAndModel(receivedFascia)) {
      for (int i = 0; i < receivedFascia.length;i++) {
        for (Fascia fascia : receivedFascia[i]) {
          this.reserveContents.add(fascia);
        }
      }  
    }
  }
  
  public ArrayList<Fascia> getReserveContents() {
    return reserveContents;
  }
  
  public ArrayList<Integer> getResupplyLog() {
    return resupplyLog;
  }

  /**
   * update the reserve supply log by recording the new incoming Fascia SKU number. 
   * @param receivedFascia : incoming fascia unloaded from truck.
   */
  public void updateResupplyLog(Fascia[][] receivedFascia) {
    for (int i = 0; i < receivedFascia.length;i++) {
      for (Fascia fascia : receivedFascia[i]) {
        this.resupplyLog.add(Integer.parseInt(fascia.getSku().getSku()));
      }
    }
  }

  public HashMap<String[], Sku[]> getTranslationTable() {
    return translationTable;
  }


  /**
   * Checks the colour and model of incoming Fascia
   * to ensure they correspond with their associated SKUs.
   * @param receivedFascia the fascia being unloaded from the incoming truck
   * @return true if the colour and model match the item SKUs, and false otherwise.
   */
  boolean checkColourAndModel(Fascia[][] receivedFascia) {
    for (int i = 0 ; i < receivedFascia.length; i++) {
      for (Fascia fascia: receivedFascia[i]) {
        String[] keyString = {fascia.getColour(), fascia.getModel()};
        if (fascia.isFront) {
          if (fascia.getSku() == translationTable.get(keyString)[0]) {
            return true;
          }
        } else {
          if (fascia.getSku() == translationTable.get(keyString)[1]) {
            return true;
          }
        }
      }
    }
    return false; 
  }

  /**
   * Constructor for the Reserve room class.
   * @param translationTable a copy of the translation table generated
   *                         in the program's "main" method.
   */
  public ReserveRoom(HashMap<String[], Sku[]> translationTable) {
    this.translationTable = translationTable;
    resupplyLog = new ArrayList<>();
  }
}

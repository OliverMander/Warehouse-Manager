import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * Manages the storage and retrieval of items in the warehouse.
 */
public class ItemStorage {

  /**
   * A four dimensional array containing all Shelfs managed by ItemStorage.
   */
  Shelf[][][][] storage;

  /**
   * The number of zones in the warehouse.
   */
  private final int numOfZones = 2;

  /**
   * The number of aisles in each zone.
   */
  private final int numOfAisles = 2;

  /**
   * The number of racks on each pick face.
   */
  private final int numOfRacks = 3;

  /**
   * The number of rack levels on each rack.
   */
  private final int heightOfRack = 4;

  private WarehouseManager warehouseManager;

  /**
   * Constructor for the ItemStorage class.
   * 
   * @param warehouseManager TAn instance of the warehouseManager class.
   * @param warehousePicking An instance of the warehousePicking class.
   * @throws FileNotFoundException If the file path cannot be found throws exception.
   */
  public ItemStorage(WarehousePicking warehousePicking, WarehouseManager warehouseManager)
      throws FileNotFoundException {
    storage = new Shelf[numOfZones][numOfAisles][numOfRacks][heightOfRack];
    this.warehouseManager = warehouseManager;
    populateShelfsWithFascia(warehousePicking, warehouseManager);

  }

  public WarehouseManager getWarehouseManager() {
    return warehouseManager;
  }

  /**
   * Populates all Shelfs in ItemStorage with the correct Fascia.
   *
   * @param warehousePicking The instance of warehousePicking.
   * @param warehouseManager The instance of warehouseManager.
   * @throws FileNotFoundException if the
   */
  public void populateShelfsWithFascia(WarehousePicking warehousePicking,
      WarehouseManager warehouseManager) throws FileNotFoundException {
    HashMap<String, Integer> rackToSku = warehousePicking.getRackToSku();
    HashMap<Sku, String> skuToCarOrder = warehouseManager.getSkuToCarOrder();

    boolean isFrontFascia;
    String translatedZone;

    for (int zone = 0; zone < numOfZones; zone++) {
      for (int aisle = 0; aisle < numOfAisles; aisle++) {
        for (int rack = 0; rack < numOfRacks; rack++) {
          for (int height = 0; height < heightOfRack; height++) {

            // Creates the SKU that corresponds to the location in the warehouse.
            if (zone == 0) {
              translatedZone = "A";
            } else {
              translatedZone = "B";
            }

            String key = translatedZone + "," + aisle + "," + rack + "," + height;
            Sku tempSku = new Sku(String.valueOf(rackToSku.get(key)));


            // System.out.println(key);
            // Checks if the SKU corresponds to a front Fascia or a rear Fascia.
            if (Integer.parseInt(tempSku.getSku()) % 2 == 0) {
              isFrontFascia = false;
            } else {
              isFrontFascia = true;
            }

            // Get the details about the car from the SKU.
            String tempOrder = skuToCarOrder.get(tempSku);
            String[] tempOrderComponents = tempOrder.split("(?=\\p{Upper})");
            String carColour = tempOrderComponents[0];
            String carModel = "";

            for (int i = 1; i < tempOrderComponents.length; i++) {
              carModel += tempOrderComponents[i];
            }

            // Builds Storage from the tempFascia and TempShelf.
            Fascia tempFascia = new Fascia(tempSku, isFrontFascia, carColour, carModel);
            Shelf tempShelf = new Shelf(tempSku, tempFascia, 30);
            // TODO: Change it so the amount remaining on the Shelf is variable
            // as stated by the file, read handout.

            storage[zone][aisle][rack][height] = tempShelf;
          }
        }
      }
    }
    File translationInput = new File("initial.csv");
    Scanner input = new Scanner(translationInput);

    while (input.hasNextLine()) {
      String currentLine = input.nextLine();
      String[] currentLineComponents = currentLine.split(",");
      int zone = translateZone(currentLineComponents[0]);
      int aisle = Integer.parseInt(currentLineComponents[1]);
      int rack = Integer.parseInt(currentLineComponents[2]);
      int level = Integer.parseInt(currentLineComponents[3]);
      int amount = Integer.parseInt(currentLineComponents[4]);

      storage[zone][aisle][rack][level].setRemainingFascia(amount);
    }
    input.close();
  }

  /**
   * Determines if a resupply request needs to be filled for the shelf at the given location.
   * 
   * @param zoneIdentifier a string representing the zone in which the Fascia is found.
   * @param aisle an integer representing the aisle in which the Fascia is found.
   * @param rackLocation an integer representing the rackLocation at which the Fascia is found.
   * @param rackLevel an integer representing the rack level on which at Fascia is found.
   * @return true if the Shelf needs to be resupplied, false otherwise.
   */

  boolean needsResupply(String zoneIdentifier, int aisle, int rackLocation, int rackLevel) {
    int translatedZone = translateZone(zoneIdentifier);

    return storage[translatedZone][aisle][rackLocation][rackLevel].getRemainingFascia() <= 5;
  }

  /**
   * Generates an integer representative of the given zone.
   *
   * @param zoneIdentifier a string representation of a zone in the warehouse.
   * @return and integer representation of the given zone.
   */
  private int translateZone(String zoneIdentifier) {
    if (zoneIdentifier.equals("A")) {
      return 0;
    }
    return 1;
  }

  /**
   * Gets a Fascia from the storage and returns it. Also checks if the shelf needs a resupply.
   *
   * @param zoneIdentifier a string representing the zone in which the Fascia is found.
   * @param aisle an integer representing the aisle in which the Fascia is found.
   * @param rackLocation an integer representing the rackLocation at which the Fascia is found.
   * @param rackLevel an integer representing the rack level on which at Fascia is found.
   * @return Returns the Fascia at the location of the four parameters.
   */

  public Fascia requestItem(String zoneIdentifier, int aisle, int rackLocation, int rackLevel) {
    int translatedZone = translateZone(zoneIdentifier);
    if (needsResupply(zoneIdentifier, aisle, rackLocation, rackLevel)) {
      
      warehouseManager.getLogger().log(Level.INFO, "The location " + zoneIdentifier + ", " + aisle
          + ", " + rackLocation + ", " + rackLevel + " requires resupply");
      resupplyItem(zoneIdentifier, aisle, rackLocation, rackLevel);
    }



    return storage[translatedZone][aisle][rackLocation][rackLevel].getFascia();
  }

  /**
   * Triggers a resupply for the Shelf at the given location. Once we did the resupply; we keep the
   * replenish log up-to-date.
   * 
   * @param zoneIdentifierString a string representing the zone in which the Fascia is found.
   * @param aisle an integer representing the aisle in which the Fascia is found.
   * @param rackLocation an integer representing the rackLocation at which the Fascia is found.
   * @param rackLevel an integer representing the rack level on which at Fascia is found.
   */
  public void resupplyItem(String zoneIdentifierString, int aisle, int rackLocation,
      int rackLevel) {
    int zoneIdentifier = translateZone(zoneIdentifierString);
    storage[zoneIdentifier][aisle][rackLocation][rackLevel].doResupply();
    String newFasciaSkuString =
        storage[zoneIdentifier][aisle][rackLocation][rackLevel].getFascia().getSku().getSku();
    int newFasciaSkuNumber = Integer.parseInt(newFasciaSkuString);
    this.getWarehouseManager().updateReplenishLog(newFasciaSkuNumber);
    warehouseManager.getLogger().log(Level.INFO, "Replenishing Products at " + zoneIdentifierString
        + " " + aisle + " " + rackLocation + " " + rackLevel);
  }

  public Shelf[][][][] getStorage() {
    return storage;
  }
}

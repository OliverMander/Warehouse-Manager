
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.Scanner;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;



/**
 * Manages all facets of the warehouse operation.
 */
public class WarehouseManager {


  Scanner input = new Scanner(System.in);


  private ItemStorage itemStorage;

  private ArrayList<OrdersPerProcess> orderList;



  private HashMap<String, ArrayList<Worker>> employeesTable;


  private HashMap<String, Sku[]> translationTable;


  private ArrayList<Pallet> sequencedFascia = new ArrayList<>();



  private HashMap<Sku, String> skuToCarOrder;



  private final int modelPos = 1;


  private final int colourPos = 2;


  private final int fasciaPerPallet = 4;


  private WarehousePicking warehousePicking;



  private ArrayList<Fascia[]> stagingArea = new ArrayList<>();



  private int processingOrderId;

  private ArrayList<Truck> truckList = new ArrayList<>();

  private ArrayList<String> unprocessedOrders = new ArrayList<>();

  private final Logger logger = Logger.getLogger(WarehouseManager.class.getName());

  // private static final Handler consoleHandler = new ConsoleHandler();

  private FileHandler fileHandler;



  /** Record the SKU for the fascias so that system knows what fascias are added into warehouse. */
  private ArrayList<Integer> replenishLog = new ArrayList<>();

  /**
   * Constructor for the WarehouseManager class.
   * 
   * @param args an array of command line arguments populated by file paths to the traversal table,
   *        translation table, and the warehouse event simulation file.
   * @throws FileNotFoundException
   */
  public WarehouseManager(String[] args) throws FileNotFoundException {
    orderList = new ArrayList<OrdersPerProcess>();
    employeesTable = new HashMap<>();

    translationTable = new HashMap<String, Sku[]>();
    skuToCarOrder = new HashMap<Sku, String>();
    warehousePicking = new WarehousePicking();

    logger.setLevel(Level.ALL);
    logger.setLevel(Level.ALL);



    try {
      readTranslationTable("translation.csv");
      warehousePicking.buildTraversalMap("traversal_table.csv");
      itemStorage = new ItemStorage(warehousePicking, this);
      fileHandler = new FileHandler("log.txt");
      logger.addHandler(fileHandler);


    } catch (SecurityException | IOException exception) {
      logger.log(Level.SEVERE, exception.toString(), exception);
      exception.printStackTrace();
    } finally {
      input.close();
    }
  }

  /**
   * getter method for accessing the item storage.
   */
  public ItemStorage getItemStorage() {
    return itemStorage;
  }

  public ArrayList<Integer> getReplenishLog() {
    return replenishLog;
  }

  public Logger getLogger() {
    return logger;
  }

  /**
   * Take the sku number of a fascia product; add it into replenish log.
   * 
   * @param skuNumber : SKU number of a fascia product.
   */
  public void updateReplenishLog(int skuNumber) {
    replenishLog.add(skuNumber);
  }

  public ArrayList<Fascia[]> getStagingArea() {
    return stagingArea;
  }

  public ArrayList<Pallet> getSequencedFascia() {
    return sequencedFascia;
  }

  public void setSequencedFascia(ArrayList<Pallet> sequencedFascia) {
    this.sequencedFascia = sequencedFascia;
  }

  public WarehousePicking getWarehousePicking() {
    return warehousePicking;
  }


  public ArrayList<OrdersPerProcess> getOrderList() {
    return orderList;
  }


  public Worker getAnyAvailableWorker(String jobTitle) {

    for (Worker worker : this.employeesTable.get(jobTitle)) {
      if (worker.isAvailable()) {
        return worker;
      }
    }
    logger.log(Level.INFO, "No Available Worker at This Moment");
    return null;
  }

  public Worker getParticularWorker(String jobTitle, String workerName) {

    if (isWorkerInSystem(jobTitle, workerName)) {
      for (Worker worker : this.employeesTable.get(jobTitle)) {
        if (worker.getName().equals(workerName)) {

          return worker;
        }
      }
    }
    return null;
  }



  /**
   * Getter for TranslationTable, a HashMap that maps CarOrders to SKU numbers.
   * 
   * @return translationTable Maps carColour and carModel to the respective SKUs.
   */
  public HashMap<String, Sku[]> getTranslationTable() {
    return translationTable;
  }

  /**
   * Getter for SKUToCarOrder, a HashMap that maps SKUs to CarOrders.
   * 
   * @return skuToCarOrder Maps SKU numbers to the the carColour and carModel.
   */
  public HashMap<Sku, String> getSkuToCarOrder() {
    return skuToCarOrder;
  }


  /**
   * Requests an item from the warehouse's instance of the ItemStorage class.
   * 
   * @return The fascia requested by the picker.
   */
  public Fascia getProduct(String zoneIdentifier, int aisle, int rackLocation, int rackLevel) {
    return itemStorage.requestItem(zoneIdentifier, aisle, rackLocation, rackLevel);
  }



  /**
   * Reads the translation table from a given file.
   * 
   * @param filePath The path to the file containing the translation table.
   */
  private void readTranslationTable(String filePath) throws FileNotFoundException {

    File translationInput = new File(filePath);
    Scanner input = new Scanner(translationInput);

    while (input.hasNextLine()) {
      String[] mapItem = input.nextLine().split(",");
      String carDescription = mapItem[0] + mapItem[1];
      Sku[] itemSkuList = {new Sku(mapItem[2]), new Sku(mapItem[3])};
      translationTable.put(carDescription, itemSkuList);
      skuToCarOrder.put(itemSkuList[0], carDescription);
      skuToCarOrder.put(itemSkuList[1], carDescription);
    }
    input.close();
  }

  public Worker hireWorker(String jobTitle, String name) {
    if (!isWorkerInSystem(jobTitle, name)) {
      logger.log(Level.INFO, "Hiring a new " + jobTitle + " " + name);
      switch (jobTitle) {
        case "Picker":
          Picker p = new Picker(name, this, warehousePicking);
          this.employeesTable.get(jobTitle).add(p);
          return p;

        case "Sequencer":
          Sequencer s = new Sequencer(name, this);
          this.employeesTable.get(jobTitle).add(s);
          return s;


        case "Loader":
          Loader l = new Loader(name, this);
          this.employeesTable.get(jobTitle).add(l);
          return l;

        case "Replenisher":
          Replenisher r = new Replenisher(name, this);
          this.employeesTable.get(jobTitle).add(r);
          return r;
        
      }
      
      
    } else {
      logger.log(Level.INFO, jobTitle + " " + name + " is in our System");
    }
    return null;
  }

  public boolean isWorkerInSystem(String jobTitle, String name) {
    boolean result = false;
    if (this.employeesTable.get(jobTitle) != null) {
      for (Worker worker : this.employeesTable.get(jobTitle)) {
        if (worker.getName().equals(name)) {
          result = true;
          break;
        }
      }
    }
    return result;
  }

  /**
   * When a sequncer have found an unmatched Items in the well-sequenced list.
   * 
   * @param ordersPerProcess
   * @param processingId
   */
  public void emergencyCase(OrdersPerProcess ordersPerProcess, int positionInSequencedFasciaList) {
    Worker worker = null;
    switch (ordersPerProcess.getProcessingProgress()) {
      case "Not Picked":
        worker = getAnyAvailableWorker("Picker");
        if (worker == null) {
          worker = hireWorker("Picker", "Temporary Picker: Paul");
        }
      case "Done Picking":
        worker = getAnyAvailableWorker("Sequencer");
        if (worker == null) {
          worker = hireWorker("Sequencer","Temporary Sequencer: Paul");
        }
     }
    
    if (ordersPerProcess.getProcessingProgress().equals("Done Sequencing")
        && worker != null) {
      employeesTable.get(worker.getJobTitle()).remove(worker.getName());
    } else {
      worker.emergencyCase(ordersPerProcess,positionInSequencedFasciaList);
    }

  }



  /**
   * Create a ProcessedOrder after four orders have been placed on the fax machine.
   * 
   * @param unprocessedOrders an ArrayList of Orders, will be combined into one unique
   *        ProcessingOrder.
   */
  public void createProcessedOrder(ArrayList<String> unprocessedOrders) {

    Order[] tempOrders = new Order[fasciaPerPallet];
    if (unprocessedOrders.size() == fasciaPerPallet) {
      for (int i = 0; i < tempOrders.length; i++) {
        String carModel = unprocessedOrders.get(i).split(" ")[modelPos];
        String carColour = unprocessedOrders.get(i).split(" ")[colourPos];
        String carDescription = carColour + carModel;
        Sku frontSku = translationTable.get(carDescription)[0];
        Sku rearSku = translationTable.get(carDescription)[1];
        Order order = new Order(carColour, carModel, frontSku, rearSku);
        tempOrders[i] = order;
      }
      unprocessedOrders.clear();
      OrdersPerProcess tempProcessingOrder =
          new OrdersPerProcess(tempOrders, getProcessingOrderId());
      orderList.add(tempProcessingOrder);
      logger.log(Level.INFO, "Created a new ProcessingOrder with unique id: "
          + tempProcessingOrder.getProcessingId() + ".\n " + tempProcessingOrder);
    }
  }

  private int getProcessingOrderId() {

    return processingOrderId++;
  }



  public void windDown() throws IOException {

    String finalStateFileName = "final.csv";
    String ordersFileName = "orders.csv";

    File finalStateFile = new File(finalStateFileName);
    BufferedWriter finalStateWriter = new BufferedWriter(new FileWriter(finalStateFile));

    File orderFile = new File(ordersFileName);
    BufferedWriter orderWriter = new BufferedWriter(new FileWriter(orderFile));



    Shelf[][][][] storage = this.getItemStorage().getStorage();

    int storageSize = 48;
    int zone = 0;
    int aisle = 0;
    int row = 0;
    int height = 0;

    for (int i = 0; i < storageSize; i++) {

      if (storage[zone][aisle][row][height].getRemainingFascia() != 30) {
        finalStateWriter.write(zone + "," + aisle + "," + row + "," + height + ","
            + storage[zone][aisle][row][height].getRemainingFascia() + "\n");
      }
      zone++;
      if (zone % 2 == 0) {
        zone = 0;
        aisle++;
        if (aisle % 2 == 0) {
          aisle = 0;
          row++;
          if (row % 3 == 0) {
            row = 0;
            height++;
            if (height % 4 == 0) {
              height = 0;
            }
          }
        }
      }
    }


    for (int i = 0; i < orderList.size(); i++) {
      if (orderList.get(i).getProcessingProgress() == "Loaded") {
        orderWriter.write(orderList.get(i) + "\n");
      }

    }
    finalStateWriter.close();
    orderWriter.close();
  }



  public HashMap<String, ArrayList<Worker>> getEmployeesTable() {
    return employeesTable;
  }

  public void setEmployeesTable(HashMap<String, ArrayList<Worker>> employeesTable) {
    this.employeesTable = employeesTable;
  }

  public ArrayList<String> getUnprocessedOrders() {
    return unprocessedOrders;
  }

  public void setUnprocessedOrders(ArrayList<String> unprocessedOrders) {
    this.unprocessedOrders = unprocessedOrders;
  }

  public void addTruck(Truck tempTruck) {
    truckList.add(tempTruck);

  }



  /**
   * Gets the next Truck from truckList which has not been filled up yet.
   * 
   * @return Truck, the next Truck that should be filled up.
   */
  public Truck getAvailableTruck() {

    if (truckList.size() != 0) {
      for (int i = 0; i < truckList.size(); i++) {
        if (!truckList.get(i).getIsFull()) {
          return truckList.get(i);
        }
      }
    } else {
      Truck newTruck = new Truck();
      return newTruck;
    }
    return null;
  }


}

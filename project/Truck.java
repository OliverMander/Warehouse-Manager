import java.util.Stack;

/**
 * Truck class, contains Pallets full of Fascia in the order they were ordered in.
 */
public class Truck {

//  /**
//   * The amount of Fascia that can fit horizontally in the Truck.
//   * */
//  private final int truckWidth = 2;
//  /**
//   * The amount of Fascia that can fit lengthwise in the Truck.
//   * */
//  private final int truckLen = 8;
//
//  /**
//   * The amount of Fascia that can fit vertically into the Truck.
//   * */
//  private final int truckHeight = 10;
//
//  /**
//   * The width position where the next order of Fascia will go.
//   * */
//  private int truckWidthPos;
//
//  /**
//   * The length position where the next order of Fascia will go.
//   * */
//  private int truckLenPos;
//
//  /**
//   * The height position where the next order of Fascia will go.
//   * */
//  private int truckHeightPos;

  /**
   * The Id of the next ProcessingOrder that should be placed onto the truck.
   * */
  private int nextOrderToLoad;

  /**
   * Where the Truck actually stores the Fascia.
   * */
  //private Fascia[][][] storage;

  private Stack<Pallet> storage;
  /**
   * Is true if the Truck is at full capacity, otherwise returns false.
   * */
  private boolean isFull;
  private int maxSize;

  /**
   * The constructor for Truck.
   * */
  public Truck() {
//    storage = new Fascia[truckWidth][truckLen][truckHeight];   
//    truckWidthPos = 0;
//    truckLenPos = 0;
//    truckHeightPos = 0;
//    nextOrderToLoad = 0;
    isFull = false;
    storage = new Stack<Pallet>();
    maxSize = 20;
  }


  /**
   * Returns the value of isFull.
   * */
  public boolean getIsFull() {
    return isFull;
  }

  /**
   * Returns the nextOrderToLoad.
   * */
  public int getNextOrderToLoad() {
    return nextOrderToLoad;
  }

  public void incrementNextOrder() {
    nextOrderToLoad++;
  }


  public void addFasciaToTruck(Pallet pallet) {
    if (storage.size() < maxSize) {
      storage.push(pallet);
    } else {
      isFull = true;
    }
  }

//  /**
//   * Loads Fascia onto a Truck. Checks to make sure its the right ProcessingOrder
//   * Or else it doesn't add the Fascia.
//   * 
//   * @param fascia the Fascia that is going to be loaded unto the Truck.
//   * */
//  public void addFasciaToTruck(Fascia[][] fascia) {
//    for (int i = 0; i < fascia[0].length;i++) {
//      for (int j = 0; j < fascia.length; j++) {
//
//        //Adds the Fascia to the truck.
//        storage[truckWidthPos][truckLenPos][truckHeightPos] = fascia[j][i];
//
//        //Finds the location to add the next Fascia
//        truckLenPos++;
//        if (truckLenPos % truckLen == 0) {
//          truckLenPos = 0;
//          truckWidthPos++;
//          if (truckWidthPos % truckWidth == 0) {
//            truckWidthPos = 0;
//            truckHeightPos++;
//            if (truckHeightPos % truckHeight == 0) {
//              isFull = true;
//            }  
//          }
//        }
//      } 
//    }
//  }
}

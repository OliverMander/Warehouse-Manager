import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class WarehousePicking {


  /**
   * A translation table mapping SKU number to appropriate locations.
   */
  private HashMap<Integer, String> traversalTable;
  
  /**
   * A HashMap mapping Shelfs to their associated SKUs.
   */
  private HashMap<String, Integer> rackToSku;
  
  /**
   * Constructor for the WarehousePicking class.
   * Initializes all member fields.
   */
  public WarehousePicking()  {
    traversalTable = new HashMap<>();
    rackToSku = new HashMap<>();
  }
  
  /**
   * Provides external access to the class' traversal table.
   * @return the traversal table associated with the class.
   */
  HashMap<Integer, String> getTraversalTable() {
    return traversalTable;
  }
  
  /**
   * Provides access to the class' rackToSKU map.
   * @return the rackToSKU map associated with the class.
   */
  public HashMap<String, Integer> getRackToSku() {
    return rackToSku;
  }
  
  /**
   * Provides the translation table with a set of key-value combinations.
   * @param traversalTable the traversal table we are assigning to the class' field
   *                       of the same name.
   */
  public void setTraversalTable(HashMap<Integer, String> traversalTable) {
    this.traversalTable = traversalTable;
  }

  /**
   * Reads the traversal table from a given file.
   * Key (integer object) represents SKU number
   * Value (String) contains this SKU locations : zone, aisle, rack-location,level.
   * @param filePath The path to the file containing the traversal table.
   * @throws FileNotFoundException If the file path cannot be found throws exception.
   */
  public void buildTraversalMap(String filePath) throws FileNotFoundException {
    
    File translationInput = new File(filePath);
    Scanner input = new Scanner(translationInput);
    
    while (input.hasNextLine()) {
      String[] mapItem = input.nextLine().split(",");
      Integer skuNumber = Integer.parseInt(mapItem[4]);
      String location = mapItem[0] + "," + mapItem[1] + "," + mapItem[2]
          +  "," + mapItem[3];
      traversalTable.put(skuNumber, location);
      rackToSku.put(location,skuNumber);
    }
    input.close();
  }


  /**
   * Based on the Integer SKUs in List 'skus', return a List of locations,
   * where each location is a String containing 5 pieces of information: the
   * zone character (in the range ['A'..'B']), the aisle number (an integer
   * in the range [0..1]), the rack number (an integer in the range ([0..2]),
   * and the level on the rack (an integer in the range [0..3]), and the SKU
   * number.
   * @param skus the list of SKUs to retrieve.
   * @return the List of locations.
   */
  public static List<String> optimize(List<Integer> skus) {
    return null;
  }
}


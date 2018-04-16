import java.util.HashMap;

/**
 * Subclass of HashMap allowing for the storage and retrieval of SKU data for
 * individual car models and colours.
 */
public class SkuMap extends HashMap<String[], Sku[]> {

  /**
   * Used for serialization.
   */
  private static final long serialVersionUID = 1L;
  
  
  /**
   * Places the given key value combination into the SKU map.
   * @param key the key to which the SKU will be mapped.
   * @param value the list of SKU mapped to the key.
   * @return
   */
  public Sku[] put(String[] key, Sku[] value) {
    return super.put(key, value);
  }
  
  
  /**
   * If applicable, returns the list of SKU corresponding to the given key.
   * @param key the key whose associated SKU are to be returned.
   * @return the list of SKU associated with the key.
   */
  public Sku[] get(String[] key) {
    return super.get(key);
  }
}

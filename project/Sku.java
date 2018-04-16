/**
 * A SKU number associated with a specific product range.
 */
public class Sku {

  private String sku;

  public String toString() {
    return sku;
  }

  /**
   * If the SKU is a front SKU or a rear SKU.
   * 
   * @return String, "Rear SKU" if its a rear Fascia, "Front SKU" if
   *         its a front FKU.
   * */
  public String isFront() {
    if (Integer.parseInt(sku) % 2 == 0) {
      return "Rear SKU";
    }
    return "Front SKU";

  }

  /**
   * Constructor for a Sku.
   * 
   * @param desiredSku The value sku should be initilized to.
   * */
  public Sku(String desiredSku) {
    sku = desiredSku;
  }

  /**
   * Getter for sku.
   * 
   * @return sku the String which represents this Sku.
   * */
  public String getSku() {
    return sku;
  }


  /**
   * Used for find the hash for this Sku.
   * */
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((sku == null) ? 0 : sku.hashCode());
    return result;
  }


  /**
   * Checks to see if the two objects are equal.
   * 
   * @param obj The Object this Sku is being checked against for equality.
   * @return boolean True if the objects are equal, false otherwise.
   * */
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof Sku)) {
      return false;
    }
    Sku other = (Sku) obj;
    if (sku == null) {
      if (other.sku != null) {
        return false;
      }
    } else if (!sku.equals(other.sku)) {
      return false;
    }
    return true;
  }


}

/**
 * Describes an individual Fascia in the warehouse system.
 */
public class Fascia {

  /**
   * The SKU associated with the Fascia.
   */
  private Sku itemSku;

  /**
   * A boolean value that is true when the Fascia is intended for the front of a car, and false
   * otherwise.
   */
  boolean isFront;

  /**
   * a String representation of the car colour associated with the Fascia.
   */
  private String colour;

  /**
   * a String representation of the car model associated with the Fascia.
   */
  private String model;

  /**
   * A boolean indication whether the Fascia is damaged. True if the Fascia is 
   * damaged, false otherwise.
   */
  private boolean isDamaged;

  /**
   * Getter method for the car's SKU.
   * 
   * @return the SKU object associated with the Fascia.
   */
  public Sku getSku() {
    return itemSku;
  }

  /**
   * Getter method for the car's colour.
   * 
   * @return a String representation of the car colour associated with the Fascia.
   */
  String getColour() {
    return colour;
  }

  /**
   * Getter method for the car's model.
   * 
   * @return a String representation of the car model associated with the Fascia.
   */
  String getModel() {
    return model;
  }

  /**
   * Constructor for the Fascia class. Initializes all attributes in the class.
   * 
   * @param tempSku a SKU which will be associated with the Fascia
   * @param isFrontFascia boolean value that is true if the fascia is for. the front of a car, false
   *        otherwise.
   * @param carColour a String representing the car's colour.
   * @param carModel a String representing the car's model.
   */
  public Fascia(Sku tempSku, boolean isFrontFascia, String carColour, String carModel) {
    itemSku = tempSku;
    model = carModel;
    this.isFront = isFrontFascia;
    colour = carColour;
    this.isDamaged = false;
  }

  /**
   * Getter for isDamaged.
   * 
   * @return isDamaged true if the Fascia is damaged, false otherwise.
   * */
  public boolean isDamaged() {
    return isDamaged;
  }

  /**
   * Setter for isDamaged.
   * 
   * @param isDamaged The boolean value for isDamaged, true if this Fascia is damaged
   *        false otherwise.
   * */
  public void setDamaged(boolean isDamaged) {
    this.isDamaged = isDamaged;
  }
  
  public String toString() {
     return colour + " " + model + " " + itemSku;
  }
}

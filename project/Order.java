/**
 * A single Order for a front and back Fascia.
 */
public class Order {
  /**
   * The colour of car associated with the Order.
   */
  private String carColour;

  /**
   * The model of car associated with the Order.
   */
  private String carModel;

  /**
   * The Sku for the car's front bumper.
   */
  private Sku frontSku;

  /**
   * The Sku for the car's rear bumper.
   */
  private Sku rearSku;


  /** Instantiate a order by reading an order file(executed in main program).
   *  The file contain multiple orders; each line per order.
   *  Get the information of color, model.
   *  
   *  @param carColour The colour of the Car.
   *  @param carModel The model of the Car.
   *  @param frontSku The Sku of the front Fascia.
   *  @param rearSku the Sku of the rear Fascia.
   */
  public Order(String carColour, String carModel, Sku frontSku, Sku rearSku) {
    this.carColour = carColour;
    this.carModel = carModel;
    this.frontSku = frontSku;
    this.rearSku = rearSku;
  }

  /**
   * Provides access to the colour of the car associated with the order.
   * @return the car's colour.
   */
  public String getCarColour() {
    return carColour;
  }

  /**
   * Sets the the colour of the car associated with the order.
   * @param carColour the new colour to associate with the car.
   */
  public void setCarColour(String carColour) {
    this.carColour = carColour;
  }

  /**
   * Provides access to the model of the car associated with the order.
   * @return the car's model
   */
  public String getCarModel() {
    return carModel;
  }

  /**
   * Sets the the model of the car associated with the order.
   * @param carModel the new colour to associate with the car.
   */
  public void setCarModel(String carModel) {
    this.carModel = carModel;
  }

  /**
   * Provides access to the front SKU of the car associated with the order.
   * @return the SKU associated with the front bumper
   */
  public Sku getFrontSku() {
    return frontSku;
  }

  /**
   *Provides access to the rear SKU of the car associated with the order.
   * @return the SKU associated with the rear bumper
   */
  public Sku getRearSku() {
    return rearSku;
  }

  /**
   * Provides a String representation of the Order object.
   * @return a String representation of the Order object.
   */
  public String toString() {
    return this.getCarColour() + ", "
        + this.getCarModel() + ", (Front SKU) " + this.getFrontSku()
        + ", (Rear SKU) " + this.getRearSku() + ". ";
  }

}

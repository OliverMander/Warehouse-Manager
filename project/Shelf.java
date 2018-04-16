/**
 * The Shelf class, represents a single shelf.
 */
public class Shelf {

  private Sku shelfSku;
  private Fascia fasciaOnShelf;
  private int remainingFascia;
  private int shelfSize;

  /**
   * The constructor for a Shelf.
   * 
   * @param shelfSku The Sku associated with this Shelf.
   * @param fasciaOnShelf The type of Fascia that is stored on this Shelf.
   * @param remainingFascia The amount of Fascia that is left on the Shelf.
   */
  public Shelf(Sku shelfSku, Fascia fasciaOnShelf, int remainingFascia) {
    this.shelfSku = shelfSku;
    this.remainingFascia = remainingFascia;
    this.fasciaOnShelf = fasciaOnShelf;
    this.shelfSize = 30;
  }


  public void setRemainingFascia(int amount) {
    remainingFascia = amount;
  }

  public Sku getShelfSku() {
    return shelfSku;
  }

  /**Get the remaining number of Fascia on the shelf.
   * @return the remaining number of Fascia on the shelf.
   */
  public int getRemainingFascia() {
    return remainingFascia;
  }


  /** get Fascia from shelf.
   * @return the one of the fascia from the shelf.
   */
  public Fascia getFascia() {
    remainingFascia -= 1;
    return fasciaOnShelf;
  }


  public void doResupply() {
    remainingFascia = shelfSize;
  }

}

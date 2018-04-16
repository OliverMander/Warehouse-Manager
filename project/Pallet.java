
public class Pallet {
  
  private final int numOfPallets = 2;
  /**
   * The array index of the pallet that represents the front Fasica.
   * */
  private final int frontPalletIndex = 0;
  /**
   * The array index of the pallet that represents the rear Fascia.
   * */
  private final int rearPalletIndex = 1;
  
  private final int fasciaPerPallet = 4;
  
  private Fascia[][] content;
  
  private OrdersPerProcess checkOrder;
  
  private Fascia[] frontFascias;
  
  private Fascia[] rearFascias;
  
  public Pallet(Fascia[] frontFascias, Fascia[] rearFascias, OrdersPerProcess checkOrder) {
    
    this.frontFascias = frontFascias;
    this.rearFascias = rearFascias;
    this.checkOrder = checkOrder;
  }
  
  public void composePallet() {
    this.content = new Fascia[fasciaPerPallet][numOfPallets];
    for (int i = 0; i < frontFascias.length; i++) {
      this.content[i][0] = frontFascias[i];
    }
    for (int i = 0; i < rearFascias.length; i++) {

      this.content[i][1] = rearFascias[i];
    }

  }
  

  public Fascia[] getFrontFascias() {
    return frontFascias;
  }

  public Fascia[] getRearFascias() {
    return rearFascias;
  }

  public Fascia[][] getContent() {
    return content;
  }



  public OrdersPerProcess getCheckOrder() {
    return checkOrder;
  }

  public void setCheckOrder(OrdersPerProcess checkOrder) {
    this.checkOrder = checkOrder;
  }
  
}

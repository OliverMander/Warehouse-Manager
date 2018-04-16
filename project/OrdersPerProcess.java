
public class OrdersPerProcess {
  
  /**
   * The number of orders needed to make once ProcessingOrder.
   */
  private int orderSize = 4;
  
  /**
   * A list of orders being processed.
   */
  private Order[] order;
  
  /**
   * An ID associated with the processing order.
   */
  private final int processingId;
  
  /**
   * A String denoting the progress of the associated process.
   */
  private String processingProgress;
  
  /**
   * Constructor for the ProcessingOrder class.
   * 
   * @param processingOrder The array of Orders that need to be combined.
   * @param id The unique Id of this ProcessingOrder
   */
  public OrdersPerProcess(Order[] processingOrder, int id) {
    this.order = processingOrder;
    this.processingId = id;
    processingProgress = "Not Picked";
  }
  
  public int getLength() {
    return orderSize;
  }
  
  public String getProcessingProgress() {
    return processingProgress;
  }
  
  public void setProcessingProgress(String progress) {
    processingProgress = progress;
  }
  

  public Order getOrder(int index) {
    return order[index];
  }
  
  public void setAnOrder(int index, Order order) {
    this.order[index] = order;
  }
  
  public Order[] getAllOrders() {
    return order;
  }

  public void setAllOrders(Order[] order) {
    this.order = order;
  }

  public int getProcessingId() {
    return processingId;
  }
  
  /**
   * The String representation of a ProcessingOrder.
   * 
   * @return String The String representation of a ProcessingOrder.
   * */
  public String toString() {
    String orderList = "";
    for (int i = 0; i < orderSize; i++) {
      orderList = orderList + " " + order[i];
    }
    return orderList;
  }
  
}

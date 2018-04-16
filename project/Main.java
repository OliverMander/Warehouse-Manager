import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;

public class Main {

  public static void main(String[] args) throws FileNotFoundException {

    final int eventTrigger = 0;

    WarehouseManager warehouseManager = new WarehouseManager(args);
    ArrayList<Worker> pickerList = new ArrayList<>();
    ArrayList<Worker> sequencerList = new ArrayList<>();
    ArrayList<Worker> loaderList = new ArrayList<>();
    ArrayList<Worker> replenisherList = new ArrayList<>();

    // make this part of configuration ??
    warehouseManager.getEmployeesTable().put("Picker", pickerList);
    warehouseManager.getEmployeesTable().put("Sequencer", sequencerList);
    warehouseManager.getEmployeesTable().put("Loader", loaderList);
    warehouseManager.getEmployeesTable().put("Replenisher", replenisherList);



    Scanner sc = null;
    try {
      // read 16order.txt
      File orderFile = new File(args[0]);
      sc = new Scanner(orderFile);

      // simulation starts
      do {
        String currentEvent = sc.nextLine();

        String[] currentEventParts = currentEvent.split(" ");
        if (currentEventParts[eventTrigger].equals("Order")) {
          warehouseManager.getUnprocessedOrders().add(currentEvent);
          warehouseManager.createProcessedOrder(warehouseManager.getUnprocessedOrders());
        } else {
          Worker currentWorker =
              warehouseManager.hireWorker(currentEventParts[eventTrigger], currentEventParts[1]);
          if (currentWorker == null) {
            currentWorker = warehouseManager.getParticularWorker(currentEventParts[eventTrigger],
                currentEventParts[1]);
          }
          if (currentEventParts[2].equals("rescans")) {
            currentWorker.rescans();
          } else if (currentEventParts[2].equals("ready")) {
            currentWorker.getNextWorkingOrder();
          } else if (currentEventParts[eventTrigger].equals("Replenisher")) {
            String zone = currentEventParts[3];
            String aisle = currentEventParts[4];
            String rack = currentEventParts[5];
            String level = currentEventParts[6];
            currentWorker.setCurrentWorkingLocation(zone, aisle, rack, level);
            currentWorker.Working();
            currentWorker.preparingProductForNextStage();
          } else if (currentEventParts[currentEventParts.length - 1].equals("Marshaling")) {
            currentWorker.preparingProductForNextStage();
          } else {
            currentWorker.Working();
            if (!currentWorker.getJobTitle().equals("Picker")) {
              currentWorker.preparingProductForNextStage();
            }
          }

        }


      } while (sc.hasNext());

      warehouseManager.windDown();

    } catch (IOException exception) {
      exception.printStackTrace();
    } finally {
      sc.close();
      for (int i = 0; i < warehouseManager.getOrderList().size(); i++) {
        warehouseManager.getLogger().log(Level.INFO,
            "The Processing Request ID " + warehouseManager.getOrderList().get(i).getProcessingId()
                + " is " + warehouseManager.getOrderList().get(i).getProcessingProgress());
      }

    }
  }
}


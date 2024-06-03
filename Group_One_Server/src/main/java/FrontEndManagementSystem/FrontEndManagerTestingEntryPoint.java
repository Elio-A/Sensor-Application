package FrontEndManagementSystem;

import Communications.CommunicationsController;
import DataManagementSystem.IDataManagementSystem;
import MockSystem.Mock_DataManager;
import InternalMessaging.ISystemInitialization;
import Marshalling.InternalServerMarshall;
import MonitoringManagementSystem.IMonitoringManagementSystem;
import MockSystem.Mock_MonitoringManager;
import SensorManagementSystem.ISensorManagementSystem;
import MockSystem.Mock_SensorManager;
import ServerLogicEntryPoint.IEntryPoint;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
public class FrontEndManagerTestingEntryPoint implements IEntryPoint {

    private final IDataManagementSystem dataManager = new Mock_DataManager();
    private final ISensorManagementSystem sensorManager = new Mock_SensorManager();
    private final IFrontEndManagementSystem frontEndManager = new FrontEndManager();
    private final IMonitoringManagementSystem monitoringManager = new Mock_MonitoringManager();

    private final int MAX_RETRY_ATTEMPTS;
    private CommunicationsController commsController;
    private final ExecutorService executor = Executors.newFixedThreadPool(4);
    private final List<Future<?>> futures = new ArrayList<>();
    public FrontEndManagerTestingEntryPoint(int maxRetryAttempts) {
        this.MAX_RETRY_ATTEMPTS = maxRetryAttempts;
    }

    private InternalServerMarshall marshall;  // Step 1: Add the InternalServerMarshall instance

    @Override
    public void start(CommunicationsController commsController) {

        this.commsController = commsController;
        futures.add(executor.submit(() -> startSystem(monitoringManager, "Monitoring System")));
        futures.add(executor.submit(() -> startSystem(dataManager, "Data System")));
        futures.add(executor.submit(() -> startSystem(sensorManager, "Sensor System")));
        futures.add(executor.submit(() -> startSystem(frontEndManager, "Front End System")));

        marshall = InternalServerMarshall.getInstance(sensorManager, monitoringManager, dataManager, frontEndManager, commsController);
    }

    public void shutdown() {
        try {
            System.out.println("Attempting to shut down system...");

            executor.shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) { // wait for tasks to complete for up to 30 seconds
                System.out.println("Tasks did not finish in 30 seconds. Trying a forceful shutdown...");
                executor.shutdownNow(); // force shutdown if tasks are still running after 30 seconds
                if (!executor.awaitTermination(30, TimeUnit.SECONDS)) { // wait again after forceful shutdown
                    System.err.println("Executor did not terminate even after a forceful shutdown.");
                } else {
                    System.out.println("Forceful shutdown succeeded.");
                }
            } else {
                System.out.println("Graceful shutdown succeeded.");
            }
        } catch (InterruptedException e) {
            System.err.println("Shutdown process interrupted. Trying a forceful shutdown...");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        for (Future<?> future : futures) {
            if (!future.isDone()) {
                System.out.println("Cancelling a still-running task...");
                future.cancel(true); // Interrupts the task
            }
        }

        System.out.println("Shutdown process complete.");
    }


    private void startSystem(ISystemInitialization system, String systemName) {
        int i = 0;
        while (!system.InitializeSystem()) {
            i++;
            if (i > MAX_RETRY_ATTEMPTS) {
                System.out.println(systemName + " Failed to Activate");
                return;
            }
            waitOneSecond();
        }
        System.out.println(systemName + " Activated");
    }

    private void waitOneSecond() {
        try {
            Thread.sleep(1000); // Sleep for 1 second (1000 milliseconds)
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


package MockSystem;

import Communications.CommunicationsController;
import FrontEndManagementSystem.IFrontEndManagementSystem;
import Marshalling.InternalServerMarshall;
import SharedDataTypes.SensorObjects.VirtualSensorObject;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.concurrent.CompletableFuture;

public class Mock_FrontEndManager implements IFrontEndManagementSystem {
    @Override
    public boolean InitializeSystem() {
        return true;
    }
    public void SendStateFromMonitor(VirtualSensorObject[] sensorsStates){

    }
    public void SendStateFromDMS(VirtualSensorObject[] sensorsStates){

    }
    @Override
    public CompletableFuture<String> onMessageRecieved(String message, String id) {

        System.out.println(message + " FROM " + id);
        VirtualSensorObject toConvert = InternalServerMarshall.getDataManagementSystem().RequestSensorData("DefaultSensor");
        String json = "dud";
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            json = objectMapper.writeValueAsString(toConvert);

        } catch (Exception e) {
            System.out.println("Error serializing SimpleSensorObject to JSON: " + e.getMessage());
        }

        CommunicationsController.Session gui = null;
        for (CommunicationsController.Session sess: InternalServerMarshall.getCommunicationsController().getClientList()) {
            if (!sess.getName().toLowerCase().contains("sensor")){
                gui = sess;
                break;
            }
        }
        gui.sendMessage(json);
        return CompletableFuture.supplyAsync(() -> {
            // Simulate a long-running job
            try {
                Thread.sleep(2000); // This represents a computation that takes time
            } catch (InterruptedException e) {
                // Handle the interruption
                Thread.currentThread().interrupt();
            }
            return "Result of the computation";
        });
    }
}

package FrontEndManagementSystem;

import InternalMessaging.ISystemInitialization;
import SharedDataTypes.SensorObjects.VirtualSensorObject;

import java.util.concurrent.CompletableFuture;

public interface IFrontEndManagementSystem extends ISystemInitialization {
    public void SendStateFromMonitor(VirtualSensorObject[] sensorsStates);
    public void SendStateFromDMS(VirtualSensorObject[] sensorsStates);
    public CompletableFuture<String> onMessageRecieved(String message, String id);

}

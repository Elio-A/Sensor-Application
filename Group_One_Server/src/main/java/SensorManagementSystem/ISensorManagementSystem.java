package SensorManagementSystem;

import Communications.CommunicationsController;
import InternalMessaging.ISystemInitialization;
import SharedDataTypes.SensorObjects.SensorObject;

public interface ISensorManagementSystem extends ISystemInitialization {
    public boolean SendCommandToSensor(SensorObject newState);
    public void onMessageReceived(String message, String sessionID);
}

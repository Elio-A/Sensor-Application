package MonitoringManagementSystem;

import InternalMessaging.ISystemInitialization;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;

public interface IMonitoringManagementSystem extends ISystemInitialization {
    public VirtualSensorObject RequestSensorInfo(String sensorName);
    public boolean MonitorSensor (SensorObject sensor);

}

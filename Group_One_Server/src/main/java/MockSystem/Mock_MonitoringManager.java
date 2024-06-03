package MockSystem;

import java.util.Random;

import MonitoringManagementSystem.IMonitoringManagementSystem;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleVirtualSensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;
import SharedDataTypes.SensorState;

public class Mock_MonitoringManager implements IMonitoringManagementSystem {
    @Override
    public boolean InitializeSystem() {
        return true;
    }
    public VirtualSensorObject RequestSensorInfo(String sensorName){
        VirtualSensorObject reply = new SimpleVirtualSensorObject();
        Random random = new Random();

        reply.name=sensorName;
        reply.state= SensorState.fromValue(random.nextInt(4) + 1);

        return reply;
    }
    public boolean MonitorSensor (SensorObject sensor){
        return true;
    }
}

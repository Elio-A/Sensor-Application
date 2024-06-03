package MonitoringManagementSystem;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorRecord;
import SharedDataTypes.SensorState;

public class ActiveFunctioningSensorTest implements MonitoringTest {
    private MonitoringManager manager;

    public ActiveFunctioningSensorTest(MonitoringManager manager) {
        this.manager = manager;
    }

    @Override
    public void initialize() {
        manager.subscribeToMonitor(this::checkSensorState);
    }

    private void checkSensorState(java.util.List<SensorRecord> sensorRecords) {
        for (SensorRecord record : sensorRecords) {
            SensorObject sensor = record.getSensor();

            // Check if sensor state is ActiveFunctioning
            if (sensor.state == SensorState.ActiveFunctioning) {
                // Remove tickets associated with this sensor
                manager.removeTicketsForSensor(sensor);
            }
        }
    }
}

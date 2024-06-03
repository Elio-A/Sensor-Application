package MonitoringManagementSystem;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleVirtualSensorObject;
import SharedDataTypes.SensorObjects.VirtualSensorObject;
import SharedDataTypes.SensorRecord;
import SharedDataTypes.SensorState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

public class MonitoringManager implements IMonitoringManagementSystem{
    private Monitor monitor;
    private List<MonitoringTest> tests = new ArrayList<>();

    @Override
    public boolean InitializeSystem() {
        monitor = new Monitor();
        monitor.startMonitoring();

        // Create and initialize tests
        tests.add(new SampleRangeTest(this));
        tests.add(new StaleSensorDataTest(this));
        tests.add(new ActiveFunctioningSensorTest(this));
        startTests();

        return true;
    }

    private void startTests() {
        for (MonitoringTest test : tests) {
            test.initialize();
        }
    }

    public VirtualSensorObject RequestSensorInfo(String sensorName){
        VirtualSensorObject reply = new SimpleVirtualSensorObject();
        Random random = new Random();

        reply.name=sensorName;
        reply.state= SensorState.fromValue(random.nextInt(4) + 1);

        return reply;
    }
    public void createTicket(SensorObject sensor, String fieldNotes) {
        monitor.createTicket(sensor, fieldNotes);
    }
    public boolean MonitorSensor(SensorObject sensor) {
        return monitor.monitorSensor(sensor);
    }

    public void subscribeToMonitor(Consumer<List<SensorRecord>> action) {
        monitor.subscribe(action);
    }

    public void removeTicketsForSensor(SensorObject sensor){
        monitor.removeTicketsForSensor(sensor);
    }
}

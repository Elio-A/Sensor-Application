package MonitoringManagementSystem;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorRecord;
import SharedDataTypes.SensorSample;
import SharedDataTypes.SensorState;

public class SampleRangeTest implements MonitoringTest {
    private MonitoringManager manager;

    public SampleRangeTest(MonitoringManager manager) {
        this.manager = manager;
    }

    @Override
    public void initialize() {
        manager.subscribeToMonitor(this::checkSensorSamples);
    }

    private void checkSensorSamples(java.util.List<SensorRecord> sensorRecords) {
        for (SensorRecord record : sensorRecords) {
            SensorObject sensor = record.getSensor();
            boolean isOutOfRange = false;

            // First, check if any sample is out of range
            for (SensorSample sample : sensor.samples) {
                if (sample.value < sensor.min || sample.value > sensor.max) {
                    isOutOfRange = true;
                    break;
                }
            }

            // If any sample is out of range, set all samples to -1
            if (isOutOfRange) {
                for (SensorSample sample : sensor.samples) {
                    sample.value = -1;
                }
                sensor.state = SensorState.InactiveMalfunctioning;
                manager.createTicket(sensor, "Sample out of range");
            }
        }
    }
}

package MonitoringManagementSystem;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorRecord;
import SharedDataTypes.SensorSample;
import SharedDataTypes.SensorState;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class StaleSensorDataTest implements MonitoringTest {
    private static final int STALE_THRESHOLD_SECONDS = 15;
    private MonitoringManager manager;

    public StaleSensorDataTest(MonitoringManager manager) {
        this.manager = manager;
    }

    @Override
    public void initialize() {
        manager.subscribeToMonitor(this::checkSensorSamples);
    }

    private void checkSensorSamples(java.util.List<SensorRecord> sensorRecords) {
        for (SensorRecord record : sensorRecords) {
            SensorObject sensor = record.getSensor();
            if (sensor.state != SensorState.ActiveFunctioning) {
                updateSamples(record);
            } else if (isDataStale(record)) {
                updateSamples(record);
                sensor.state = SensorState.InactiveMalfunctioning;
            }
        }
    }

    private boolean isDataStale(SensorRecord record) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime recordTimeAdded = record.timeAdded;

        long secondsDiff = ChronoUnit.SECONDS.between(recordTimeAdded, now);
        return secondsDiff > STALE_THRESHOLD_SECONDS;
    }


    private void updateSamples(SensorRecord record) {
        SensorObject sensor = record.getSensor();

        if (record.readyToReduce) {
            // If the record is ready to reduce, clear the samples and add only one sample
            sensor.samples = new SensorSample[1];
            SensorSample newSample = new SensorSample();
            newSample.sampleDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());
            newSample.value = -1;
            sensor.samples[0] = newSample;
        } else {
            // Existing logic for creating new samples
            ArrayList<SensorSample> newSamples = new ArrayList<>();
            LocalDateTime latestSampleTime = LocalDateTime.now();

            for (LocalDateTime time = latestSampleTime;
                 time.isAfter(latestSampleTime.minusSeconds(STALE_THRESHOLD_SECONDS));
                 time = time.minusSeconds((long) sensor.sampleRate)) {

                SensorSample newSample = new SensorSample();
                newSample.sampleDateTime =DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(time);
                newSample.value = -1;
                newSamples.add(0, newSample);  // Add in reverse order to maintain chronological order
            }
            sensor.samples = newSamples.toArray(new SensorSample[0]);
            record.readyToReduce = true;
        }

        // Update the state of the sensor
        sensor.state = SensorState.InactiveMalfunctioning;
        manager.createTicket(sensor, "Sensor Inactive");
    }

}

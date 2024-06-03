package SharedDataTypes;

import SharedDataTypes.SensorObjects.SensorObject;

import java.time.LocalDateTime;

public class SensorRecord {
    private SensorObject sensor;
    public LocalDateTime timeAdded;
    public boolean readyToReduce = false;

    public SensorRecord(SensorObject sensor) {
        this.sensor = sensor;
        this.timeAdded = LocalDateTime.now();
    }

    public SensorObject getSensor() {
        return sensor;
    }

    public LocalDateTime getTimeAdded() {
        return timeAdded;
    }

}

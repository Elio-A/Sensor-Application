package SharedDataTypes;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SensorSample {
    public String sampleDateTime;
    public float value;

    @Override
    public String toString() {
        return "{"+String.format("%.3f", value)+" : "+sampleDateTime+"}";
    }
    public SensorSample clone(){
        SensorSample clone = new SensorSample();
        clone.sampleDateTime = sampleDateTime;
        clone.value = value;

        return clone;
    }
}

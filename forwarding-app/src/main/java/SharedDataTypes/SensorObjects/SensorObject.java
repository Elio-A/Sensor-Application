package SharedDataTypes.SensorObjects;

import SharedDataTypes.SensorSample;
import SharedDataTypes.SensorState;
import SharedDataTypes.SensorType;

public abstract class SensorObject {

    public String name,nickName,location,description;
    public float sampleRate,max,min;
    public SensorSample[] samples;
    public SensorState state;
    public SensorType type;
    public int errorMode = 0;

    @Override
    public String toString() {
        return String.format(
                "name        : %s%n" +
                        "nick name   : %s%n" +
                        "location    : %s%n" +
                        "description : %s%n" +
                        "sampleRate  : %.2f%n" +
                        "max         : %.2f%n" +
                        "min         : %.2f%n" +
                        "samples     : %s%n" +
                        "state       : %s%n" +
                        "type        : %s%n",
                name,
                nickName,
                location,
                description,
                sampleRate,
                max,
                min,
                (samples == null ? "null" : "[" + samples.length + " samples]"),  // Just showing the count, but you can adjust as needed
                (state == null ? "null" : state.toString()),  // Explicitly calling toString() on the enum
                (type == null ? "null" : type.toString())    // Explicitly calling toString() on the enum
        );
    }
    public abstract SensorObject clone();
}

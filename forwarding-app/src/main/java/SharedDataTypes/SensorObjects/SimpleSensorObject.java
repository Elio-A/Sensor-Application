package SharedDataTypes.SensorObjects;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorSample;
import SharedDataTypes.SensorState;
import SharedDataTypes.SensorType;

public class SimpleSensorObject extends SensorObject {
    public SensorObject clone()
    {
        SensorObject clone = new SimpleSensorObject();
        clone.name = name;
        clone.location = location;
        clone.description = description;

        clone.nickName = nickName;
        clone.sampleRate = sampleRate;
        clone.max = max;
        clone.min = min;

        SensorSample[] clonedSamples = new SensorSample[samples.length];

        for (int i = 0; i<samples.length; i++){
            clonedSamples[i] = samples[i].clone();
        }

        clone.samples = clonedSamples;
        clone.state = state;
        clone.type = type;


        return clone;
    }
}

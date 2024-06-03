package SharedDataTypes.SensorObjects;

import SharedDataTypes.SensorSample;
import SharedDataTypes.SensorState;
import SharedDataTypes.SensorType;

public abstract class VirtualSensorObject {
        public String name,nickName,location,description;
        public float sampleRate,max,min;
        public SensorState state;
        public SensorType type;
        public SensorSample[] samples;

}

package SharedDataTypes.SensorObjects;

import SharedDataTypes.SensorSample;
import SharedDataTypes.SensorState;
import SharedDataTypes.SensorType;

public abstract class VirtualSensorObject {
        public String name,nickName,location,description;
        public float sampleRate;
        public float max;
        public float min;
        public SensorState state;
        public SensorType type;
        public SensorSample[] samples;

}

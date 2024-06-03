import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import SharedDataTypes.SensorSample;
import SharedDataTypes.SensorState;
import SharedDataTypes.SensorType;
import SharedDataTypes.SensorObjects.SimpleVirtualSensorObject;

public class VirtualSensorObjectTest {
    @Test
    public void testVirtualSensorObjectProperties() {
        SimpleVirtualSensorObject virtualSensorObject = new SimpleVirtualSensorObject(); 

        virtualSensorObject.name = "TestSensor";
        virtualSensorObject.location = "TestLocation";
        virtualSensorObject.description = "TestDescription";
        virtualSensorObject.sampleRate = 2.5f;
        virtualSensorObject.state = SensorState.ActiveFunctioning;
        virtualSensorObject.type = SensorType.Energy;
        virtualSensorObject.samples = new SensorSample[]{new SensorSample(), new SensorSample()};

        assertEquals("TestSensor", virtualSensorObject.name);
        assertEquals("TestLocation", virtualSensorObject.location);
        assertEquals("TestDescription", virtualSensorObject.description);
        assertEquals(2.5f, virtualSensorObject.sampleRate, 0.001); 
        assertEquals(SensorState.ActiveFunctioning, virtualSensorObject.state);
        assertEquals(SensorType.Energy, virtualSensorObject.type);
        assertNotNull(virtualSensorObject.samples);
        assertEquals(2, virtualSensorObject.samples.length); 

    }

}

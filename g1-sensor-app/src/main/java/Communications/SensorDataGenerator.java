package Communications;

import Communications.CommunicationsService;
import Communications.SensorApp;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleSensorObject;
import SharedDataTypes.SensorSample;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class SensorDataGenerator {
    private CommunicationsService commsService;
    private Random rand;

    private Timer sampleTimer;

    public SensorDataGenerator(CommunicationsService commsService) {
        this.commsService = commsService;
    }

    public void start() {
        rand = new Random();
        if (sampleTimer == null){
            sampleLoop();
            System.out.println("Sampling started.");
        }
        else{
            System.out.println("Sampling already started.");
        }
    }

    public void stop() {
        if (sampleTimer != null) {
            sampleTimer.cancel();
            sampleTimer = null;
            System.out.println("Sampling stopped.");
        }
        else{
            System.out.println("Sampling has not started yet.");
        }
    }

    private void send() {
        SimpleSensorObject toSend = new SimpleSensorObject();
        toSend.description = SensorApp.sensorDescription;
        toSend.name = SensorApp.name;
        toSend.nickName = SensorApp.nickName;
        toSend.location = SensorApp.sensorLocation; // Changed from sensorDescription to sensorLocation
        toSend.state = SensorApp.sensorState;
        toSend.type = SensorApp.type;
        toSend.sampleRate = SensorApp.sampleRate;
        toSend.min = SensorApp.min;
        toSend.max = SensorApp.max;

        // Generate a random float within the range [SensorApp.min, SensorApp.max]
        float range = SensorApp.max - SensorApp.min;
        float randomFloat = SensorApp.min + rand.nextFloat() * range;

        toSend.samples = new SensorSample[1];
        toSend.samples[0] = new SensorSample();
        toSend.samples[0].value = randomFloat;
        toSend.samples[0].sampleDateTime  = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(ZonedDateTime.now());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            String json = objectMapper.writeValueAsString(toSend);

            commsService.sendCommandToServer(json);
        } catch (Exception e) {
            System.out.println("Error serializing SimpleSensorObject to JSON: " + e.getMessage());
        }
    }
    private void send(float sampleValue) {
        SimpleSensorObject toSend = new SimpleSensorObject();
        toSend.description = SensorApp.sensorDescription;
        toSend.name = SensorApp.name;
        toSend.nickName = SensorApp.nickName;
        toSend.location = SensorApp.sensorLocation; // Changed from sensorDescription to sensorLocation
        toSend.state = SensorApp.sensorState;
        toSend.type = SensorApp.type;
        toSend.sampleRate = SensorApp.sampleRate;
        toSend.min = SensorApp.min;
        toSend.max = SensorApp.max;

        toSend.samples = new SensorSample[1];
        toSend.samples[0] = new SensorSample();
        toSend.samples[0].value = sampleValue;
        toSend.samples[0].sampleDateTime  = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
            String json = objectMapper.writeValueAsString(toSend);

            commsService.sendCommandToServer(json);
        } catch (Exception e) {
            System.out.println("Error serializing SimpleSensorObject to JSON: " + e.getMessage());
        }
    }

    private int currentSampleRate;

    private void adjustTimer() {
        if (sampleTimer != null) {
            sampleTimer.cancel();
            sampleTimer.purge();
        }

        if (SensorApp.sampleRate > 0) {
            sampleTimer = new Timer();
            currentSampleRate = SensorApp.sampleRate;
            sampleTimer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if (SensorApp.sampleRate > 0) {
                        if (SensorApp.errorMode==0)
                            send();
                        else if (SensorApp.errorMode==1)
                            send(SensorApp.max+10);
                    }
                }
            }, 0, currentSampleRate * 1000L);
        }
    }

    public synchronized void updateSampleRate(int newSampleRate) {
        if (newSampleRate != currentSampleRate) {
            SensorApp.sampleRate = newSampleRate;
            adjustTimer();
        }
    }

    private void sampleLoop() {
        sampleTimer = new Timer();
        sampleTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (SensorApp.sampleRate > 0) {
                    if (SensorApp.errorMode == 0)
                        send();
                    else if (SensorApp.errorMode == 1)
                        send(SensorApp.max+10);
                }
            }
        }, 0, SensorApp.sampleRate * 1000L);
    }
}

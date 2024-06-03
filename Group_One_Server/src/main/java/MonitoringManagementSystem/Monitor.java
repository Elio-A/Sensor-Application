package MonitoringManagementSystem;

import Marshalling.InternalServerMarshall;
import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleSensorObject;
import SharedDataTypes.SensorRecord;
import SharedDataTypes.TicketObject;
import SharedDataTypes.TicketState;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class Monitor {
    private List<SensorRecord> sensorRecords = new ArrayList<>();
    private List<Consumer<List<SensorRecord>>> actions = new ArrayList<>();
    private Thread eventLoopThread;
    private List<TicketObject> tickets = new ArrayList<>();
    private AtomicInteger ticketIdGenerator = new AtomicInteger(0);
    public boolean monitorSensor(SensorObject sensor) {
        synchronized (sensorRecords) {
            for (SensorRecord record : sensorRecords) {
                if (record.getSensor().name.equals(sensor.name)) {
                    sensorRecords.remove(record);
                    break;
                }
            }
            sensorRecords.add(new SensorRecord(sensor));
        }
        return true;
    }
    public void createTicket(SensorObject sensor, String fieldNotes) {
        // Check if a ticket for this sensor already exists
        for (TicketObject existingTicket : tickets) {
            if (existingTicket.sensorInfo.name.equals(sensor.name)) {
                // A ticket for this sensor already exists, so do not create a new one
                return;
            }
        }

        // Create a new ticket
        TicketObject ticket = new TicketObject();
        ticket.ticketID = ticketIdGenerator.incrementAndGet();
        ticket.fieldNotes = fieldNotes;
        ticket.sensorInfo = sensor;
        ticket.creationDate = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());

        ticket.ticketState = TicketState.Unassigned; // Default state
        // Other fields can be set as needed

        tickets.add(ticket);
    }
    public List<TicketObject> getTickets() {
        return new ArrayList<>(tickets); // Return a copy to avoid modification outside
    }
    public void subscribe(Consumer<List<SensorRecord>> action) {
        actions.add(action);
    }

    public void startMonitoring() {
        eventLoopThread = new Thread(this::executeActions);
        eventLoopThread.start();
    }

    private void executeActions() {
        while (!Thread.currentThread().isInterrupted()) {
            synchronized (sensorRecords) {
                for (Consumer<List<SensorRecord>> action : actions) {
                    action.accept(new ArrayList<>(sensorRecords));
                }
                sendSensors();
                sendTickets();
            }
            try {
                Thread.sleep(1000); // Delay for 1 second
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void sendSensors(){

        for (int i = 0; i< sensorRecords.size(); i++){
            SensorObject cloned = sensorRecords.get(i).getSensor().clone();
            for (int j = 0; j< cloned.samples.length; j++) {
                if (cloned.samples[j].value == -1){
                    cloned.samples[j].sampleDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss").format(LocalDateTime.now());
                }
            }
            InternalServerMarshall.getDataManagementSystem().recordSensorData(cloned);
        }
    }
    public void sendTickets() {
        for (TicketObject ticket : tickets) {
            if (!ticket.isSent) {
                InternalServerMarshall.getDataManagementSystem().RecordTicket(ticket);
                String[] scheds = InternalServerMarshall.getDataManagementSystem().Get_all_schedules();
                ticket.isSent = true; // Mark the ticket as sent
            }
        }
    }
    public void removeTicketsForSensor(SensorObject sensor) {
        if (sensor == null) {
            return; // Handle null sensor case
        }

        tickets.removeIf(ticket -> ticket.sensorInfo.name.equals(sensor.name));
    }
}

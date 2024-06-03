package Communications;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorObjects.SimpleSensorObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.client.RestTemplate;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class CommunicationsService {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationsService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private JmDNS jmdns;
    private String serverAddress = "http://127.0.0.1:8080";

    @Autowired
    private Environment environment;  // Autowire the Environment bean
    ObjectMapper objectMapper = new ObjectMapper();
    public volatile boolean serverResolved = false;  // Flag to indicate if the server has been resolved


    @Autowired
    public CommunicationsService(Environment environment) throws IOException {  // Constructor injection
        this.environment = environment;
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

        initializeJmDNS();
    }

    private void initializeJmDNS() throws IOException {
        jmdns = JmDNS.create(InetAddress.getLoopbackAddress());

        // Listener for services
        jmdns.addServiceListener("_http._tcp.local.", new ServiceListener() {
            @Override
            public void serviceAdded(ServiceEvent event) {
                System.out.println("Service added: " + event.getInfo());
            }

            @Override
            public void serviceRemoved(ServiceEvent event) {
                System.out.println("Service removed: " + event.getInfo());
            }

            @Override
            public void serviceResolved(ServiceEvent event) {
                System.out.println("Service resolved: " + event.getInfo());
                String address = event.getInfo().getHostAddresses()[0];
                int port = event.getInfo().getPort();
                serverAddress = "http://" + address + ":" + port;
                serverResolved = true;
            }
        });
    }

    public void closeJmDNS() throws IOException {
        if (jmdns != null) {
            jmdns.close();
        }
    }

    public void sendCommandToServer(String message) {
        String localPort = environment.getProperty("local.server.port"); // Retrieve the port on which the client's web server is listening
        Command command = new Command(SensorApp.name, localPort, message);

        try {
            restTemplate.postForEntity(serverAddress + "/api/receiveFromClient", command, Void.class);
        } catch (Exception e) {
            System.out.println("Error sending command: " + e.getMessage());
        }
    }


    public void onMessageReceived(String message) {
        // Handle the incoming message here
        System.out.println("Received message: " + message);
        try{

            SensorObject newState = objectMapper.readValue(message, SimpleSensorObject.class);
            SensorApp.Instance.UpdateSensorInformation(newState);

        }
        catch(Exception e)
        {
            System.out.println("ERROR: " +e);
        }
    }

    public static class Command {
        public String clientName;
        public String clientPort;
        public String message;

        public Command(String client,String clientPort, String message) {
            this.clientName = client;
            this.clientPort = clientPort;
            this.message = message;
        }
    }
}

package Communications;

import Marshalling.InternalServerMarshall;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.jmdns.JmDNS;
import javax.jmdns.ServiceInfo;
import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import javax.annotation.PreDestroy;

/**
 * Controller that handles communication-related API requests.
 */
@RestController
@RequestMapping("/api")
public class CommunicationsController implements ApplicationListener<ContextClosedEvent> {

    private final List<Session> connectedClients = new ArrayList<>();
    /**
     * Retrieves a list of currently connected client sessions.
     *
     * @return A list of active client sessions.
     */
    public List<Session>  getClientList(){
        return connectedClients;
    }
    private JmDNS jmdns;
    private ServiceInfo serviceInfo;
    private final RestTemplate restTemplate;
    public Environment environment;

    /**
     * Constructs a new CommunicationsController.
     *
     * @param restTemplateBuilder Helps create a new RestTemplate instance.
     */
    public CommunicationsController(RestTemplateBuilder restTemplateBuilder, Environment environment) {
        this.restTemplate = restTemplateBuilder.build();
        this.environment = environment;
    }
    /**
     * Endpoint for receiving messages from a client.
     *
     * @param command The command object containing client and message information.
     * @return ResponseEntity indicating the status of the message receipt.
     */
    @PostMapping("/receiveFromClient")
    public ResponseEntity<?> receiveFromClient(@RequestBody Command command) {
        Session session = findOrCreateSession(command.clientName, command.clientPort);
        if (session!=null)
            System.out.println("Session Found:\n "+ session.toString());
        else
            System.out.println("Session Not Found");

        session.onMessageReceived(command);

        return ResponseEntity.ok("goods bruh");
    }

    /**
     * Endpoint for receiving messages from a client.
     *
     * @param command The command object containing gui-client and message information.
     * @return ResponseEntity indicating the status of the message receipt.
     */
    @PostMapping("/receiveFromGui")
    public Mono<ResponseEntity<Command>> receiveFromGui(@RequestBody Command command) {
        Session session = findOrCreateSession(command.clientName, command.clientPort);
        // Log the receipt of the message
        System.out.println("Received message from GUI on port: " + command.clientPort);

        // Forward to server and return the response in a non-blocking manner
        return  session.onMessageReceived(command)
                .doOnNext(responseCommand -> {
                    // This will execute when the forwardToServer Mono emits its item
                    System.out.println("Received response from server: " + responseCommand);
                })
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Provide a default if no item is emitted
    }


    /**
     * Endpoint for sending a message to a specific client.
     *
     * @param command The command object containing client and message information.
     * @return ResponseEntity indicating the status of the message sending.
     */
    @PostMapping("/send")
    public ResponseEntity<?> sendMessageToClient(@RequestBody Command command) {
        Session client = findSessionByName(command.clientName);
        if (client != null) {
            String clientUrl = "http://" + "localhost" + ":" + client.getPort() + "/receive";
            restTemplate.postForEntity(clientUrl, command, Void.class);
            return ResponseEntity.ok("Message sent to client");
        } else {
            return ResponseEntity.badRequest().body("Client not found");
        }
    }

    private Session findOrCreateSession(String name, String port) {
        Session session = findSessionByName(name);
        System.out.println("Looking for session named: "+name);

        if (session == null) {
            session = new Session(name, port, this, environment);
            connectedClients.add(session);
        }

        return session;
    }

    public Session findSessionByName(String name) {
        for (Session client : connectedClients) {
            if (client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }

    public Session findSessionById(String id) {
        for (Session client : connectedClients) {
            if (client.getId().equals(id)) {
                return client;
            }
        }
        return null;
    }
    /**
     * Initializer bean for JmDNS, used for service discovery.
     *
     * @return A configured JmDNS instance.
     * @throws IOException If an error occurs while initializing JmDNS.
     */
    @Bean
    public JmDNS jmDNS() throws IOException {
        jmdns = JmDNS.create(InetAddress.getByName("127.0.0.1"));

        // Register a service for clients to discover
        serviceInfo = ServiceInfo.create("_http._tcp.local.", "G1Server", 8080, "path=index.html");
        jmdns.registerService(serviceInfo);

        return jmdns;
    }
    /**
     * Listener to handle the event when the Spring context is closed.
     *
     * @param event The context close event.
     */
    @Override
    public void onApplicationEvent(ContextClosedEvent event) {
        if (jmdns != null) {
            try {
                jmdns.close();
            } catch (IOException e) {
                System.err.println("Error closing JmDNS: " + e.getMessage());
            }
        }
    }
    /**
     * Cleanup method called before the bean is destroyed.
     */
    @PreDestroy
    public void cleanup() {
        if (jmdns != null) {
            try {
                jmdns.unregisterService(serviceInfo); // Unregister the service
                jmdns.close(); // Close JmDNS
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Represents a command object with client details and a message.
     */
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
    /**
     * Represents a client session with the server.
     * This class encapsulates details about each connected client, allowing for communication and message processing.
     */
    public static class Session {
       private boolean isSensor;
        private String id;   // Unique ID for each client
        private String name;
        private String port;
        /** Reference to the main CommunicationsController to send messages. */
        private final CommunicationsController comms;
        private final Environment environment;

        /**
         * Creates a new client session.
         *
         * @param name Name of the client.
         * @param port Port at which the client operates.
         * @param comms Reference to the main CommunicationsController.
         */
        public Session(String name, String port, CommunicationsController comms, Environment envirnment){
            this.environment = envirnment;
            this.port= port;
            this.name = name;
            this.comms = comms;
            this.isSensor = name.toLowerCase().contains("sensor");
            id = generateUniqueId();

            String acknowledged = "App Session Made";
            if(isSensor)
                acknowledged = "Sensor Session Made";
            System.out.println(acknowledged);
        }


        /**
         * Retrieves the unique identifier for the session.
         *
         * @return A string representing the unique ID of the session.
         */
        public String getId() {
            return id;
        }
        /**
         * Retrieves the port of the client associated with this session.
         *
         * @return A string representing the client's port.
         */
        public String getPort() {
            return port;
        }
        /**
         * Retrieves the name of the client associated with this session.
         *
         * @return A string representing the client's name.
         */
        public String getName() {
            return name;
        }
        /**
         * Processes the message received from the client associated with this session.
         *
         * @param command The message command sent by the client.
         */
        public Mono<ResponseEntity<Command>> onMessageReceived(Command command) {
            if (isSensor) {
                InternalServerMarshall.getSensorSystem().onMessageReceived(command.message, this.id);
                return null;
            }
            else {
            return Mono.fromCallable(() -> {
                        String response = "Message Received!";
                        CompletableFuture<String> futureResponse = null;

                        futureResponse = InternalServerMarshall.getFrontEndSystem().onMessageRecieved(command.message, this.id);
                        response = futureResponse.get();

                        System.out.println("it worked! in on received in commscontroller");


                        // Assuming the above methods do not return a value and just perform some action.
                        return new Command(
                                environment.getProperty("server.address", "defaultServerName"),
                                environment.getProperty("local.server.port", Integer.class, -1).toString(),
                                response);
                    })
                    .map(ResponseEntity::ok)
                    .onErrorResume(e -> {
                        System.out.println("Error in on received in commscontroller: " + e.getMessage());
                        return Mono.just(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new Command(
                                environment.getProperty("server.address", "defaultServerName"),
                                environment.getProperty("local.server.port", Integer.class, -1).toString(),
                                "Error: " + e.getMessage())));
                    });
            }
        }

        /**
         * Sends a message to the client associated with this session.
         *
         * @param message The message string to be sent to the client.
         */
        public void sendMessage(String message){
            Command toSend = new Command(name,port,message);
            if (isSensor)
                comms.sendMessageToClient(toSend);
        }


    }
    /**
     * Generates a unique identifier for a session.
     *
     * @return A unique string identifier.
     */
    private static String generateUniqueId() {
        // Generate a unique ID for the session, e.g., using UUIDs:
        return UUID.randomUUID().toString();
    }
}

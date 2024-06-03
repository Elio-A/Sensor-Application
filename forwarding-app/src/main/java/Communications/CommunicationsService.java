package Communications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import javax.jmdns.JmDNS;
import javax.jmdns.ServiceEvent;
import javax.jmdns.ServiceListener;
import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.CompletableFuture;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Service
public class CommunicationsService {
    private static final Logger logger = LoggerFactory.getLogger(CommunicationsService.class);
    private final RestTemplate restTemplate = new RestTemplate();
    private JmDNS jmdns;
    private String serverAddress = "http://127.0.0.1:8080";
    private final WebClient webClient; // Declare WebClient

    @Autowired
    private Environment environment;  // Autowire the Environment bean



    @Autowired
    public CommunicationsService(Environment environment, WebClient.Builder webClientBuilder) throws IOException {  // Constructor injection
        this.environment = environment;
        this.webClient = webClientBuilder.baseUrl(serverAddress).build();

        initializeJmDNS();
    }

    public void forwardToGui(Command command) {
        System.out.println("Message forwarded from " + command.clientName + " to GUI");

        // Use the received port for the GUI endpoint
        String guiEndpoint = "http://127.0.0.1:" + ForwardingEndPoint.lastReceivedPort + "/";

        try {
            restTemplate.postForEntity(guiEndpoint, command, Void.class);
            System.out.println("Message forwarded from " + command.clientName + " to Unity GUI");
        } catch (Exception e) {
            System.out.println("Error sending command to Unity GUI: " + e.getMessage());
        }
    }

/*
    public void forwardToServer(Command command) {
        String localPort = environment.getProperty("local.server.port");
        Command newCommand = new Command(ForwardingApp.name, localPort, command.message);

        try {
            restTemplate.postForEntity(serverAddress + "/api/receiveFromGui", newCommand, Void.class);
            System.out.println("Message forwarded from " + newCommand.clientName + " to " + command.clientName);
        } catch (Exception e) {
            System.out.println("Error sending command: " + e.getMessage());
        }
    }*/
public Mono<Command> forwardToServer(Command command) {
    String localPort = environment.getProperty("local.server.port");
    Command newCommand = new Command(ForwardingApp.name, localPort, command.message);

    return webClient.post()
            .uri(serverAddress + "/api/receiveFromGui")
            .bodyValue(newCommand)
            .retrieve()
            .bodyToMono(Command.class) // Expecting Command as response
            .doOnNext(responseCommand -> {
                // This logs the successful response
                System.out.println("Response received: " + responseCommand.message);
            })
            .onErrorResume(e -> {
                // Log and handle error
                System.out.println("Error sending command: " + e.getMessage());
                // Return a fallback command with the error message
                return Mono.just(new Command("Error", localPort, "Error processing your request: " + e.getMessage()));
            });
}



    private Command handleResponse(ResponseEntity<Void> response, Command newCommand) {
        System.out.println("Message forwarded from " + newCommand.clientName + " to " + newCommand.clientName);
        return newCommand;
    }

    private Command handleException(Throwable e, Command command) {
        System.out.println("Error sending command: " + e.getMessage());
        // Handle exception (e.g., by returning a modified command or a specific error command object)
        return command; // Modify as needed
    }

    private void initializeJmDNS() throws IOException {
        jmdns = JmDNS.create(InetAddress.getByName("127.0.0.1"));

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
                System.out.println("READY");
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
        Command command = new Command(ForwardingApp.name, localPort, message);

        try {
            restTemplate.postForEntity(serverAddress + "/api/receiveFromClient", command, Void.class);
        } catch (Exception e) {
            System.out.println("Error sending command: " + e.getMessage());
        }
    }


    public void onMessageReceived(String message) {
        // Handle the incoming message here
        System.out.println("Received message: " + message);
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

package Communications;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@RestController
public class ForwardingEndPoint {

    private final CommunicationsService commsService;
    public static String lastReceivedPort;

    public ForwardingEndPoint(CommunicationsService commsService) {
        this.commsService = commsService;
    }

    @PostMapping("/receiveFromServer")
    public void receiveFromServer(@RequestBody CommunicationsService.Command command) {
        commsService.forwardToGui(command);
    }
/*
    @PostMapping("/receiveFromGui")
    public void receiveFromGui(@RequestBody CommunicationsService.Command command) {
        if (Integer.parseInt(command.clientPort) != 0)
            lastReceivedPort = command.clientPort;
        System.out.println("GUI port: "+ command.clientPort);
        commsService.forwardToServer(command);
    }*/

    @PostMapping("/receiveFromGui")
    public Mono<ResponseEntity<CommunicationsService.Command>> receiveFromGui(
            @RequestBody CommunicationsService.Command command) {

        // Validate the port
        int port;
        try {
            port = Integer.parseInt(command.clientPort);
            if (port != 0) {
                lastReceivedPort = command.clientPort;
            }
        } catch (NumberFormatException e) {
            System.out.println(e);
        }

        // Log the receipt of the message
        System.out.println("Received message from GUI on port: " + command.clientPort);

        // Forward to server and return the response in a non-blocking manner
        return commsService.forwardToServer(command)
                .doOnNext(responseCommand -> {
                    // This will execute when the forwardToServer Mono emits its item
                    System.out.println("Received response from server: " + responseCommand.message);
                })
                .map(ResponseEntity::ok) // Map the Command to a ResponseEntity
                .defaultIfEmpty(ResponseEntity.notFound().build()); // Provide a default if no item is emitted
    }


}

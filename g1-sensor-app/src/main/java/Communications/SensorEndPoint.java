package Communications;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SensorEndPoint {

    private final CommunicationsService commsService;

    public SensorEndPoint(CommunicationsService commsService) {
        this.commsService = commsService;
    }

    @PostMapping("/receive")
    public void receiveMessage(@RequestBody CommunicationsService.Command command) {
        System.out.println("GOT IT: "+ command.message);
        commsService.onMessageReceived(command.message);
    }
}


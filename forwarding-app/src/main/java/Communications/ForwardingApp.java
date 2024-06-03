package Communications;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.io.IOException;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class ForwardingApp {

    private static ConfigurableApplicationContext context;
    private static final Logger logger = LoggerFactory.getLogger(ForwardingApp.class);
    public static String name = "Forwarding";

    private CommunicationsViscount cV = new CommunicationsViscount();

    @Autowired
    private CommunicationsService commsService;
    public static void main(String[] args) {
        String name = "localhost";

        ForwardingApp.begin(new String[]{}, name);
    }
    public ForwardingApp() {
        // Gracefully shut down JmDNS on exit
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                if (commsService != null) {
                    commsService.closeJmDNS();
                }
            } catch (IOException e) {
                logger.error("Error closing JmDNS: {}", e.getMessage());
            }
        }));
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Type 'quit' to exit application");

            String choice = scanner.nextLine().toLowerCase();

            switch (choice) {
                case "quit":
                case "exit":
                case "shutdown":
                    System.out.println("Exiting application.");
                    scanner.close();
                    if (context != null) {
                        SpringApplication.exit(context, () -> 0);
                    }
                    System.exit(0);
                    break;
                default:
                    System.out.println(cV.getViscountcy());
            }
        }
    }


    public static void begin(String[] args, String name) {
        ForwardingApp.name = name;
        // Start Spring Boot application and let it choose a free port
        SpringApplication app = new SpringApplication(ForwardingApp.class);
        app.setAdditionalProfiles("Forwarding");
        context = app.run(args);

        // Get the port chosen by Spring Boot
        Environment env = context.getBean(Environment.class);
        String port = env.getProperty("local.server.port");
        System.out.println("Forwarding started on port " + port);

        ForwardingApp forwardingApp = context.getBean(ForwardingApp.class);
        forwardingApp.start();
    }

}

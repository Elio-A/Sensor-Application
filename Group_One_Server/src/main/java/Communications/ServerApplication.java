package Communications;

import DataManagementSystem.DataManagerTestingEntryPoint;
import FrontEndManagementSystem.FrontEndManagerTestingEntryPoint;
import MockSystem.Mock_ServerEntryPoint;
import MonitoringManagementSystem.MonitoringManagerTestingEntryPoint;
import SensorManagementSystem.SensorManagerTestingEntryPoint;
import ServerLogicEntryPoint.IEntryPoint;
import ServerLogicEntryPoint.ServerEntryPoint;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * The entry point for the Server application, bootstrapped using Spring Boot.
 */
@SpringBootApplication
public class ServerApplication {

    static ConfigurableApplicationContext context;
    static IEntryPoint entryPoint;
    static boolean isRunning = false;
    static final ExecutorService switchExecutor = Executors.newSingleThreadExecutor();
    static Future<?> switchFuture;
    static CommunicationsController commsController;

    static int MAX_RETRY_ATTEMPTS;
    /**
     * The main entry point of the application.
     *
     * @param args Command-line arguments, if provided, to set up and start the application.
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

            if (args.length < 2 || !isValidSystemType(args[0].toLowerCase()) || !isValidMaxRetry(args[1])) {
                System.out.println("Autostart arguments not provided. Manual setup required:\n\n");
                String systemType;
                do {
                    System.out.println("Enter the name of the system you would like to test or type \"full server\" " +
                            "to test entire system (sensor management, monitoring management, frontend management, data management, " +
                            "full server, mock server). E.g., full server: ");
                    systemType = scanner.nextLine().toLowerCase();
                } while (!isValidSystemType(systemType));

                setupEntryPoint(systemType);

            } else {
                setupEntryPoint(args[0].toLowerCase());
                try {
                    MAX_RETRY_ATTEMPTS = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    System.err.println("Unexpected error when parsing max retry attempts.");
                    return;
                }
                context = ServerApplication.run();
                commsController = context.getBean(CommunicationsController.class);
                entryPoint.start(commsController);
            }
            interactiveConsole();
        } finally {
            switchExecutor.shutdown();  // Shutting down the executor
        }
    }
    @Bean
    public ServletWebServerFactory servletContainer() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        tomcat.addConnectorCustomizers(connector -> {
            if (connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?>) {
                AbstractHttp11Protocol<?> protocol = (AbstractHttp11Protocol<?>) connector.getProtocolHandler();
                protocol.setMaxSwallowSize(-1); // Example to set it to unlimited
            }
        });
        return tomcat;
    }

    /**
     * Validates the provided maximum retry value.
     *
     * @param retry The string representation of the retry value to validate.
     * @return true if valid, false otherwise.
     */
    private static boolean isValidMaxRetry(String retry) {
        try {
            int value = Integer.parseInt(retry);
            return value > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks if the provided system type is valid.
     *
     * @param type The string representation of the system type.
     * @return true if valid, false otherwise.
     */
    private static boolean isValidSystemType(String type) {
        switch (type.replaceAll("\\s+", "")) {
            case "sensormanagement":
            case "monitoringmanagement":
            case "frontendmanagement":
            case "datamanagement":
            case "fullserver":
            case "mockserver":
                return true;
            default:
                System.err.println("Unrecognized test type: " +type+"\nPlease enter a valid system type.");
                return false;
        }
    }
    /**
     * Sets up the main entry point based on the provided system type.
     *
     * @param type The system type to set up.
     */
    private static void setupEntryPoint(String type) {
        switch (type.replaceAll("\\s+", "")) {
            case "sensormanagement":
                entryPoint = new SensorManagerTestingEntryPoint(MAX_RETRY_ATTEMPTS);
                break;
            case "monitoringmanagement":
                entryPoint = new MonitoringManagerTestingEntryPoint(MAX_RETRY_ATTEMPTS);
                break;
            case "frontendmanagement":
                entryPoint = new FrontEndManagerTestingEntryPoint(MAX_RETRY_ATTEMPTS);
                break;
            case "datamanagement":
                entryPoint = new DataManagerTestingEntryPoint(MAX_RETRY_ATTEMPTS);
                break;
            case "fullserver":
                entryPoint = new ServerEntryPoint(MAX_RETRY_ATTEMPTS);
                break;
            case "mockserver":
                entryPoint = new Mock_ServerEntryPoint(MAX_RETRY_ATTEMPTS);
        }

    }
    /**
     * Manages the interactive console, providing users with command options
     * and responding to their inputs.
     */
    private static void interactiveConsole() {
        Scanner scanner = new Scanner(System.in);
        String command;

        System.out.println("Enter a command (type 'help' for a list of commands):");

        while (true) {
            System.out.print("> ");
            command = scanner.nextLine().trim().toLowerCase();

            switch (command) {
                case "start":
                    if (entryPoint == null){
                        System.out.println("\nPlease select a valid system type before attempting to start the system.");
                    }
                    else if (!isRunning) {
                        while (MAX_RETRY_ATTEMPTS < 1) {
                            System.out.println("Enter the max number of retry attempts (a positive number that will be " +
                                    "used to determine the number of initialization attempts " +
                                    "before a subsystem is considered to be malfunctioning) when " +
                                    "initializing a subsystem. E.g., 15: ");
                            try {
                                MAX_RETRY_ATTEMPTS = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                System.err.println("Invalid number format. Please enter a positive integer.");
                                MAX_RETRY_ATTEMPTS = -1;
                            }
                        }
                        context = ServerApplication.run();
                        commsController = context.getBean(CommunicationsController.class);
                        entryPoint.start(commsController);
                        isRunning = true;
                        System.out.println("\nSystem started.");
                    } else {
                        System.out.println("System is already running.");
                    }
                    break;

                case "stop":
                    if (entryPoint == null){
                        System.out.println("\nPlease select a valid system type before attempting to " +
                                "start or stop the system.");
                    }else if (isRunning) {
                        entryPoint.shutdown();
                        isRunning = false;
                        System.out.println("\nSystem stopped.");
                    } else {
                        System.out.println("System is not running.");
                    }
                    break;

                case "exit":
                    if (isRunning) {
                        entryPoint.shutdown();
                    }
                    if (context != null) {
                        SpringApplication.exit(context, () -> 0);
                    }
                    System.out.println("Exiting...");
                    scanner.close();
                    System.exit(0);
                    break;

                case "help":
                    displayHelp();
                    break;

                case "sensor management":
                case "monitoring management":
                case "frontend management":
                case "data management":
                case "full server":
                    if (!isRunning){
                        switchEntryPoint(command);
                        System.out.println("\nSwitched to " + command + " system. Use the 'start' command to start it.");
                    }
                    else{
                        System.out.println("\nPlease stop the current system before attempting to switch systems.");
                    }

                    break;

                default:
                    System.out.println("Invalid command. Type 'help' for a list of commands.");
                    break;
            }
        }
    }
    /**
     * Switches to a different system type while the application is running.
     *
     * @param type The system type to switch to.
     */
    private static void switchEntryPoint(String type) {
        if (isRunning) {
            System.out.println("Shutting down current system...");
            switchFuture = switchExecutor.submit(() -> {
                entryPoint.shutdown();
                isRunning = false;
            });
            try {
                switchFuture.get(); // Wait for shutdown to complete
            } catch (Exception e) {
                System.err.println("Error during shutdown: " + e.getMessage());
            }
        }
        setupEntryPoint(type);

        System.out.println("Switched to " + type + " system. Use the 'start' command to start it.");
    }
    /**
     * Displays the available commands to the user.
     */
    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  start                  - Start the current system.");
        System.out.println("  stop                   - Stop the current system.");
        System.out.println("  sensor management      - Switch to the Sensor Management system.");
        System.out.println("  monitoring management  - Switch to the Monitoring Management system.");
        System.out.println("  frontend management    - Switch to the Front End Management system.");
        System.out.println("  data management        - Switch to the Data Management system.");
        System.out.println("  full server            - Switch to the Full Server system.");
        System.out.println("  mock server            - Switch to the Mock Server system.");
        System.out.println("  exit                   - Stop the system (if running) and exit the program.");
        System.out.println("  help                   - Display this help message.");
    }

    public static ConfigurableApplicationContext run() {
        return SpringApplication.run(ServerApplication.class);
    }
}

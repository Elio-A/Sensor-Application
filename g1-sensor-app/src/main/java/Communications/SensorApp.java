package Communications;

import SharedDataTypes.SensorObjects.SensorObject;
import SharedDataTypes.SensorState;
import SharedDataTypes.SensorType;
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
public class SensorApp {

    public static int errorMode = 0;
    private static ConfigurableApplicationContext context;
    private static final Logger logger = LoggerFactory.getLogger(SensorApp.class);
    public static String name = "DefaultSensor";
    public static String nickName = "DefaultSensor";
    public static int sampleRate = 5;

    public static float max = 5;

    public static float min = 5;
    public static SensorType type = SensorType.Energy;
    public static String sensorDescription = "testing sensor";
    public static String sensorLocation = "the aether";
    public static SensorState sensorState = SensorState.ActiveFunctioning;
    public static SensorDataGenerator dataGen;

    public static SensorApp Instance;


    @Autowired
    private CommunicationsService commsService;
    public static void main(String[] args) {
        if (args.length >= 1) {
            name = args[0];
        }
        if (args.length >= 2) {
            nickName = args[1].replace('_', ' ');
        }
        if (args.length >= 3) {
            int temp = sampleRate;
            try {
                sampleRate = Integer.parseInt(args[2]);
            } catch (Exception e) {
                sampleRate = temp;
                System.out.println("Invalid sample rate provided. Using default: " + sampleRate);
            }
        }
        if (args.length >= 4) {
            float temp = max;
            try {
                max = Float.parseFloat(args[3]);
            } catch (Exception e) {
                max = temp;
                System.out.println("Invalid max value provided. Using default: " + max);
            }
        }
        if (args.length >= 5) {
            float temp = min;
            try {
                min = Float.parseFloat(args[4]);
            } catch (Exception e) {
                min = temp;
                System.out.println("Invalid min value provided. Using default: " + min);
            }
        }
        if (args.length >= 6) {
            SensorType temp = type;
            try {
                type = SensorType.fromValue(Integer.parseInt(args[5]));
            } catch (Exception e) {
                type = temp;
                System.out.println("Invalid sensor type provided. Using default: " + type);
            }
        }
        if (args.length >= 7) {
            sensorDescription = args[6].replace('_', ' ');
        }
        if (args.length >= 8) {
            sensorLocation = args[7].replace('_', ' ');
        }
        if (args.length >= 9) {
            SensorState temp = sensorState;
            try {
                sensorState = SensorState.fromValue(Integer.parseInt(args[8]));
            } catch (Exception e) {
                sensorState = temp;
                System.out.println("Invalid sensor state provided. Using default: " + sensorState);
            }
        }

        System.out.println("Sensor Name: " + name);
        SensorApp.begin(args, name);
    }


    public void UpdateSensorInformation(SensorObject newInfo){

            System.out.println("HERE: "+ newInfo);

           nickName = newInfo.nickName;
           sampleRate = (int)newInfo.sampleRate;

           max = newInfo.max;
           min =  newInfo.min;
           type =  newInfo.type;
           sensorDescription =  newInfo.description;
           sensorLocation =  newInfo.location;
           sensorState =  newInfo.state;
           errorMode = newInfo.errorMode;
    }

    public static void begin(String[] args, String name) {
        SensorApp.name = name;
        // Start Spring Boot application and let it choose a free port
        SpringApplication app = new SpringApplication(SensorApp.class);
        app.setAdditionalProfiles("sensor");
        context = app.run(args);

        // Get the port chosen by Spring Boot
        Environment env = context.getBean(Environment.class);
        String port = env.getProperty("local.server.port");
        System.out.println("SensorApp started on port " + port);

        SensorApp sensorApp = context.getBean(SensorApp.class);

        if (args.length>0)
            sensorApp.start(true);
        else
            sensorApp.start(false);
    }
    public SensorApp() {
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

    public void start(boolean autoStart) {
        Scanner scanner = new Scanner(System.in);
        Instance = this;
        logger.info("Initializing communications...");
        System.out.println("Communications initialized.");
        while (!commsService.serverResolved) {
            try {
                logger.info("Waiting for JmDNS service resolution...");
                Thread.sleep(1000);  // waits for 5 seconds
            } catch (InterruptedException e) {
                logger.error("Interrupted while waiting for JmDNS service resolution: {}", e.getMessage());
            }
        }
        if (autoStart){
            startSampling();
        }
        while (true) {
            System.out.println("Enter your command (or 'exit' to quit) [Type 'help' for available commands]:");
            String command = scanner.nextLine().trim().toLowerCase();

            if ("exit".equalsIgnoreCase(command)) {
                System.out.println("Exiting application.");

                break;
            }

            switch (command) {
                case "change sample rate":
                    changeSampleRate(scanner);
                    break;
                case "change sensor type":
                    changeSensorType(scanner);
                    break;
                case "change sensor state":
                    changeSensorState(scanner);
                    break;
                case "change sensor description":
                    changeSensorDescription(scanner);
                    break;
                case "change sensor location":
                    changeSensorLocation(scanner);
                    break;
                case "stop sampling":
                    stopSampling();
                    break;
                case "start sampling":
                    startSampling();
                    break;
                case "change error mode":
                    changeErrorMode(scanner);
                    break;
                case "help":
                    displayHelp();
                    break;
                default:
                    System.out.println("Invalid command. Type 'help' for a list of commands.");
            }
        }
        scanner.close();
        if (context != null) {
            SpringApplication.exit(context, () -> 0);
        }
        System.exit(0);
    }


    public void startSampling(){
        if (dataGen == null){
            dataGen =  new SensorDataGenerator(commsService);
        }
        dataGen.start();
    }
    public void stopSampling(){
        if (dataGen == null){
            dataGen =  new SensorDataGenerator(commsService);
        }
        dataGen.stop();
    }
    private void displayHelp() {
        System.out.println("Available Commands:");
        System.out.println("- change error mode: Change the error mode for testing.");
        System.out.println("- change sample rate: Change the sample rate.");
        System.out.println("- change sensor type: Choose a new sensor type.");
        System.out.println("- change sensor state: Choose a new sensor state.");
        System.out.println("- change sensor description: Set a new description for the sensor.");
        System.out.println("- change sensor location: Set a new location for the sensor.");
        System.out.println("- stop sampling: Stop sampling.");
        System.out.println("- start sampling: Start sampling.");
        System.out.println("- exit: Exit the application.");
    }

    private void changeSampleRate(Scanner scanner) {
        while (true) {
            System.out.println("Enter the new sample rate (positive integer):");
            try {
                int rate = Integer.parseInt(scanner.nextLine());
                if (rate > 0) {
                    sampleRate = rate;
                    System.out.println("Sample rate changed to " + rate);
                    stopSampling();
                    startSampling();
                    break;
                } else {
                    System.out.println("Please enter a positive integer.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer.");
            }
        }
    }

    private void changeSensorType(Scanner scanner) {
        while (true) {
            System.out.println("Choose sensor type:");
            int i = 0;
            for (SensorType type : SensorType.values()) {
                System.out.println(i++ + ". " + type);
            }
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 0 && choice < SensorType.values().length) {
                    type = SensorType.values()[choice];
                    System.out.println("Sensor type changed to " + type);
                    break;
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please select from the provided options.");
            }
        }
    }
    private void changeErrorMode(Scanner scanner) {
        while (true) {
            System.out.println("Enter the new error mode (integer between 0 and 2):");
            try {
                int errorMode = Integer.parseInt(scanner.nextLine());
                if (errorMode >= 0 && errorMode<3) {
                    SensorApp.errorMode = errorMode;
                    System.out.println("Error mode changed to: " + errorMode);

                    break;
                } else {
                    System.out.println("Please enter a positive integer between 0 and 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a positive integer between 0 and 2.");
            }
        }

    }

    private void changeSensorState(Scanner scanner) {
        while (true) {
            System.out.println("Choose sensor state:");
            int i = 0;
            for (SensorState state : SensorState.values()) {
                System.out.println(i++ + ". " + state);
            }
            try {
                int choice = Integer.parseInt(scanner.nextLine());
                if (choice >= 0 && choice < SensorState.values().length) {
                    sensorState = SensorState.values()[choice];
                    System.out.println("Sensor state changed to " + sensorState);
                    break;
                } else {
                    System.out.println("Invalid choice.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please select from the provided options.");
            }
        }
    }

    private void changeSensorDescription(Scanner scanner) {
        while (true) {
            System.out.println("Enter the new sensor description:");
            String desc = scanner.nextLine().trim();
            if (!desc.isEmpty()) {
                sensorDescription = desc;
                System.out.println("Sensor description changed to: " + sensorDescription);
                break;
            } else {
                System.out.println("Description cannot be empty.");
            }
        }
    }
    private void changeSensorLocation(Scanner scanner) {
        while (true) {
            System.out.println("Enter the new sensor location:");
            String location = scanner.nextLine().trim();
            if (!location.isEmpty()) {
                sensorLocation = location;
                System.out.println("Sensor location changed to: " + sensorLocation);
                break;
            } else {
                System.out.println("Location cannot be empty.");
            }
        }
    }
}

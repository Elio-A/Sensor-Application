package Marshalling;

import Communications.CommunicationsController;
import DataManagementSystem.IDataManagementSystem;
import FrontEndManagementSystem.IFrontEndManagementSystem;
import MonitoringManagementSystem.IMonitoringManagementSystem;
import SensorManagementSystem.ISensorManagementSystem;

public class InternalServerMarshall {

    private ISensorManagementSystem sensorSystem;
    private IMonitoringManagementSystem monitoringSystem;
    private IDataManagementSystem dataManagementSystem;
    private IFrontEndManagementSystem frontEndSystem;
    private CommunicationsController communicationsController;

    // Singleton instance, declared as volatile to ensure visibility across threads
    private static volatile InternalServerMarshall instance = null;

    // Private constructor to prevent direct instantiation
    private InternalServerMarshall(ISensorManagementSystem sensorSystem,
                                   IMonitoringManagementSystem monitoringSystem,
                                   IDataManagementSystem dataManagementSystem,
                                   IFrontEndManagementSystem frontEndSystem, CommunicationsController communicationsController) {
        this.sensorSystem = sensorSystem;
        this.monitoringSystem = monitoringSystem;
        this.dataManagementSystem = dataManagementSystem;
        this.frontEndSystem = frontEndSystem;
        this.communicationsController = communicationsController;
    }

    // Static method to provide thread-safe Singleton instantiation
    public static InternalServerMarshall getInstance(ISensorManagementSystem sensorSystem,
                                                     IMonitoringManagementSystem monitoringSystem,
                                                     IDataManagementSystem dataManagementSystem,
                                                     IFrontEndManagementSystem frontEndSystem, CommunicationsController communicationsController) {
        if (instance == null) {  // first check, without locking for performance reasons
            synchronized (InternalServerMarshall.class) {
                if (instance == null) {  // second check, with locking
                    instance = new InternalServerMarshall(sensorSystem, monitoringSystem, dataManagementSystem, frontEndSystem, communicationsController);
                }
            }
        }
        return instance;
    }

    public static ISensorManagementSystem getSensorSystem() {
        try{
            return instance.sensorSystem;
        }
        catch(Exception e){
            return null;
        }
    }

    public static IMonitoringManagementSystem getMonitoringSystem() {
        try{
            return instance.monitoringSystem;
        }
        catch(Exception e){
            return null;
        }
    }

    public static IDataManagementSystem getDataManagementSystem() {
        try{
            return instance.dataManagementSystem;
        }
        catch(Exception e){
            return null;
        }
    }

    public static IFrontEndManagementSystem getFrontEndSystem() {
        try{
            return instance.frontEndSystem;
        }
        catch(Exception e){
            return null;
        }
    }
    public static CommunicationsController getCommunicationsController() {
        try{
            return instance.communicationsController;
        }
        catch(Exception e){
            return null;
        }
    }

}

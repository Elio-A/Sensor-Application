package ServerLogicEntryPoint;

import Communications.CommunicationsController;

public interface IEntryPoint {
    void start(CommunicationsController commsController);
    void shutdown();
}

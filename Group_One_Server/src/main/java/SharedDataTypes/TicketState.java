package SharedDataTypes;
public enum TicketState {
    Unassigned(0),
    NotStarted(1),
    InProgress(2),
    Completed(3);
    private final int value;
    TicketState(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static SharedDataTypes.TicketState fromValue(int value) {
        for (SharedDataTypes.TicketState state : SharedDataTypes.TicketState.values()) {
            if (state.value == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}

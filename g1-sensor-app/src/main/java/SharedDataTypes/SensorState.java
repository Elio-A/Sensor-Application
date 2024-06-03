package SharedDataTypes;

public enum SensorState {
    ActiveMalfunctioning(0),
    ActiveFunctioning(1),
    InactiveMalfunctioning(2),
    InactiveFunctioning(3),
    Unknown(4);

    private final int value;

    SensorState(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SensorState fromValue(int value) {
        for (SensorState state : SensorState.values()) {
            if (state.value == value) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}


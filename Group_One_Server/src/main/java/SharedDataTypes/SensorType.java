package SharedDataTypes;

public enum SensorType {
    Energy(0),
    Water(1),
    Aggregated(2);

    private final int value;

    SensorType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static SensorType fromValue(int value) {
        for (SensorType type : SensorType.values()) {
            if (type.value == value) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid value: " + value);
    }
}

package com.parkingalot.domain;

public enum DeviceType {

    CAR("CAR", "Car",1),
    TRUCK("TRUCK", "Truck",4),
    JEEP("JEEP", "Jeep",2);

    private final String code;
    private final String cleanName;
    private final int slot;

    DeviceType(String code, String cleanName, int slot) {
        this.code = code;
        this.cleanName = cleanName;
        this.slot = slot;
    }

    public static DeviceType fromCode(String code) {
        for (DeviceType value : values()) {
            if (value.getCode().equalsIgnoreCase(code)) {
                return value;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getCleanName() {
        return cleanName;
    }

    public int getSlot() {
        return slot;
    }
}

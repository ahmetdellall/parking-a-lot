package com.parkingalot.domain;

public class Device {

    private String regNo;
    private String color;
    private DeviceType deviceType;

    public Device() {
    }

    public Device(String regNo, String color, DeviceType deviceType) {
        this.regNo = regNo;
        this.color = color;
        this.deviceType = deviceType;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }
}

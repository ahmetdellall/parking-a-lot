package com.parkingalot.domain;

import java.util.ArrayList;
import java.util.List;

public class ResponseStatusDTO {

    private String regNo;
    private String color;
    private List<String> slotNumbers = new ArrayList<>();

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

    public List<String> getSlotNumbers() {
        return slotNumbers;
    }

    public void setSlotNumbers(List<String> slotNumbers) {
        this.slotNumbers = slotNumbers;
    }
}

package com.parkingalot.service;

import com.parkingalot.domain.Device;
import com.parkingalot.domain.ResponseStatusDTO;

import java.util.ArrayList;
import java.util.Map;

public interface ParkService {

    void park(Device device);

    void leave(String slotNo);

    Map<Device, ArrayList<Integer>> status();

    ArrayList<Integer> getAvailableSlotList();

    Map<String, Device> getSlotAndCarMap();

}

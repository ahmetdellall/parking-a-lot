package com.parkingalot.service;

import com.parkingalot.domain.Device;
import com.parkingalot.exception.SlotFullException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class ParkServiceImpl implements ParkService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkServiceImpl.class);

    //Duplicated string literals make the process of refactoring error-prone, since you must be sure to update all occurrences.
    private static final String ERROR_PARKING_CREATE_MESSAGE = "Sorry, parking lot is not created";
    private static final String NOT_FOUND_MESSAGE = "NOT FOUND";
    private int maxSize = 10;

    // Available slots list
    private ArrayList<Integer> availableSlotList;

    // Map of Slot, Car
    private Map<String, Device> slotAndDeviceMap;

    // Map of RegNo, Slot
    private Map<String, String> regNOAndSlotMap;

    // Map of Color, List of RegNo
    private Map<String, ArrayList<String>> regNoListAndColorMap;

    {
        availableSlotList = new ArrayList<Integer>() {
        };

        for (int i = 1; i <= maxSize; i++) {
            availableSlotList.add(i);
        }

        // Tread safe:
        // HashMap performance is relatively high because it is non-synchronized in nature and
        // any number of threads can perform simultaneously. But ConcurrentHashMap
        // performance is low sometimes because sometimes Threads are required to wait on ConcurrentHashMap.
        // While one thread is Iterating the HashMap object, if other thread
        // try to add/modify the contents of Object then we will get Run-time exception
        // saying ConcurrentModificationException.Whereas In ConcurrentHashMap we wont
        // get any exception while performing any modification at the time of Iteration.
        slotAndDeviceMap = new ConcurrentHashMap<>();
        regNOAndSlotMap = new ConcurrentHashMap<>();
        regNoListAndColorMap = new ConcurrentHashMap<>();
    }

    @Override
    public void park(Device device) {
        if (this.slotAndDeviceMap.size() + device.getDeviceType().getSlot() >= this.maxSize) {
            LOGGER.info("Sorry, parking slot is full");
            throw new SlotFullException("Sorry, parking slot is full");
        } else {
            for (int i = 0; i <= device.getDeviceType().getSlot(); i++) {
                String slot = availableSlotList.get(0).toString();
                this.slotAndDeviceMap.put(slot, device);
                this.regNOAndSlotMap.put(device.getRegNo(), slot);
                if (i == device.getDeviceType().getSlot()) {
                    this.slotAndDeviceMap.put(slot, new Device());
                    this.regNOAndSlotMap.put("", slot);
                }
                availableSlotList.remove(0);
            }

            ArrayList<String> regNoList;
            if (this.regNoListAndColorMap.containsKey(device.getColor())) {
                regNoList = this.regNoListAndColorMap.get(device.getColor());
                this.regNoListAndColorMap.remove(device.getColor());
            } else {
                regNoList = new ArrayList<>();
            }
            regNoList.add(device.getRegNo());
            this.regNoListAndColorMap.put(device.getColor(), regNoList);

            LOGGER.info("Allocated slot number: {}", device.getDeviceType().getSlot());
        }
    }

    @Override
    public void leave(String slotNo) {
        if (this.slotAndDeviceMap.size() > 0) {
            Device deviceToLeave = this.slotAndDeviceMap.get(slotNo);
            if (deviceToLeave != null && deviceToLeave.getRegNo() != null) {

                Map<Device, ArrayList<Integer>> reverseMap = new HashMap(
                        slotAndDeviceMap.entrySet().stream()
                                .collect(Collectors.groupingBy(Map.Entry::getValue)).values().stream()
                                .collect(Collectors.toMap(
                                        item -> item.get(0).getValue(),
                                        item -> new ArrayList<>(
                                                item.stream()
                                                        .map(Map.Entry::getKey)
                                                        .collect(Collectors.toList())
                                        ))
                                ));

                ArrayList<Integer> slots = reverseMap.get(deviceToLeave);
                Integer lastSlot = Integer.parseInt(String.valueOf(slots.get(slots.size() - 1)));

                this.slotAndDeviceMap.entrySet()
                        .removeIf(stringDeviceEntry -> Objects.nonNull(stringDeviceEntry.getValue().getRegNo()) && stringDeviceEntry.getValue().getRegNo().equals(deviceToLeave.getRegNo()));

                this.regNoListAndColorMap.remove(deviceToLeave.getRegNo());

                ArrayList<String> regNoList = this.regNoListAndColorMap.get(deviceToLeave.getColor());
                if (regNoList.contains(deviceToLeave.getRegNo())) {
                    regNoList.remove(deviceToLeave.getColor());
                }
                // Add the slot No. back to available slot list.

                for (int i = 0; i < slots.size(); i++) {
                    int slot = Integer.parseInt(String.valueOf(slots.get(i)));
                    this.availableSlotList.add(slot);
                }

                this.availableSlotList.add(lastSlot + 1);
                LOGGER.info("Slot number {} is free", slotNo);
            } else {
                LOGGER.info("Slot number {} is already empty", slotNo);
            }
        } else {
            LOGGER.info("Parking slot is empty");
        }
    }

    @Override
    public Map<Device, ArrayList<Integer>> status() {
        if (this.slotAndDeviceMap.size() > 0) {
            Map<Device, ArrayList<Integer>> reverseMap = new HashMap(
                    slotAndDeviceMap.entrySet().stream()
                            .collect(Collectors.groupingBy(Map.Entry::getValue)).values().stream()
                            .collect(Collectors.toMap(
                                    item -> item.get(0).getValue(),
                                    item -> new ArrayList<>(
                                            item.stream()
                                                    .map(Map.Entry::getKey)
                                                    .collect(Collectors.toList())
                                    ))
                            ));

            return reverseMap.entrySet().stream()
                    .filter(deviceArrayListEntry -> deviceArrayListEntry.getKey().getRegNo() != null)
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } else {
            LOGGER.info("Parking lot is empty");
            return new HashMap<>();
        }
    }


    @Override
    public ArrayList<Integer> getAvailableSlotList() {
        return availableSlotList;
    }

    @Override
    public Map<String, Device> getSlotAndCarMap() {
        return slotAndDeviceMap;
    }

}

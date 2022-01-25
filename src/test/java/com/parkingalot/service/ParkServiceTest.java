package com.parkingalot.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parkingalot.domain.Device;
import com.parkingalot.domain.DeviceType;
import com.parkingalot.domain.ResponseStatusDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ParkServiceTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Autowired
    private ParkService parkService;

    @Test
    public void park() throws Exception {
        Device deviceCar = new Device();
        deviceCar.setDeviceType(DeviceType.CAR);
        deviceCar.setColor("Black");
        deviceCar.setRegNo("34-SO-1988");

        Device deviceTruck = new Device();
        deviceTruck.setDeviceType(DeviceType.TRUCK);
        deviceTruck.setColor("Red");
        deviceTruck.setRegNo("34-BO-1987");

        parkService.park(deviceCar);
        parkService.park(deviceTruck);

        assertEquals(3, parkService.getAvailableSlotList().size());
    }

    @Test
    public void leave() throws Exception {

        Device deviceCar = new Device();
        deviceCar.setDeviceType(DeviceType.CAR);
        deviceCar.setColor("Black");
        deviceCar.setRegNo("34-SO-1988");

        Device deviceTruck = new Device();
        deviceTruck.setDeviceType(DeviceType.TRUCK);
        deviceTruck.setColor("Red");
        deviceTruck.setRegNo("34-BO-1987");
        parkService.park(deviceCar);
        parkService.park(deviceTruck);
        Map<String, Device> slotAndCarMap = parkService.getSlotAndCarMap();
        parkService.leave("4");
        //truck size slot  = 4 and next step will be empty size= 5
        assertEquals(8, parkService.getAvailableSlotList().size());


        //Since the truck takes up 4 slots, even if we delete 4, 3,4,5,6 places will be full, and 5 slots are really empty.
        assertNotNull(parkService.getAvailableSlotList().get(3));
        assertNotNull(parkService.getAvailableSlotList().get(4));
        assertNotNull(parkService.getAvailableSlotList().get(5));
        assertNotNull(parkService.getAvailableSlotList().get(6));
        //next slot is empty
        assertNotNull(parkService.getAvailableSlotList().get(7));

    }

    @Test
    public void status() throws Exception {

        Device deviceCar = new Device();
        deviceCar.setDeviceType(DeviceType.CAR);
        deviceCar.setColor("Black");
        deviceCar.setRegNo("34-SO-1988");

        Device deviceTruck = new Device();
        deviceTruck.setDeviceType(DeviceType.TRUCK);
        deviceTruck.setColor("Red");
        deviceTruck.setRegNo("34-BO-1987");
        parkService.park(deviceCar);
        parkService.park(deviceTruck);

        Map<Device, ArrayList<Integer>> status = parkService.status();
        List<ResponseStatusDTO> responseStatusDTOList = new ArrayList<>();
        status.forEach((device, value) -> {
            ResponseStatusDTO responseStatusDTO = new ResponseStatusDTO();
            responseStatusDTO.setRegNo(device.getRegNo());
            responseStatusDTO.setColor(device.getColor());
            responseStatusDTO.setSlotNumbers(Collections.singletonList(value.toString()));
            responseStatusDTOList.add(responseStatusDTO);
        });

        String s = new Gson().toJson(responseStatusDTOList);


        assertEquals("[{\"regNo\":\"34-SO-1988\",\"color\":\"Black\",\"slotNumbers\":[\"[1]\"]},{\"regNo\":\"34-BO-1987\",\"color\":\"Red\",\"slotNumbers\":[\"[3, 4, 5, 6]\"]}]", s);
    }

}

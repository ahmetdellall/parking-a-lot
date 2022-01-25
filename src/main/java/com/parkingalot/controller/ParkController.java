package com.parkingalot.controller;

import com.parkingalot.domain.Device;
import com.parkingalot.domain.DeviceDTO;
import com.parkingalot.domain.ResponseStatusDTO;
import com.parkingalot.exception.SlotFullException;
import com.parkingalot.service.ParkService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/api/parking-a-lot")
public class ParkController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ParkController.class);

    @Autowired
    private ParkService parkService;

    @Autowired
    private ModelMapper modelMapper;

    @PostMapping("/park")
    @ResponseBody
    public ResponseEntity<String> park(@RequestBody DeviceDTO deviceDTO) {
        try {
            Device device = modelMapper.map(deviceDTO, Device.class);
            parkService.park(device);
            return ResponseEntity.status(HttpStatus.OK).body("Allocated slot number: " + device.getDeviceType().getSlot());
        } catch (SlotFullException e) {
            LOGGER.error(" {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        } catch (HttpMessageNotReadableException e) {
            LOGGER.error(" {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("You entered one of the values in the entered json missing or incorrectly.");
        }
    }


    @PutMapping("/leave/{slotNo}")
    @ResponseBody
    public void leave(@PathVariable String slotNo) {
        parkService.leave(slotNo);
    }

    @GetMapping("/status")
    public ResponseEntity<?> status() {
        try {
            Map<Device, ArrayList<Integer>> parkStatusMap = parkService.status();
            if (parkStatusMap.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK).body("Parking lot is empty");
            } else {
                List<ResponseStatusDTO> responseStatusDTOList = new ArrayList<>();
                parkStatusMap.forEach((device, value) -> {
                    ResponseStatusDTO responseStatusDTO = new ResponseStatusDTO();
                    responseStatusDTO.setRegNo(device.getRegNo());
                    responseStatusDTO.setColor(device.getColor());
                    responseStatusDTO.setSlotNumbers(Collections.singletonList(value.toString()));
                    responseStatusDTOList.add(responseStatusDTO);
                });
                return ResponseEntity.status(HttpStatus.OK).body(responseStatusDTOList);
            }
        } catch (Exception e) {
            LOGGER.error(" Error : {} ", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
        }
    }

}

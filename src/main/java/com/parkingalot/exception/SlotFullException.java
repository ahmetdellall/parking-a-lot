package com.parkingalot.exception;

public class SlotFullException extends RuntimeException {

    public SlotFullException(String message) {
        super(message);
    }

}

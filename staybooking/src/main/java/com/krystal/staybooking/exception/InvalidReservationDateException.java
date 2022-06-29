package com.krystal.staybooking.exception;

//format有问题 抛exception
public class InvalidReservationDateException extends RuntimeException {
    public InvalidReservationDateException(String message) {
        super(message);
    }
}
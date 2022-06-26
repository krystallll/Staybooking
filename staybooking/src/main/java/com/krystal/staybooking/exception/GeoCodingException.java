package com.krystal.staybooking.exception;


// if there’s any exception when we connect to Geolocation API.
public class GeoCodingException extends RuntimeException {
    public GeoCodingException(String message) {
        super(message);
    }
}

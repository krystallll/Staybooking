package com.krystal.staybooking.exception;

//match出现异常throw
public class UserNotExistException extends RuntimeException {
    public UserNotExistException(String message) {
        super(message);
    }
}
package com.krystal.staybooking.exception;

//被注册过抛异常
public class UserAlreadyExistException extends RuntimeException {
    public UserAlreadyExistException(String message) {
        super(message);
    }
}

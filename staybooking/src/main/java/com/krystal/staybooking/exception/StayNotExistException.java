package com.krystal.staybooking.exception;

//用户搜索的stay不存在 抛异常
public class StayNotExistException extends RuntimeException {
    public StayNotExistException(String message) {
        super(message);
    }
}

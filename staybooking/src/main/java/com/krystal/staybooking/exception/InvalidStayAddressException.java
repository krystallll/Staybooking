package com.krystal.staybooking.exception;

//用户上传地址有问题
public class InvalidStayAddressException extends RuntimeException {
    public InvalidStayAddressException(String message) {
        super(message);
    }
}

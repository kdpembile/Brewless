package com.brewless.order.exceptions;

import lombok.Getter;

@Getter
public class OrderException extends RuntimeException {

    private final String errorCode;

    public OrderException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "OS00002";
    }

}
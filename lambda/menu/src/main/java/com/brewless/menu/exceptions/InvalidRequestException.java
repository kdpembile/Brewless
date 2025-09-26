package com.brewless.menu.exceptions;

import lombok.Getter;

@Getter
public class InvalidRequestException extends RuntimeException {

    private final String errorCode;

    public InvalidRequestException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "M00003";
    }

}
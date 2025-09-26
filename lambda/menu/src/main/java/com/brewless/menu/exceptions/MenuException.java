package com.brewless.menu.exceptions;

import lombok.Getter;

@Getter
public class MenuException extends RuntimeException {

    private final String errorCode;

    public MenuException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "M00001";
    }

}
package com.brewless.menu_migration.exceptions;

import lombok.Getter;

@Getter
public class MigrationException extends RuntimeException {

    private final String errorCode;

    public MigrationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "MM00001";
    }

}
package com.brewless.menu_migration.exceptions;

public class MigrationException extends RuntimeException {

    private final String errorCode;

    public MigrationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "MM00001";
    }

    public String getErrorCode() {
        return errorCode;
    }
}
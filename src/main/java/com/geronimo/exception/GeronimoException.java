package com.geronimo.exception;

public abstract class GeronimoException extends RuntimeException {

    public GeronimoException(String message) {
        super(message);
    }
}

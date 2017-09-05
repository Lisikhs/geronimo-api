package com.geronimo.exceptions;

public abstract class GeronimoException extends RuntimeException {

    public GeronimoException(String message) {
        super(message);
    }
}

package com.geronimo.exception;

public class SelfFollowingException extends GeronimoException {

    public SelfFollowingException(String message) {
        super(message);
    }
}

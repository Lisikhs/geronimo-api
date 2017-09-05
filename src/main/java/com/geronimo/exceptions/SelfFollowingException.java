package com.geronimo.exceptions;

public class SelfFollowingException extends GeronimoException {

    public SelfFollowingException(String message) {
        super(message);
    }
}

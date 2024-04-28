package com.revature.exceptions;

public class InvalidStatusException extends Exception {

    public InvalidStatusException() {
    }

    public InvalidStatusException(String message) {
        super(message);
    }

    public InvalidStatusException(String message, Throwable cause) {
        super(message, cause);
    }

}

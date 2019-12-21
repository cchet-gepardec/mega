package com.gepardec.mega.security;

public class ForbiddenException extends SecurityException {
    public ForbiddenException() {
    }

    public ForbiddenException(String s) {
        super(s);
    }

    public ForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForbiddenException(Throwable cause) {
        super(cause);
    }
}

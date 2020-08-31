package me.stefan923.superlms.exceptions;

public class MissingPermissionException extends Exception {
    public MissingPermissionException() {
        super("");
    }

    public MissingPermissionException(final String string) {
        super(string);
    }
}

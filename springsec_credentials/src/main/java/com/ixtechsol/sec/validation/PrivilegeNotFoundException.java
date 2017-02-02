package com.ixtechsol.sec.validation;

public class PrivilegeNotFoundException extends Exception {
    private static final long serialVersionUID = -1402337502045439388L;

    public PrivilegeNotFoundException() {
    }

    public PrivilegeNotFoundException(String message) {
        super(message);
    }

    public PrivilegeNotFoundException(Throwable cause) {
        super(cause);
    }

    public PrivilegeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrivilegeNotFoundException(String message, Throwable cause,
            boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

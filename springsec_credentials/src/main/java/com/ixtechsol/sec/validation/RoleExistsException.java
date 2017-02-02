package com.ixtechsol.sec.validation;

public class RoleExistsException extends Exception {
    private static final long serialVersionUID = 4867645708199153376L;

    public RoleExistsException() {
    }

    public RoleExistsException(String arg0) {
        super(arg0);
    }

    public RoleExistsException(Throwable arg0) {
        super(arg0);
    }

    public RoleExistsException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public RoleExistsException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}

package com.tarzan.cms.common.exception;



public class SecureException extends RuntimeException {
    private static final long serialVersionUID = 2359767895161832954L;

    public SecureException(String message) {
        super(message);
    }

    public Throwable fillInStackTrace() {
        return this;
    }

}

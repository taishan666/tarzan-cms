package com.tarzan.cms.utils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

public class Exceptions {
    public Exceptions() {
    }

    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof Error) {
            throw (Error)e;
        } else if (!(e instanceof IllegalAccessException) && !(e instanceof IllegalArgumentException) && !(e instanceof NoSuchMethodException)) {
            if (e instanceof InvocationTargetException) {
                return new RuntimeException(((InvocationTargetException)e).getTargetException());
            } else if (e instanceof RuntimeException) {
                return (RuntimeException)e;
            } else {
                if (e instanceof InterruptedException) {
                    Thread.currentThread().interrupt();
                }

                assert runtime(e) instanceof RuntimeException;
                return runtime(e);
            }
        } else {
            return new IllegalArgumentException(e);
        }
    }

    private static <T extends Throwable> T runtime(Throwable throwable) throws T {
        return (T) throwable;
    }

    public static Throwable unwrap(Throwable wrapped) {
        Throwable unwrapped = wrapped;

        while(true) {
            while(!(unwrapped instanceof InvocationTargetException)) {
                if (!(unwrapped instanceof UndeclaredThrowableException)) {
                    return unwrapped;
                }

                unwrapped = ((UndeclaredThrowableException)unwrapped).getUndeclaredThrowable();
            }

            unwrapped = ((InvocationTargetException)unwrapped).getTargetException();
        }
    }

}

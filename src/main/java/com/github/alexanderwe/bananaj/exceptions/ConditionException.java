package com.github.alexanderwe.bananaj.exceptions;

/**
 * Created by alexanderweiss on 02.01.17.
 */
public class ConditionException extends Exception {

    public ConditionException()  {
        super("Invalid condition");
    }

    public ConditionException(String message) {
        super(message);
    }

    public ConditionException(Throwable cause) {
        super(cause);
    }

    public ConditionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConditionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}

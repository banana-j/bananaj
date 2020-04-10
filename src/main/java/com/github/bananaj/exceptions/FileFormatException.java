package com.github.bananaj.exceptions;

/**
 * Created by alexanderweiss on 11.02.16.
 */
public class FileFormatException extends Exception {

    public FileFormatException() {
        super("Invalid file format. Only use: .xls, .xlxs, .csv or .txt");
    }

    public FileFormatException(String message) {
        super(message);
    }

    public FileFormatException(Throwable cause) {
        super(cause);
    }

    public FileFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileFormatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

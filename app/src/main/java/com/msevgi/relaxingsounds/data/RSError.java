package com.msevgi.relaxingsounds.data;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class RSError {

    public static final int NO_CONNECTION = 1;
    public static final int NO_RESULT = 2;
    public static final int DEFAULT_ERROR = 3;

    private final int mCode;
    private final int mMessageCode;

    public RSError(int code, int messageCode) {
        this.mCode = code;
        this.mMessageCode = messageCode;
    }

    public int getCode() {
        return mCode;
    }

    public int getMessageCode() {
        return mMessageCode;
    }
}

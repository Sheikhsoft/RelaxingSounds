package com.msevgi.relaxingsounds.data;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class Error {
    private final int mCode;
    private final String mMessage;

    public Error(int mCode, String mMessage) {
        this.mCode = mCode;
        this.mMessage = mMessage;
    }

    public int getmCode() {
        return mCode;
    }

    public String getmMessage() {
        return mMessage;
    }

}

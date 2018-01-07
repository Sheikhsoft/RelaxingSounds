package com.msevgi.relaxingsounds.data;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class UIDataWrapper<T, C> implements DataWrapper<T> {
    private T data;
    private DataState dataState;
    private RSError error;

    public UIDataWrapper(T data, DataState dataState, RSError error) {
        this.data = data;
        this.dataState = dataState;
        this.error = error;
    }

    @Override
    public T getData() {
        return data;
    }

    @Override
    public DataState getState() {
        return dataState;
    }

    @Override
    public RSError getRSError() {
        return error;
    }

}

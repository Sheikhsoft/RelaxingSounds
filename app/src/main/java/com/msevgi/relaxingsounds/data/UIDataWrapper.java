package com.msevgi.relaxingsounds.data;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public class UIDataWrapper<T, C> implements DataWrapper<T> {
    private T data;
    private DataState dataState;
    private Error error;

    public UIDataWrapper(T data, DataState dataState, Error error) {
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
    public Error getError() {
        return error;
    }

}

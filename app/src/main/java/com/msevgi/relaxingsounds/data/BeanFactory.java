package com.msevgi.relaxingsounds.data;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public final class BeanFactory {
    private BeanFactory() {
        //no available constructor
    }

    public static <T> DataWrapper success(T data) {
        return new UIDataWrapper<>(data, DataState.SUCCESS, null);
    }

    public static <T> DataWrapper error(T data, Error error) {
        return new UIDataWrapper<>(data, DataState.ERROR, error);
    }

    public static <T> DataWrapper fetching(T data) {
        return new UIDataWrapper<>(data, DataState.FETCHING, null);
    }
}

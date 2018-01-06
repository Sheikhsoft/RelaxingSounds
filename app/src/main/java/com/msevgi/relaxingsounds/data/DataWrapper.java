package com.msevgi.relaxingsounds.data;

/**
 * Created by mustafasevgi on 3.01.2018.
 */

public interface DataWrapper<T> {
    T getData();

    DataState getState();

    Error getError();
}

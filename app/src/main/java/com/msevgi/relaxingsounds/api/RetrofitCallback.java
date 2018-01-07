package com.msevgi.relaxingsounds.api;

import java.net.UnknownHostException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by mustafasevgi on 7.01.2018.
 */

public abstract class RetrofitCallback<T> implements Callback<T> {
    public abstract void failure(Call<T> call, Throwable t);

    public abstract void success(Call<T> call, T responseBean);

    public abstract void noInternetConnection();

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        success(call, response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        if (t instanceof UnknownHostException) {
            noInternetConnection();
        } else failure(call, t);
    }
}

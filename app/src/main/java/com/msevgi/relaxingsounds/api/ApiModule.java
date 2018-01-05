package com.msevgi.relaxingsounds.api;

import com.msevgi.relaxingsounds.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mustafasevgi on 4.01.2018.
 */

public class ApiModule {

    private static ApiModule sInstance;

    private Services mService;

    public static void init() {
        sInstance = new ApiModule();
    }

    private ApiModule() {
        setUp();
    }

    public static ApiModule getInstance() {
        if (sInstance == null) {
            throw new NullPointerException("call init first");
        }
        return sInstance;
    }

    private void setUp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create()).baseUrl(BuildConfig.BASE_URL);

        Retrofit mRetrofit = builder.build();

        mService = mRetrofit.create(Services.class);
    }

    public Services getService() {
        return mService;
    }
}
